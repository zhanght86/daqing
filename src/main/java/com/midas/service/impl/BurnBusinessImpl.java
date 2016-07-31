package com.midas.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;
import com.midas.constant.ErrorConstant;
import com.midas.constant.ServiceConstant;
import com.midas.constant.SysConstant;
import com.midas.context.SpringContextHelper;
import com.midas.enums.BurnState;
import com.midas.enums.DataSource;
import com.midas.enums.DataType;
import com.midas.enums.ExportState;
import com.midas.exception.ServiceException;
import com.midas.service.BurnBusiness;
import com.midas.service.BurnService;
import com.midas.service.CommonService;
import com.midas.service.DataService;
import com.midas.service.StandingbookService;
import com.midas.uitls.date.DateUtil;
import com.midas.uitls.file.FileOper;
import com.midas.uitls.file.LocalFileOper;
import com.midas.uitls.runtime.RunCommand;
import com.midas.uitls.threadpool.ThreadPoolS;
import com.midas.uitls.tools.CommonsUtils;
import com.midas.uitls.tools.EnumUtils;
import com.midas.uitls.tools.StringTools;
import com.midas.vo.BurnProgress;

/**
 * 刻盘, 获取下载完成的内容, 开始刻录任务
 * 
 * @author arron
 *
 */
@Service
public class BurnBusinessImpl extends BurnBase implements BurnBusiness {

    private static final Logger logger = LoggerFactory.getLogger(BurnBusinessImpl.class);

    @Autowired
    private CommonService commonService;
    @Autowired
    private BurnService   burnService;
    // 文件操作
    private FileOper      fileOper = new LocalFileOper();

    // 先不指定， 根据内容获取具体的Bean对交易处理
    private DataService dataService;

    @Autowired
    private StandingbookService standingbookService;
    
    /**
     * 
     * @param indexFile
     *            索引文件
     * @param dataType
     *            数据类型
     * @param dataSource
     *            数据来源: {@link DataSource}
     * @param discType
     *            盘类型
     */
    @Override
    public void master(String filename, String indexFile, String dataType, String dataSource, String discType,String exportPath,String server)
            throws ServiceException {
    	
        // 获取可以使用的盘库数据为系统参数表外加系统可用盘的数量
    	 //  Map<String, Object> burnMapParam =null;
        Map<String, Object> burnMapParam = burnService.getFreeBurn();
        if (burnMapParam == null || burnMapParam.isEmpty()) {
            throw new ServiceException(ErrorConstant.CODE4000, "没有盘库可以使用!");
        }
        // 读取Excel索引文件
        List<Map<String, Object>> list = FileToList(indexFile, dataType);
        // 获取上传数据估计总大小
        BigDecimal bd = getDataQuantity(list);
        // 检查盘是否足够
        boolean bool = checkDisc(bd, discType, burnMapParam.get(ServiceConstant.EFF_QUAN));
        if (!bool) {
            throw new ServiceException(ErrorConstant.CODE3000,
                    "盘库: {" + burnMapParam.get("sp_name") + "}中盘的数量不足, 需要继续执行请先增加盘");
        }
        
        String burnMachine="";
        if (!StringTools.isEmpty(server)) {
            burnMachine = server;
            logger.info("选择盘库{}",server);
        } else {
        
            burnMachine = ObjectUtils.toString(burnMapParam.get("sp_value1"));
            logger.info("分配盘库{}",burnMachine);
        }   
     
        String valLabel = burnService.insertBurn(filename, discType, dataSource, burnMachine, dataType,exportPath); // 获取卷标号
        setDataService(dataType);
        // 添加数据索引
        dataService.insertBetch(list, valLabel);
        // 验证文件是否正确
        List<String> filepathList = dataService.getFilepath(valLabel,dataSource);
        // 检查文件
        checkFile(filepathList, dataSource, valLabel);
        boolean isCancel = burnService.isCancel(valLabel);
        if (!isCancel) {
            Thread t = new Thread(new Burn(valLabel));
            t.start();
            String threadName = t.getName();
            logger.info("已经通过线程启动刻录任务: {}, 线程名称为: {}", valLabel, threadName);
        } else {
            logger.info("用户已经取消刻录任务或者状态不对： {}", isCancel);
        }

        // 验证都已经结束， 下载文件和启动刻录
    }

