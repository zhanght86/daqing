package com.midas.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.midas.constant.ErrorConstant;
import com.midas.constant.ServiceConstant;
import com.midas.constant.SysConstant;
import com.midas.context.SpringContextHelper;
import com.midas.dao.BurnDao;
import com.midas.enums.BurnState;
import com.midas.enums.DataType;
import com.midas.enums.ExportState;
import com.midas.exception.ServiceException;
import com.midas.service.BurnService;
import com.midas.service.CommonService;
import com.midas.service.DataService;
import com.midas.uitls.date.DateStyle;
import com.midas.uitls.date.DateUtil;
import com.midas.uitls.runtime.RunCommand;
import com.midas.uitls.tools.CommonsUtils;
import com.midas.uitls.tools.EnumUtils;
import com.midas.uitls.tools.StringTools;

@Service
public class BurnServiceImpl implements BurnService {

    private Logger logger = LoggerFactory.getLogger(BurnServiceImpl.class);

    @Autowired
    private CommonService commonService;
    @Autowired
    private BurnDao       burnDao;
    private DataService   dataService;

	

    @Override
    public Map<String, Object> getFreeBurn() throws ServiceException {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        // 配置表中的内容
        List<Map<String, Object>> discLibList = commonService.listSystemParameters(SysConstant.DISC_LIBRARY);
        if (null == discLibList || discLibList.isEmpty()) {
            logger.error("没有配置可用的光盘库");
            throw new ServiceException(ErrorConstant.CODE3000, "没有可用盘库");
        }
        for (Map<String, Object> disc : discLibList) {

            // 检查网络是否正常
            boolean bool = commonService.isConnect(disc);
            logger.info("盘库机器： 【{}】是否连接: {} ", disc, bool);
            if (!bool) {
                logger.error("服务器：" + disc + ", 未成功连接");
                continue;
            }
            // 查看是否有刻录任务
            Map<String, Object> burnParaMap = new HashMap<String, Object>();

            List<String> burnState = new ArrayList<String>();
            burnState.add(BurnState.BURNNING.getKey());
            burnState.add(BurnState.INPUT_SUCCESS.getKey());
            burnState.add(BurnState.UPLOAD_INIT.getKey());
            burnState.add(BurnState.UPLOAD_SUCCESS.getKey());

            burnParaMap.put("burning_state", StringUtils.join(burnState, ","));
            burnParaMap.put("burning_machine", disc.get("sp_value1"));
            List<Map<String, Object>> burningList = burnDao.list(burnParaMap);
            logger.info("盘库【{}】是否还有刻录中的任务：{}", disc, (null == burningList || burningList.isEmpty()));
            if (null == burningList || burningList.isEmpty()) {
                // 没有刻录任务情况下， 检查盘库机器是否繁忙， false为不忙
                boolean isBusy = commonService.isBusy(ObjectUtils.toString(disc.get("sp_value1")));
                logger.info("检查盘库机器是否忙碌: {}", isBusy);
                if (!isBusy) {
                    int effQuan = commonService.effectiveQuantity(ObjectUtils.toString(disc.get("sp_value1")));
                    disc.put(ServiceConstant.EFF_QUAN, effQuan);
                    resultList.add(disc);
                }
            }
        }

        if (resultList.isEmpty()) {
            logger.info("没有可用的光盘库, 光盘都在运行");
            boolean ischeck = true;
            try {
                String env = CommonsUtils.getPropertiesValue(SysConstant.ENVIRONMENTAL);
                if ("DEV".equals(env)) {
                    logger.warn("该环境为开发环境， 不验证真正数据内容");
                    ischeck = false;
                    resultList = discLibList;
                }
            } catch (IOException e) {
            }
            if (ischeck) {
                throw new ServiceException(ErrorConstant.CODE3000, "没有可用盘库, 盘库都是繁忙状态");
            }
        }

        // 大于2个服务器的时候， 剔除最后刻录的机器
        if (resultList.size() > 1) {
            Map<String, Object> removeMap = null;
            Map<String, Object> paraMap = new HashMap<String, Object>();
            Map<String, Object> burnOne = burnDao.getLast(paraMap);
            if (null != burnOne) {
                for (Map<String, Object> disc : resultList) {
                    if (disc.get("sp_value1").equals(burnOne.get("burning_machine"))) {
                        removeMap = disc;
                    }
                }
                if (null != removeMap) {
                    resultList.remove(removeMap);
                }
            }
        }
        return resultList.get(0);
    }