    /**
     * 检查所有文件是否存在
     * 
     * TODO 文件验证
     * 
     * @param filepathList
     * @param dataSource
     */
    private void checkFile(List<String> filepathList, String dataSource, String volLabel) {
        String sourceWorkdir = null;
        if (DataSource.HARD_DISK.getKey().equals(dataSource)) {
            // 移动硬盘
            sourceWorkdir = getLocalPath(volLabel);
        } else if (DataSource.NETWORK_SHARING.getKey().equals(dataSource)) {
            // 网络共享
            Map<String, Object> map = commonService.getSystemParameters(SysConstant.SHARED_NETWORK_ADDRESS);
            sourceWorkdir = ObjectUtils.toString(map.get("sp_value4"));
        }
        for (String s : filepathList) {
            File f = new File(sourceWorkdir + "/" + s);
            logger.info("验证数据文件: {}", f.getAbsolutePath());

            File f2 = new File(f.getParent());
            
            if (!f2.exists()) {
                throw new ServiceException(ErrorConstant.CODE4000, "文件路径不存在：" + f2.getAbsolutePath());
            }

            String workArea = f2.getName();
            BigDecimal filesize = new BigDecimal(0);
            
            boolean isOk = false;
            for (File filename : f2.listFiles()) {
                if (filename.getName().startsWith(f.getName())) {
                    if(filename.isDirectory()) {
                        for(File fc : filename.listFiles()) {
                            filesize = filesize.add(new BigDecimal(fc.length()));
                        }
                    } else {
                        filesize = filesize.add(new BigDecimal(filename.length()));
                    }
                    isOk = true;
                }
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("work_area", workArea);
            map.put("volume_label", volLabel);
            map.put("data_quantity", filesize);
            map.put("type", 1);
            //standingbookService.update(map);
            logger.info("刻录任务文件总大小{}",filesize);
            logger.info("检查文件是否存在：{}, 结果： {}", f.getAbsolutePath(), f.exists());
            if (!isOk) {
                throw new ServiceException(ErrorConstant.CODE4000, "文件： [" + s + "], 未找到， 请提供验证数据后在操作");
            }
        }
    }

    @Override
    public void masterBurn(String volLabel) {
        Map<String, Object> burnResultMap = burnService.getBurnByVolLabel(volLabel);
        if (null == burnResultMap || burnResultMap.isEmpty()) {
            throw new ServiceException(ErrorConstant.CODE2000, "没有需要刻录的任务");
        }
        
        if(burnService.isCancel(volLabel)) {
            throw new ServiceException(ErrorConstant.CODE2000, "用户已经取消刻录");
        }
        
        setDataService(ObjectUtils.toString(burnResultMap.get("data_type")));
        /**
         * 获取所有的文件路径
         */
        // 数据来源：
        String dataSource = ObjectUtils.toString(burnResultMap.get("data_source"));
        List<String> list = dataService.getFilepath(volLabel,dataSource);
       
        // 刻录的光盘类型
        String discType = ObjectUtils.toString(burnResultMap.get("disc_type"));
        // 刻录设备
        String burnMachine = ObjectUtils.toString(burnResultMap.get("burning_machine"));
        long st = System.currentTimeMillis();
        List<Future<?>> listFutures = new ArrayList<Future<?>>();
        int seqNumber = 1;
        // 开始下载
        burnService.updateState(volLabel, BurnState.UPLOAD_INIT.getKey(), "", 0);
        for (String filepath : list) {
            logger.info("下载的文件路径为: {}", filepath);
            Future<?> f = executeSubmit(new Downfile(burnMachine, volLabel, dataSource, filepath, seqNumber));
            listFutures.add(f);
            seqNumber++;
        }
        boolean bool = true;
        String errInfo = null;
        for (Future<?> f : listFutures) {
            try {
                f.get();
            } catch (Exception e) {
                logger.error("下载数据失败ExecutionException", e);
                if (e.getCause() != null) {
                    errInfo = e.getCause().getMessage();
                }
                bool = false;
                break;
            }
        }
        if (bool) {
            burnService.updateState(volLabel, BurnState.UPLOAD_SUCCESS.getKey(), "", 0);
            try {
                List<Map<String, Object>> listMap = dataService.findByVolLabel(volLabel);
                writeInfo(burnMachine, volLabel, listMap);
                masterMidasBurn(burnMachine, volLabel, discType);
                burnService.updateState(volLabel, BurnState.BURNNING.getKey(), "", 0);
            } catch (Exception e) {
                if (StringUtils.isEmpty(errInfo)) {
                    errInfo = e.getMessage();
                }
                logger.error("发起刻录信息失败, 删除刻录机器{" + burnMachine + "}的内容：{" + volLabel + "}", e);
                burnService.updateState(volLabel, BurnState.BURN_INIT_FAILD.getKey(), errInfo, 0);
                delete(burnMachine, volLabel);
            }
        } else {
            boolean isDel = delete(burnMachine, volLabel);
            logger.info("上传文件失败, 检查上传的文件内容, 删除设备{}, 上的卷标内容: {}, 删除结果为： {}", burnMachine, volLabel, isDel);
            burnService.updateState(volLabel, BurnState.UPLOAD_FAILD.getKey(), errInfo, 0);
        }
       logger.info("运行时长为: " + (System.currentTimeMillis() - st));
    }

    /**
     * 启动刻录
     * 
     * @param burnMachine
     *            刻录机器
     * @param volLabel
     *            卷标号
     * @param discType
     *            盘类型
     * @throws Exception
     */
    @Override
    public void masterMidasBurn(String burnMachine, String volLabel, String discType) throws Exception {
        if(burnService.isCancel(volLabel)) {
            throw new ServiceException(ErrorConstant.CODE2000, "用户已经取消刻录任务");
        }
        List<Map<String, Object>> machineInfos = commonService.listSystemParameters(burnMachine);
        if (null == machineInfos || machineInfos.isEmpty()) {
            logger.info("系统参数表中 服务器： {} 未找到相关配置", burnMachine);
            throw new ServiceException(ErrorConstant.CODE2000, "刻录机器未找到");
        }
        String srcpath = ObjectUtils.toString(machineInfos.get(0).get("sp_value4"));
        fileOper.createFile(srcpath, volLabel, "MIDASBURN.txt", discType + "\n" + volLabel);
    }
    
    private void writeInfo(String burnMachine, String volLabel, List<Map<String, Object>> listMap) throws Exception {
        if(burnService.isCancel(volLabel)) {
            throw new ServiceException(ErrorConstant.CODE2000, "用户已经取消刻录任务");
        }
        List<Map<String, Object>> machineInfos = commonService.listSystemParameters(burnMachine);
        if (null == machineInfos || machineInfos.isEmpty()) {
            logger.info("系统参数表中 服务器： {} 未找到相关配置", burnMachine);
            throw new ServiceException(ErrorConstant.CODE2000, "刻录机器未找到");
        }
        
        String srcpath = ObjectUtils.toString(machineInfos.get(0).get("sp_value4"));
        fileOper.createFile(srcpath, volLabel, "infomation.txt", JSONUtils.toJSONString(listMap));
    }
    
	// TODO sullivan
    @Override
	public boolean diskSpacecheck(String volLabel, String exportpath) throws ServiceException {
		String burnsizeStr = "";
		Map<String, Object> map = burnService.getBurnByVolLabel(volLabel);
		burnsizeStr = map.get("burn_size") + "";

		File file = new File(exportpath);
		if (!file.exists()) {
			throw new ServiceException(ErrorConstant.CODE4000, "导出目录[" + exportpath + "] 不存在,请输入正确路径");

		}
		Long freeSpace = file.getFreeSpace();
		Long fileSize = Long.parseLong(burnsizeStr);
		Long dirSize = freeSpace + 50000;

		if (dirSize < fileSize) {
			logger.info("导出目录[" + exportpath + "]  空间大小k[" + dirSize + "]  导出文件大小k:[" + fileSize + "] 导出空间不足无法导出!");
//			throw new ServiceException(ErrorConstant.CODE4000, "导出目录[" + exportpath + "] 空间大小k[" + dirSize / 1024
//					+ "]M  导出文件大小:[" + fileSize / 1024 + "] M导出空间不足无法导出!");
			return false;
		}

		return true;

	}
    
 
    
    /**
     * 插入刻录任务数据
     */
    public boolean masterMergeTaskSave(String volLabel, String exportpath) throws ServiceException {
		boolean isSucc = true;

		if (!diskSpacecheck(volLabel, exportpath)) {

			throw new ServiceException("空间不足,抱歉不能导出,请选择其它目录导出!");
		}

		Map<String, Object> exportMap = new HashMap<String, Object>();
		try {

			File exportFile = new File(exportpath);
			if (!exportFile.isDirectory()) {
				throw new ServiceException(ErrorConstant.CODE3000, "合并文件失败， 请输入一个可以访问的目录！");
			}
			burnService.checkMerge(volLabel);
            exportMap.put("volume_label", volLabel);
            exportMap.put("number_success", 0);
            exportMap.put("export_state", "0");
            exportMap.put("export_desc", "下载数据");
            exportMap.put("export_path", exportpath);
            // exportMap.put("c_user", null);
            burnService.insertExportRecord(exportMap);
            logger.info("eid={}",exportMap.get("eid"));
            
            Map<String, Object> paraMap=new HashMap<String, Object>();
            
            paraMap.put("volume_label", volLabel);
            paraMap.put("type", 1);
            
            List<Map<String, Object>> mergelist= standingbookService.getStandingbook(paraMap);
            for(Map<String, Object> o:mergelist){
                
                Map<String, Object> standingMap = new HashMap<String, Object>();
                standingMap.put("eid", exportMap.get("eid"));
                standingMap.put("volume_label", volLabel);
                standingMap.put("data_type", o.get("data_type"));
                standingMap.put("work_area",  o.get("work_area"));
                standingMap.put("construction_unit", o.get("construction_unit"));
                standingMap.put("construction_year", o.get("construction_year"));
                standingMap.put("data_quantity", o.get("data_quantity"));
                standingMap.put("burn_count", o.get("burn_count"));
                standingMap.put("create_time", new Date());
                standingMap.put("type", "2");
                standingbookService.insert(standingMap);
            }    
           
        } catch (ServiceException e) {
            throw e;
        }
        return isSucc;

    }
    

    @Override
    public boolean masterMerge(String volLabel, String exportpath) throws ServiceException {
        boolean isSucc = true;
        Map<String, Object> exportMap = new HashMap<String, Object>();
        try {
        	String dumpFile=null;
            List<Map<String, Object>> list = burnService.listPosition(volLabel);
            List<FutureTask<Boolean>> futureTaskList = new ArrayList<FutureTask<Boolean>>();
            for (Map<String, Object> map : list) {
                int pos = 0;
                try {
                    int magNo = Integer.parseInt(ObjectUtils.toString(map.get("position")));
                    int disc_position = Integer.parseInt(ObjectUtils.toString(map.get("disc_position")));
                    pos = (magNo - 1) * 50 + ((disc_position - 1) % 50 + 1);
                    //TODO 导出重试功能，修改导出状态为0，扫描进入此方法重新下载，发现已经下载的文件就跳过
//                    String server=map.get("server")+"";
//                    Map<String, Object> machineInfo = commonService.getSystemParameters(server);                  
//                    String filename = String.format("%04d", pos) + ".iso";                   
//                    String workdir=machineInfo.get("sp_value3")+"";
//                    dumpFile=workdir+File.separator+filename;
//                	File file = new File(dumpFile);
//        			if (file.exists()) {
//        				logger.info("跳过下载文件，已存在："+dumpFile);
//        				continue;
//        			}
                    Thread.sleep(1*1000L);
                } catch (Exception e) {
                    throw new ServiceException(ErrorConstant.CODE3000, "位置计算失败, 或者未获取位置信息", e);
                }
                Callable<Boolean> call = new MergeDump(ObjectUtils.toString(map.get("server")), pos,dumpFile);
                FutureTask<Boolean> future = new FutureTask<Boolean>(call);
                new Thread(future).start();
                futureTaskList.add(future);
              
            }
            for (FutureTask<Boolean> futureTask : futureTaskList) {
                boolean bool = false;
                try {
                    bool = futureTask.get();
                } catch (InterruptedException e) {
                    throw new ServiceException(ErrorConstant.CODE3000, "盘库导出失败", e);
                } catch (ExecutionException e) {
                    throw new ServiceException(ErrorConstant.CODE3000, "盘库导出失败, 线程执行失败", e);
                } finally {
                    if (!bool) {
                    	exportMap.put("volume_label", volLabel);
                        exportMap.put("export_state", ExportState.EXPORT_FAILD.getKey());
                        exportMap.put("export_desc", "下载失败");
                        burnService.updateExportRecord(exportMap);
                    }
                }
            }
        } catch (ServiceException e) {
            throw e;
        }
        return isSucc;

    }
   
   

    @Override
    public void masterMergeNotify(String volLabel) {
        boolean isSucc = false;
        String eid = null;
        String export_path = null;
        
      

        List<Map<String, Object>> list = burnService.listExportRecord(volLabel, ExportState.EXPORTTING.getKey(),null);

        if (list != null && !list.isEmpty()) {
            isSucc = true;
        }
        // 所有的刻录机器
        List<Map<String, Object>> machine = commonService.getAllMachine();

        List<File> fileList = new ArrayList<File>();

        for (Map<String, Object> map : list) {
            
            eid = ObjectUtils.toString(map.get("eid"));
            export_path = ObjectUtils.toString(map.get("export_path"));
            
            try {
                String electronic_tag = ObjectUtils.toString(map.get("electronic_tag"));
                Map<String, Object> tagMap = null;
                try {
                    tagMap = commonService.getTagposition(electronic_tag);
                } catch(Exception e) {
                    int count = 0;
                    while(count < 3) {
                        try {
                            Thread.sleep(10 * 1000L);
                            tagMap = commonService.getTagposition(electronic_tag);
                        } catch (Exception ex) {
                            logger.error("查询电子标签{}位置失败, 重试次数为: {}", electronic_tag, count + 1);
                        }
                        count ++;
                    }
                }
                
                if(null == tagMap || tagMap.isEmpty() || !tagMap.containsKey("position")) {
                    throw new ServiceException(ErrorConstant.CODE3000, "导出失败");
                }
                
                // 计算文件名
                int magNo = Integer.parseInt(ObjectUtils.toString(tagMap.get("position")));
                int diskPos = Integer.parseInt(ObjectUtils.toString(map.get("disc_position")));
                int cur = ((diskPos - 1) % 50 + 1) + (magNo - 1) * 50;
                String filename = String.format("%04d", cur) + ".iso";

                File f = getFilePath(machine, filename);
                if (null == f) {
                    isSucc = false;
                    break;
                } else {
                    fileList.add(f);
                }
                logger.info("盘槽位置： {}, 盘位置： {}, 当前位置: {}, 文件名列表： [{}]", magNo, diskPos, cur, fileList);
            } catch (Exception e) {
                logger.error("查找导出包的时候失败," + map, e);
                isSucc = false;
                map.put("eid", eid);
                map.put("export_state", ExportState.EXPORT_FAILD.getKey());
                map.put("export_desc", "连接服务器失败");
                map.put("number_success", fileList.size());
                burnService.updateExportRecord(map);
            }
        }

        if (isSucc) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("eid", eid);
            map.put("export_state", ExportState.EXPORT_SUCCESS.getKey());
            map.put("export_desc", "下载成功");
            map.put("number_success", fileList.size());
            burnService.updateExportRecord(map);
            ThreadPoolS.execute(new MegerCommand(eid, volLabel, export_path, fileList));

        }
    }

    private File getFilePath(List<Map<String, Object>> list, String filename) {
        for (Map<String, Object> map : list) {
            String filepath = map.get("sp_value3") + "/" + filename;
            File f = new File(filepath);
            logger.info("验证文件{} 是否存在： {}", f.getAbsolutePath(), f.exists());
            if (f.exists()) {
                return f;
            }
        }
        return null;
    }
    
   

    /**
     * 运行合并指令
     * 
     * @author arron
     *
     */
    class MegerCommand implements Runnable {

        private String     eid;
        private String     volLabel;
        private String     exportpath;
        private List<File> filelist;

        public MegerCommand(String eid, String volLabel, String exportpath, List<File> filelist) {
            this.eid = eid;
            this.volLabel = volLabel;
            this.exportpath = exportpath;
            this.filelist = filelist;
        }

        @Override
        public void run() {
            logger.info("开始运行合并指令， 卷标号： {}, 输出路径： {}, 文件数量： {}", volLabel, exportpath, filelist.size());
            boolean isMegerOk = RunCommand.merge(volLabel, exportpath, filelist.size());
            try {
				Thread.sleep(5*1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            Map<String, Object> map = new HashMap<String, Object>();
            if (isMegerOk) {
                map.put("export_state", ExportState.MEGE_SUCCESS.getKey());
                map.put("export_desc", "导出成功");
                map.put("eid", eid);
                burnService.updateExportRecord(map);               
                //Map<String, Object> burnMap =burnService.getBurnByVolLabel(volLabel);
                Map<String, Object> standingMap = new HashMap<String, Object>();
                standingMap.put("eid", eid);
                standingMap.put("volume_label", volLabel);
                standingMap.put("states", "1");
                // 修改为最后一个刻录完成的时间
                //standingMap.put("data_quantity", burnMap==null?"":burnMap.get("burn_size"));
                standingMap.put("update_time", new Date());
                standingMap.put("type", 2);
                standingbookService.update(standingMap);
                for (File f : filelist) {
                    logger.info("删除文件： {}", f.getAbsolutePath());
                    f.delete();
                }
            } else {
                map.put("export_state", ExportState.MEGE_FAILD.getKey());
                map.put("export_desc", "导出失败");
                map.put("eid", eid);
                burnService.updateExportRecord(map);
            }

        }
    }

    public static void main(String[] args) {
        
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("a", "a");
        map.put("b", "b");
        map.put("c", "c");
        map.put("d", "d");
        
        list.add(map);
        
        System.out.println(JSONUtils.toJSONString(list));
        

    }

    @Override
    public void masterNotify(Map<String, Object> map) {

        String burn_time = ObjectUtils.toString(map.get("burn_time"));
        String volLabel = ObjectUtils.toString(map.get("volume_label"));
        String burnMachine = ObjectUtils.toString(map.get("burning_machine"));

        int i = DateUtil.getIntervalDays(DateUtil.StringToDate(burn_time, "yyyy-MM-dd"), new Date());

        if (i >= 7) {
            burnService.updateState(volLabel, BurnState.BURN_ERROR.getKey(), "超过3天未刻录完成,自动失败", 0);
            logger.info("刻录时间已超过三天， 手动修改为已失败, 唯一号：{}, 刻录机器： {}", volLabel, burnMachine);
            return;
        }
        try {
            BurnProgress bp = null;
            try {
                bp = commonService.getBurnStatus(burnMachine, volLabel);
            } catch (Exception e) {
                int count = 0;
                while(count < 3) {
                    try {
                        Thread.sleep(10 * 1000L);
                        bp = commonService.getBurnStatus(burnMachine, volLabel);
                    } catch (Exception ex) {
                        logger.error("查询结果失败, 查询次数为: " + (count + 1), e);
                    }
                    count ++;
                }
            }
            
            logger.info("获取结果内容： {}", bp);
            
            if (null == bp || !bp.isSuccess()) {
                logger.info("未找到刻录的信息或失败, 稍后再试");
//                burnService.updateState(volLabel, BurnState.BURN_ERROR.getKey(), "刻录已失败，刻录机器不能连接", 0);
                return;
            }

            int number = bp.getSplitsNubmer();
            List<Map<String, String>> warningList = bp.getWarnings();
            List<Map<String, String>> ends = bp.getEnds();
            List<Map<String, String>> infos = bp.getInfos();

            if(number != 0) {
                burnService.updateState(volLabel, null, "获取刻录数量", number);
            }
            
            if (ends != null && !ends.isEmpty()) {
                // insert burn detial
                for (Map<String, String> detail : ends) {
                    burnService.insertBurnDetail(volLabel, detail.get("rfid"), detail.get("position"),
                            detail.get("isofile"), detail.get("time"));
                }
                if (number == ends.size()) {
                    // 成功全部刻录成功，更新记录为刻录成功
                    burnService.updateState(volLabel, BurnState.BURN_SUCCESS.getKey(), "成功", number);
                    
                   Map<String, Object> burnMap =burnService.getBurnByVolLabel(volLabel);
                    
                    Map<String, Object> standingMap = new HashMap<String, Object>();
                    standingMap.put("volume_label", volLabel);
                    standingMap.put("burn_count", number);
                    standingMap.put("states", "1");
                    if (burnMap!=null&&!"".equals(burnMap.get("burn_size"))) {			
                    standingMap.put("data_quantity", burnMap.get("burn_size"));
                	}
                    // 修改为最后一个刻录完成的时间
                    standingMap.put("update_time", ends.get(number-1).get("time"));
                    standingMap.put("type", 1);
                    standingbookService.update(standingMap);
                    return;
                }
            }
            
      
            if (null != infos && !infos.isEmpty()) {
                // 
                ArrayList<String> message=formatMsg(infos);
                String s1="";
                String msg ="";
                for ( Object info :message ) {
                    s1=info.toString();
                    msg+=s1+",";
                }
          
//                logger.info("message={}",msg);
                 burnService.updateState(volLabel, msg);
            }
            
            if (null != warningList && !warningList.isEmpty()) {
                // 有错误， 未正常执行， 把原因找到放入到刻录任务中, 更新刻录状态为 刻录暂停, 更新描述为原因
                Set<String> s = new HashSet<String>();

                for (Map<String, String> wars : warningList) {
                    s.add(getErrInfo(wars.get("errorCode")));
                }
                String msg = StringUtils.join(s, ",");
                burnService.updateState(volLabel, BurnState.BURN_WAIT.getKey(), msg, number);
            }
           
        } catch (Exception ex) {
            logger.error("刻录结果查询不成功：" + map, ex);
        }
    }

    public String getErrInfo(String code) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("sp_code", SysConstant.ERROR_CODE);
        param.put("sp_value1", code);
        Map<String, Object> map = commonService.getSystemParameters(param);
        String errorInfo = null;
        if (null == map || map.isEmpty()) {
            errorInfo = "未知错误";
        } else {
            errorInfo = map.get("sp_value2").toString();
        }
        logger.info("通过错误码：{} 获取系统参数表中的内容为： {}", code, errorInfo);
        return errorInfo;
    }