    @Override
    public String insertBurn(String taskName, String discType, String dataSource, String burnMachine, String dataType,String exportPath) {
        String valumeLabel = commonService.seqUniqueNextVal(DateUtil.getLastDay(DateStyle.YYYYMMDD), dataType);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("volume_label", valumeLabel);
        map.put("task_name", taskName);
        map.put("disc_type", discType);
        map.put("data_source", dataSource);
        map.put("burning_machine", burnMachine);
        map.put("burning_state", BurnState.INPUT_SUCCESS.getKey());
        map.put("burn_time", DateUtil.getLastDay(DateStyle.YYYY_MM_DD_HH_MM_SS));
        map.put("data_type", dataType);
        map.put("local_path", exportPath);
        
        int result = burnDao.insert(map);
        if (result > 0) {
            logger.info("添加刻录任务成功{}, 准备开始下载数据", map);
            return valumeLabel;
        }
        throw new ServiceException(ErrorConstant.CODE2000, "添加刻录任务失败");
    }

    @Override
    public Map<String, Object> getBurnByVolLabel(String volLabel) {
        if (null == volLabel || "".equals(volLabel)) {
            throw new ServiceException(ErrorConstant.CODE2000, "卷标号不能为空");
        }
        return burnDao.getBurnByVolLabel(volLabel);
    }

    @Override
    public List<Map<String, Object>> list(Map<String, Object> map) {
        return burnDao.list(map);
    }

    @Override
    public int insert(Map<String, Object> map) {
        return burnDao.insert(map);
    }

    @Override
    public synchronized boolean updateState(String volLabel, String state, String desc, int discNumber) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("volume_label", volLabel);
        map.put("burning_state", state);
        map.put("burn_desc", desc);
        map.put("disc_number", discNumber);
        
        return burnDao.updateState(map);
    }
    @Override
    public synchronized boolean updateState(String volLabel, String burn_progress) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("volume_label", volLabel);