    public ArrayList<String> formatMsg( List<Map<String, String>> infos){
         ArrayList<String> messagelist=new ArrayList<String>();
        for (Map<String, String> wars : infos) {
           String message= wars.get("info");
           String newMessage="";
           if(message.contains("Start Split")){
               newMessage="1 分解任务:"+message.substring(message.lastIndexOf("/"),message.length()-1);
           }else if(message.contains("Start mkiso")){
               
               newMessage="2 制作ISO:"+message.substring(message.lastIndexOf("/"),message.length()-1);
           }else   if(message.contains("End mkiso")){
               
               newMessage="3 制作ISO结束:"+message.substring(message.lastIndexOf("/"),message.length()-1);
           }else  if(message.contains("Start burn")){
               
               newMessage="4 开始刻录:"+message.substring(message.lastIndexOf("/"),message.length()-1);
           }
           messagelist.add(newMessage);
          logger.info("List.size{}", messagelist.size());
           Collections.sort(messagelist);
           if(messagelist.size()>200){
               messagelist.remove(0);
           }
           
        }
      
        
        return messagelist;}
    
    class Burn implements Runnable {
        private String volLabel;

        public Burn(String volLabel) {
            this.volLabel = volLabel;
        }

        @Override
        public void run() {
            try {
                // 线程等待5秒， 等上一个任务的事务提交
                Thread.sleep(5 * 1000L);
            } catch (InterruptedException e) {
            }
            masterBurn(volLabel);
        }
    }

    class MergeDump implements Callable<Boolean> {
        private String server;
        private int    position;
        private String dumpFile;

        public MergeDump(String server, int position,String dumpFile) {
            this.server = server;
            this.position = position;
            this.dumpFile=dumpFile;
        }

        @Override
        public Boolean call() throws ServiceException {
            logger.info("调用机器{} 导出数据， 发送导出位置为： {}", server, position);
           int num=4;
            return commonService.executeDUMPMEDIA(server, String.valueOf(position));
        }
        
//        public boolean sendDumpCmd(int num)
//        {
//        	 boolean n=commonService.executeDUMPMEDIA(server, String.valueOf(position));
//        	 if (num >3) {
//				return false;
//			}
//        	 try {
// 			if (!StringUtils.isEmpty(dumpFile)) {
// 				File file = new File(dumpFile);
// 				if (!file.exists()) {
// 					num++;
// 					sendDumpCmd(num);
// 				}
// 			} 			
//				Thread.sleep(5*1000L);
//			} catch (Exception e) {
//				logger.info("dumpMedia 线程下载重试失败"+e);
//				e.printStackTrace();
//			}
// 			return n;
//        }
        
    }

    class Downfile implements Callable<Boolean> {

        private String burnMachine;
        private String volLabel;
        private String dataSource;
        private String filepath;
        private int    seqNumber;