//        map.put("burning_state", state);
//        map.put("burn_desc", desc);
//        map.put("disc_number", discNumber);
        map.put("burn_progress", burn_progress);
        return burnDao.updateState(map);
    }
   
    @Override
    public PageInfo<Map<String, Object>> list(Map<String, Object> map, Page<Map<String, Object>> page) {
        return burnDao.list(map, page);
    }

    @Override
    public List<Map<String, Object>> listPosition(String volLabel) {
        List<Map<String, Object>> list = burnDao.listPosition(volLabel);
        Map<String, Map<String, Object>> mapTags = new HashMap<String, Map<String, Object>>();
        for (Map<String, Object> map : list) {
            String tag = ObjectUtils.toString(map.get("electronic_tag"));
            if (tag != null && !"".equals(tag)) {
                mapTags.put(tag, null);
            }
        }
        Set<String> set = mapTags.keySet();
        for (String s : set) {
            try {
                // TODO 获取电子标签在什么位置
                Map<String, Object> tagPos = commonService.getTagposition(s);
                mapTags.put(s, tagPos);
            } catch (Exception e) {
                logger.error("未获取到电子标签:" + s + ", 所在的位置", e);
            }
        }

        for (Map<String, Object> map : list) {
            String tag = ObjectUtils.toString(map.get("electronic_tag"));
            Map<String, Object> tags = mapTags.get(tag);
            if (null != tags && !tags.isEmpty()) {
                map.putAll(tags);
            }
        }

        return list;
    }

    @Override
    public boolean checkMerge(String volLabel) {
        List<Map<String, Object>> listExport = burnDao.listExportRecord(null, ExportState.EXPORTTING.getKey(),null);
        if (null != listExport && !listExport.isEmpty()) {
            logger.info("有正在导出的任务, 不能进行导出数据");
            throw new ServiceException(ErrorConstant.CODE2000, "有正在导出的任务, 现在不能导出数据, 请稍后再试!!!");
        }
        List<Map<String, Object>> listPos = listPosition(volLabel);

        List<String> listServers = new ArrayList<String>();

        for (Map<String, Object> map : listPos) {
            // 如果sp_code为空的情况下， 说明为找到指定位置
            Object spCode = map.get("spCode");
            if (null == spCode || "".equals(spCode)) {
                throw new ServiceException(ErrorConstant.CODE2000,
                        "电子标签: " + map.get("electronic_tag") + " 所在的位置,没有找到, 无法合并导出");
            }
            if (SysConstant.OFF_LINE_CABINET.equals(spCode)) {
                throw new ServiceException(ErrorConstant.CODE2000,
                        "文件: [" + map.get("iso_file") + "]在"+map.get("server")+"("+map.get("serverName")+")的第"+map.get("position")+"号盘槽中 ,电子标签: " + map.get("electronic_tag") + "， 需要存放刻录机器才能进行合并导出");
            }
            listServers.add(ObjectUtils.toString(map.get("server")));
        }
        // 都在盘库设备中的时候, 检查指定机器是否繁忙
        for (String server : listServers) {
            boolean isbusy = commonService.isBusy(ObjectUtils.toString(server));
            if (isbusy) {
                logger.debug("服务器{}, 正在被使用， 不能进行合并");
                throw new ServiceException(ErrorConstant.CODE2000, "盘库设备:" + server + " 正在运行， 请空闲的时候在进行合并");
            }
        }
        return true;
    }

    @Override
    public boolean insertBurnDetail(String volLabel, String electronic_tag, String disc_position, String isoFilename,
            String time) {
        boolean isSuccess = false;
        List<Map<String, Object>> list = burnDao.getBurnDetailByCondition(volLabel, electronic_tag, disc_position);
        // 没有找到记录进行添加， 如果找到不添加
        if (null == list || list.isEmpty()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("volume_label", volLabel);
            map.put("electronic_tag", electronic_tag);
            map.put("disc_position", disc_position);
            map.put("iso_file", isoFilename);
            map.put("create_time", time);
            int i = burnDao.insertDetail(map);
            if (i > 0) {
                isSuccess = true;
            }
        }
        return isSuccess;
    }

    @Override
    public int insertExportRecord(Map<String, Object> map) {
        return burnDao.insertExportRecord(map);
    }

    public int insertExportFileRecord(Map<String, Object> map) {
        return burnDao.insertExportFileRecord(map);
    }
    @Override
    public List<Map<String, Object>> listExportTask(String param) {
        return burnDao.listExportTask(param);
    }

    @Override
    public synchronized int updateExportRecord(Map<String, Object> map) {
        return burnDao.updateExportRecord(map);
    }

    @Override
    public List<Map<String, Object>> listExportRecord(String volLabel, String state,String task_name) {
        return burnDao.listExportRecord(volLabel, state,task_name);
    }
    
    @Override
    public List<Map<String, Object>> listExportRecordCheck(String volLabel, String state,String task_name) {
        return burnDao.listExportRecordCheck(volLabel, state,task_name);
    }

    @Override
    public List<Map<String, Object>> listExportRecord(String volLabel,String task_name) {
        return burnDao.listExportRecord(volLabel,task_name);
    }

    @Override
    public void updateSize(String volLabel, BigDecimal size) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("volLabel", volLabel);
        map.put("size", size);
        burnDao.updateSize(map);
    }

    @Override
    public boolean isCancel(String volLabel) {
        Map<String, Object> map = burnDao.getBurnByVolLabel(volLabel);
        if (BurnState.CANCEL.getKey().equals(map.get("burning_state"))) {
            return true;
        }
        return false;
    }

    @Override
    public void delete(String volLabel, String dataType) {
        burnDao.delete(volLabel);
        burnDao.deleteDetail(volLabel);
        if(StringTools.isNotEmpty(dataType)) {
            setDataService(dataType);
            dataService.deleteByVolLabel(volLabel);
        }
    }
    
    
    
    /**
     * 设置不同的实现类
     * 
     * @param dataType
     */
    private void setDataService(String dataType) {
        DataType dataTypeEnum = (DataType) EnumUtils.convertEnum(DataType.class, dataType);
        this.dataService = SpringContextHelper.getBean(dataTypeEnum.getBean(), DataService.class);
    }

    @Override
    public void deleteExport(String eid) {
        // TODO Auto-generated method stub
        burnDao.deleteExport(eid);
    }

    
    
    
    //TODO sullivan
	@Override
	/**
	 * 
	 */
	public List<Map<String, Object>> listExportFileList( String keyWord) {
		 List<Map<String, Object>>  rsList=new ArrayList<>();
	
	     String searchResult=null;
		 List<Map<String, Object>> serverList=commonService.getAllMachine();
		 
		 
		for (Map<String, Object> macheInfo : serverList) {
//			Map<String, Object> macheInfo=commonService.getSystemParameters(row.get("sp_value1")+"");
			 String ip=macheInfo.get("sp_value1")+"";
			 String server=macheInfo.get("sp_code")+"";
			 searchResult=commonService.executeFindFile(server, keyWord);
			 if(StringUtils.isNotEmpty(searchResult))
			 {
			 String[] matchInfo = searchResult.split("\n");	
				for (int i = 0; i < matchInfo.length; i++) {
					Map<String, Object> rowMap = new HashMap<>();
					rowMap.put("filePath", matchInfo[i]);
					rowMap.put("ip",ip );
					rowMap.put("server",server );
					rsList.add(rowMap);
				

				}
			 }
			
		}

		
		
		
		
		return  rsList;
	}
	

	  @Override
	    public List<Map<String, Object>> listExportFileRecord(String volLabel, String state,String task_name) {
	        return burnDao.listExportFileRecord(volLabel, state,task_name);
	    }	