        public Downfile(String burnMachine, String volLabel, String dataSource, String filepath, int seqNumber) {
            this.burnMachine = burnMachine;
            this.volLabel = volLabel;
            this.dataSource = dataSource;
            this.filepath = filepath;
            this.seqNumber = seqNumber;
        }

        @Override
        public Boolean call() throws ServiceException {
            try {
                logger.info("运行下载数据， burnMachine : {}, dataSource: {}, filepath: {}, seqNumber : {}", burnMachine,
                        dataSource, filepath, seqNumber);
                List<Map<String, Object>> machineInfos = commonService.listSystemParameters(burnMachine);
                if (null == machineInfos || machineInfos.isEmpty()) {
                    logger.info("系统参数表中 服务器： {} 未找到相关配置", burnMachine);
                    return false;
                }
                // 如果采用多网口拷贝数据， 则配置多个ip信息，根据线程的数量取机器的余获取一个
                Map<String, Object> machine = machineInfos.get(0);
                if(burnService.isCancel(volLabel)) {
                    throw new ServiceException(ErrorConstant.CODE2000, "用户取消刻录");
                }
                String descpath = ObjectUtils.toString(machine.get("sp_value4"));
                String path = null;
                if (DataSource.HARD_DISK.getKey().equals(dataSource)) {
                    // 移动硬盘
                    path = getLocalPath(volLabel);
                } else if (DataSource.NETWORK_SHARING.getKey().equals(dataSource)) {
                    // 网络共享
                    Map<String, Object> map = commonService.getSystemParameters(SysConstant.SHARED_NETWORK_ADDRESS);
                    path = ObjectUtils.toString(map.get("sp_value4"));
                    // fileOper.copy(srcpath, descpath, filepath, volLabel);
                }
                BigDecimal size = fileOper.copy(path, descpath, filepath, volLabel);
                if (burnService != null) {
                    burnService.updateSize(volLabel, size);
                }
                logger.info("copy文件或者目录： {}， 大小为： {}", path, size);
            } catch (ServiceException e) {
                logger.error("下载数据失败ServiceException", e);
            } catch (Exception e) {
                logger.error("下载数据失败， 下载到机器： " + burnMachine + ", 文件路径为: " + filepath, e);
            }
            return true;
        }
    }

    /**
     * 获取本地U盘路径
     * 
     * @return
     */
    public String getLocalPath(String volLabel){
        
        Map<String, Object> map=burnService.getBurnByVolLabel(volLabel);
       String local_path= (String) map.get("local_path");
       String retval="";
        if(StringTools.isEmpty(local_path)){
            retval=SysConstant.HARDDISCK;
        }else{
            retval=local_path;
        }
        logger.info("文件 目录： {}", retval);
        return retval;
    }

    /**
     * 根据卷标号删除指定机器的内容
     * 
     * @param burnMachine
     * @param volLabel
     * @throws Exception
     */
    private boolean delete(String burnMachine, String volLabel) {
        try {
            List<Map<String, Object>> machineInfos = commonService.listSystemParameters(burnMachine);
            if (null == machineInfos || machineInfos.isEmpty()) {
                logger.info("系统参数表中 服务器： {} 未找到相关配置", burnMachine);
                throw new ServiceException(ErrorConstant.CODE2000, "刻录机器未找到");
            }
            String srcpath = ObjectUtils.toString(machineInfos.get(0).get("sp_value4"));
            fileOper.delete(srcpath, volLabel);
            return true;
        } catch (Exception e) {
            logger.error("删除不完整刻录内容失败", e);
            return false;
        }
    }