//update by sullivan 
	
//	public List<Map<String, Object>> matchVolLabel( String keyWord) {
//		
//		List<Map<String, Object>>  rsList=new ArrayList<>();
//		rsList=listPosition(keyWord);
//		String queryRs=commonService.getAllMachine();
//		String result="";
//		Gson g=new Gson();
//		Machine machine =g.fromJson(result, Machine.class);
//		List<Mag> maglist=machine.getMag();
//		for (Mag mag : maglist) {
//		 List <Slot> slotlist=	mag.getSlot();
//		   for (Slot slot : slotlist) {
//			   HashMap<String , Object> rowMap=new HashMap<>();
//			   String volabel=slot.getLabel();
//			   if(volabel.indexOf(keyWord)>0)
//			   {
//				   rowMap.put("MagNo", mag.getMagNo());   
//				   rowMap.put("Rfid", mag.getRfid());   
//				   rowMap.put("label", slot.getLabel());   
//				   rowMap.put("slot_status", slot.getSlot_status());   
//			
//			   }
//			   rsList.add(rowMap);
//		   }
//		}
//		
//		return rsList;
//		
//	}

	
	

	
	@Override

	public boolean CheckTaskAndRun(String soucePath) throws Exception{
	


		List<Map<String, Object>> rslist = listExportTask("");		
		if (null != rslist && rslist.size() > 0) {	
			
			for (Map<String, Object> taskinfo : rslist) {
				if(ExportState.EXPORTTING.getKey().equals(taskinfo.get("export_state")))
				return false;
			}
			Map<String, Object> paramMap=rslist.get(0);
			paramMap.put("export_state", ExportState.EXPORTTING.getKey());
			paramMap.put("update_time", new Date());			
			burnDao.updateExportFile(paramMap);
			RunTask(paramMap);
		}
       
		return true;
	}
	
	public boolean RunTask( Map<String, Object> paramMap)
	{
		Map exportInfo = commonService.getSystemParameters(SysConstant.EXPORT_ENV);
		 List<Map<String, Object>> serverList=commonService.getAllMachine();
		 String servers="";
		 for (Map<String, Object> machine : serverList) {
			 servers+=machine.get("sp_value1")+",";
			
		}
		 servers= servers.substring(0,servers.length()-1);
		String cmd=exportInfo.get("sp_value3")+"";
		String username=exportInfo.get("sp_value1")+"";
		String soucePath=paramMap.get("filelist")+"";
		String targetPath=paramMap.get("export_path")+"";
		String passwd=exportInfo.get("sp_value2")+"";

		try {
			
		
		
		int rsInt=RunCommand.execute(cmd,username,servers,soucePath,targetPath.trim(),passwd);
		if(-1!=rsInt)
		{
			paramMap.put("export_state", ExportState.EXPORT_SUCCESS.toString());
			
		}
		else {
			paramMap.put("export_state", ExportState.EXPORT_FAILD.toString());
		}
		burnDao.updateExportFile(paramMap);
		
		} catch (Exception e) {
			paramMap.put("export_state", ExportState.EXPORT_FAILD.toString());
			burnDao.updateExportFile(paramMap);
		}
		return true;
		
	}
	
	
	
	
    @Override
	public boolean savefileExportTask(String soucePath, String exportpath) throws ServiceException {
		boolean isSucc = true;
		Map<String, Object> exportMap = new HashMap<String, Object>();

//		File exportFile = new File(exportpath);
//		if (!exportFile.isDirectory()) {
//			throw new ServiceException(ErrorConstant.CODE3000, "合并文件失败， 请输入一个可以访问的目录！");
//		}
		
		exportMap.put("fileList", soucePath);
		exportMap.put("number_success", 0);
		exportMap.put("export_state", "0");
		exportMap.put("export_desc", "准备下载");
		exportMap.put("export_path", exportpath);
		// exportMap.put("c_user", null);
		insertExportFileRecord(exportMap);

		return true;

	}
    
    /**
     * 检查文件占用空间大小
     * @param fileSpace
     * @param exportPath
     * @return
     */
	public boolean checkDiskSpace(String fileSpace, String exportPath) {

		String diskSpace = RunCommand.executeResult("du -k ", exportPath);
		double fileSize = Double.parseDouble(fileSpace);
		double dirSize = Double.parseDouble(diskSpace) + 50000.0;// 富余50M
		if (fileSize < dirSize) {
			return true;
		}

		return false;
	}
  //TODO sullivan
	
    
	public static void main(String[] args) {
//		String aa = "/0022_000010000020101/2013年工程/检测2013-18越洋广场项目对轨道交通六号线大剧院站、千厮门大桥引桥及C、D匝道影响第三方监测（2013.10）-王新胜/检测2013-18越洋广场项目对轨道交通六号线大剧院站、千厮门大桥引桥及C、D匝道影响第三方监测报告及原始资料（2013年10月份）/爆破振动监测波形/1010B20.DOC /n"
//				+ "/0022_000010000020101/2013年工程/检测2013-18越洋广场项目对轨道交通六号线大剧院站、千厮门大桥引桥及C、D匝道影响第三方监测（2013.10）-王新胜/检测2013-18越洋广场项目对轨道交通六号线大剧院站、千厮门大桥引桥及C、D匝道影响第三方监测报告及原始资料（2013年10月份）/爆破振动监测波形/1011B19.DOC /n"
//				+ "/0022_000010000020101/2013年工程/检测2013-18越洋广场项目对轨道交通六号线大剧院站、千厮门大桥引桥及C、D匝道影响第三方监测（2013.10）-王新胜/检测2013-18越洋广场项目对轨道交通六号线大剧院站、千厮门大桥引桥及C、D匝道影响第三方监测报告及原始资料（2013年10月份）/爆破振动监测波形/1013B20.DOC /n";
//		String[] row = aa.split("/n");
//		List<Map<String, Object>> rslist = new ArrayList<>();
//		for (int i = 0; i < row.length; i++) {
//			Map<String, Object> rowMap = new HashMap<>();
//			rowMap.put("row", row[i]);
//			rslist.add(rowMap);
//			// String[] column =row[i].split("/");
//			// for (int j = 0; j < column.length; j++) {
//			// rowMap.put("content", arg1)
//			//
//			// }
//
//		}
	
	   String executeResult = RunCommand.executeResult("adfas");
	  RunCommand.execute("aa");

	}
	
	
	 

}