    /**
     * 检查盘的数量是否满足
     * 
     * @param bd
     * @param discType
     * @param effQuan
     * @return
     */
    private boolean checkDisc(BigDecimal bd, String discType, Object effQuan) {
        boolean bool = false;
        try {
            // 系统参数
            Map<String, Object> syspMap = new HashMap<String, Object>();
            syspMap.put("sp_value3", discType);
            syspMap.put("sp_code", SysConstant.DISC_TYPE);
            syspMap.put("sp_state", BooleanUtils.toInteger(true));
            syspMap = commonService.getSystemParameters(syspMap);
            if (null == syspMap || syspMap.isEmpty()) {
                logger.info("未定义参数sp_value1: {}, key: {}", discType, SysConstant.DISC_TYPE);
                return true;
            }
            if (!syspMap.containsKey("sp_value4") || !syspMap.containsKey("sp_value5")) {
                logger.info("未定义参数sp_value1: {}, key: {}", discType, SysConstant.DISC_TYPE);
                return true;
            }
            BigDecimal discSize = new BigDecimal(ObjectUtils.toString(syspMap.get("sp_value4"))); // 盘的大小
            BigDecimal moreQuan = new BigDecimal(ObjectUtils.toString(syspMap.get("sp_value5"))); // 余量
            if (StringTools.isNotEmpty(effQuan)) {
                BigDecimal eff = new BigDecimal(ObjectUtils.toString(effQuan));
                bool = checkDiscNumber(bd, discSize, eff, moreQuan);
            } else {
                bool = true;
            }
        } catch (Exception e) {
            logger.error("验证有错误， 直接跳过，后续再控制", e);
            bool = true;
        }
        return bool;
    }

    /**
     * 设置不同的实现类
     * 
     * @param dataType
     */
    public void setDataService(String dataType) {
        DataType dataTypeEnum = (DataType) EnumUtils.convertEnum(DataType.class, dataType);
        this.dataService = SpringContextHelper.getBean(dataTypeEnum.getBean(), DataService.class);
    }

}
