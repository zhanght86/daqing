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

import javax.swing.text.html.parser.TagElement;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
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
import com.midas.uitls.FtpUtil;
import com.midas.uitls.date.DateStyle;
import com.midas.uitls.date.DateUtil;
import com.midas.uitls.runtime.RunCommand;
import com.midas.uitls.tools.CommonsUtils;
import com.midas.uitls.tools.EnumUtils;
import com.midas.uitls.tools.StringTools;
import com.midas.vo.FileVo;

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
    public List<Map<String, Object>> listPositionOffline(String volLabel) {
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
                Map<String, Object> tagPos = commonService.getTagpositionDirect(s);
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
 //TODO 原先检查任务导出的单任务检查,现在放开,有后台定时任务进行判断,不再此处判断
//        List<Map<String, Object>> listExport = burnDao.listExportRecord(null, ExportState.EXPORTTING.getKey(),null);
//        if (null != listExport && !listExport.isEmpty()) {
//            logger.info("有正在导出的任务, 不能进行导出数据");
//            throw new ServiceException(ErrorConstant.CODE2000, "有正在导出的任务, 现在不能导出数据, 请稍后再试!!!");
//        }
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
    public Map<String, Object> listExportRecordCheck(String volLabel, String state,String task_name) {
		List<Map<String, Object>> checklist = burnDao.listExportRecordCheck(volLabel, state, task_name);
		List<Map<String, Object>> serverList = commonService.getAllMachine();// 可用的下载服务器列表
		if (null != checklist && checklist.size() > 0) {
			for (Map<String, Object> map : checklist) {
				String export_state = map.get("export_state") + "";
				if (ExportState.EXPORTTING.getKey().equals(export_state))// 发现有状态为1的正在导出任务的服务器排除则不执行导出
				{
					List<Map<String, Object>> taglist = listPosition(map.get("volume_label") + "");
					for (Map<String, Object> tag : taglist) {
						String tempServer = tag.get("server") + "";
						for (Map<String, Object> server : serverList) {
							if (tempServer.equals(server.get("sp_code"))) {
								serverList.remove(server);
							}
						}

					}
				}
			}
			if (serverList.size() == 0)
				return null;
			else {

				for (Map<String, Object> map : checklist) {
					String export_state = map.get("export_state") + "";
					if ("0".equals(export_state))// 0为正准备下载的状态
					{
						List<Map<String, Object>> taglist = listPosition(map.get("volume_label") + "");
						for (Map<String, Object> tag : taglist) {
							String tempServer = tag.get("server") + "";
							for (Map<String, Object> server : serverList) {//可用服务器列表
								if (tempServer.equals(server.get("sp_code"))) {
									return map; // 得到可运行的后台任务task返回
								}
							}

						}

					}
				}
			}

		}

		return null;
    }

    @Override
    public List<Map<String, Object>> listExportRecord(String volLabel,String task_name) {
        return burnDao.listExportRecord(volLabel,task_name);
    }

    @Override
    public void updateSize(String volLabel, BigDecimal size) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("volLabel", volLabel);
        map.put("size", size.toBigInteger());
        burnDao.updateSize(map);
    }

    @Override
    public boolean isCancel(String volLabel) {
        Map<String, Object> map = burnDao.getBurnByVolLabel(volLabel);
        if(null==map)
        	return false;
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

    @Override
    public void deleteExportFile(String eid) {
        // TODO Auto-generated method stub
        burnDao.deleteExportFile(eid);
    }
    
    
    
    //TODO sullivan
	
	/**
	 * 在线文件导出列表
	 */
    @Override
	public List<Map<String, Object>> listExportFileList(String fileName) {
		List<Map<String, Object>> rsList = new ArrayList<>();

		String searchResult = null;
		List<Map<String, Object>> serverList = commonService.getAllMachine();

		for (Map<String, Object> macheInfo : serverList) {
			// Map<String, Object>
			// macheInfo=commonService.getSystemParameters(row.get("sp_value1")+"");
			String ip = macheInfo.get("sp_value1") + "";
			String server = macheInfo.get("sp_code") + "";
			searchResult = commonService.executeFindFile(server, fileName);
			if (StringUtils.isNotEmpty(searchResult)) {
				String[] matchInfo = searchResult.split("\n");
				for (int i = 0; i < matchInfo.length; i++) {
					Map<String, Object> rowMap = new HashMap<>();
					rowMap.put("filePath", matchInfo[i]);
					rowMap.put("ip", ip);
					rowMap.put("server", server);
					rsList.add(rowMap);

				}
			}

		}

		return rsList;
	}
	
	/**
	 * 离线文件导出列表
	 */
    @Override
	public List<Map<String, Object>> listExportFileListOffline(String fileName) {
		List<Map<String, Object>> rsList = new ArrayList<>();

		String searchResult = null;
		List<Map<String, Object>> serverList = commonService.getAllMachine();

		for (Map<String, Object> macheInfo : serverList) {
			// Map<String, Object>
			// macheInfo=commonService.getSystemParameters(row.get("sp_value1")+"");
			String ip = macheInfo.get("sp_value1") + "";
			String server = macheInfo.get("sp_code") + "";
			searchResult = commonService.executeFindFileOffLine(macheInfo,server, fileName);
			if (StringUtils.isNotEmpty(searchResult)) {
				String[] matchInfo = searchResult.split("\n");
				for (int i = 0; i < matchInfo.length; i++) {
					Map<String, Object> rowMap = new HashMap<>();
					String [] matchInfoAry=matchInfo[i].split(",");
					String elecNo="";
					String offset="";
					String volabel="";
					if(null!=matchInfoAry&&matchInfoAry.length>=1) //获取盘槽位置和电子标签
					{
						 elecNo=matchInfoAry[0].substring(matchInfoAry[0].indexOf("-")+1);
						 offset=matchInfoAry[0].substring( 0,matchInfoAry[0].indexOf("-"));
						 rowMap.put("electronic_tag", elecNo );
						 rowMap.put("offset", offset );
						 
					}
					if (null != matchInfoAry && matchInfoAry.length >= 2) // 获取卷标和文件路径
					{

						String tempfilePath = matchInfoAry[1].substring(matchInfoAry[1].indexOf("_") + 1);

						int pos = tempfilePath.indexOf("(");
						if (pos < 0)
							pos = tempfilePath.indexOf("/") < 0 ? 15 : tempfilePath.indexOf("/");
						volabel = tempfilePath.substring(0, pos);
						rowMap.put("iso_file", matchInfoAry[1]);
						rowMap.put("volabel", volabel);
					}
					//rowMap.put("ip", ip);
					rowMap.put("server", server);				
					rsList.add(rowMap);

				}
			}

		}

		return rsList;
	}
	
    
   

	  @Override
	    public   PageInfo<Map<String, Object>>  listExportFileRecord( Map<String, Object> paramMap,Page page) {
	        return burnDao.listExportFileRecord(paramMap, page);
	    }	


	
	@Override

	public boolean CheckTaskAndRun(String soucePath) throws Exception{

		List<Map<String, Object>> rslist = listExportTask("");
		if (null != rslist && rslist.size() > 0) {
			//开门状态不进行导出
			List<Map<String, Object>> serverList = commonService.getAllMachine();
		    for (Map<String, Object> server : serverList) {
	            boolean isbusy = commonService.isBusy(server.get("sp_code")+"");
	            if (isbusy) {
	                logger.debug("服务器{}, 正在被使用， 不能进行合并");
	                throw new ServiceException(ErrorConstant.CODE2000, "盘库设备:" + server + " 正在运行， 请空闲的时候在进行合并");
	            }
	        }
			 Map<String, Object> paramMap =rslist.get(0);
			paramMap.put("export_state", ExportState.EXPORTTING.getKey());
			paramMap.put("update_time", new Date());
			burnDao.updateExportFile(paramMap);
			
			//RunTask(paramMap);
			System.out.println("downFileThread start ................................................................................................ "+paramMap.toString());
		    new Thread(new RunTaskJob(paramMap)).start();
			
		}

		return true;
	}
	
	class RunTaskJob    implements Runnable
	{
		Map<String, Object> paramMap;
		public  RunTaskJob(Map<String, Object> paramMap)
		{
			this.paramMap=paramMap;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(5*1000);
				RunTask(paramMap);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
	}
	
//	public boolean RunTask( Map<String, Object> paramMap)
//	{
//		Map exportInfo = commonService.getSystemParameters(SysConstant.EXPORT_ENV);
//		List<Map<String, Object>> serverList = commonService.getAllMachine();
//		String servers = "";
//		for (Map<String, Object> machine : serverList) {
//			servers += machine.get("sp_value1") + ",";
//		}
//		servers = servers.substring(0, servers.length() - 1);
//		String cmd = exportInfo.get("sp_value3") + "";
//		String username = exportInfo.get("sp_value1") + "";
//		String soucePath = paramMap.get("filelist") + "";
//		String targetPath = paramMap.get("export_path") + "";
//		String passwd = exportInfo.get("sp_value2") + "";
//
//		try {
//       
//			int rsInt = RunCommand.execute(cmd, username, servers,"'"+ soucePath+"'", targetPath.trim(), passwd);
//			if (-1 != rsInt) {
//				paramMap.put("export_state", ExportState.EXPORT_SUCCESS.toString());
//
//			} else {
//				paramMap.put("export_state", ExportState.EXPORT_FAILD.toString());
//			}
//			burnDao.updateExportFile(paramMap);
//
//		} catch (Exception e) {
//			paramMap.put("export_state", ExportState.EXPORT_FAILD.toString());
//			burnDao.updateExportFile(paramMap);
//		}
//		return true;
//
//	}
	
	public boolean RunTask( Map<String, Object> paramMap)
	{
		Map exportInfo = commonService.getSystemParameters(SysConstant.EXPORT_ENV);
		List<Map<String, Object>> serverList = commonService.getAllMachine();
		String servers = "";
		String rootPath=exportInfo.get("sp_value4") ==null?"/groups/":exportInfo.get("sp_value4")+"";
		int  successDownNum=0;
		String cmd = exportInfo.get("sp_value3") + "";
		String username = exportInfo.get("sp_value1") + "";
		String passwd = exportInfo.get("sp_value2") + "";
		String soucePath = paramMap.get("fileList") + "";
		String targetPath = paramMap.get("export_path") + "";
		String tmpServer=paramMap.get("server")+"";
		List<Map<String, Object>> downServerList=new ArrayList<>();
		if (tmpServer.indexOf(",") < 0)// 单服务器任务判断准确ftp服务器,
		{
			for (Map<String, Object> map : serverList) {
				if (map.get("sp_code").equals(tmpServer)) {
					downServerList.add(map);
				}
			}
		}
		
		if(downServerList.size()==0)
			downServerList = commonService.getAllMachine();
		for (Map<String, Object> machine : downServerList) {
			servers = machine.get("sp_value1") + "";		
			String[] fileList = soucePath.split(",");
			FtpUtil ftpUtil = new FtpUtil();
			FTPClient client = null;
			try {
				client = ftpUtil.getConnectionFTP(servers, 21, username, passwd);
				if(null==client)
					throw new Exception("ftp服务器:"+servers+" user: "+username+" 连接失败,请检查服务是否正常");
				for (String file : fileList) {
					String ftpPath=file.substring(0,file.lastIndexOf("/")); //截取路径名称
					String filename=file.substring(file.lastIndexOf("/")+1);//获取文件名
					String filePath=rootPath+ftpPath;
					
					boolean downRS=ftpUtil.downFileV2(client, filePath.replaceAll("//", "/"), filename, targetPath);
					if(downRS){
						successDownNum++;
					}
				}
				paramMap.put("number_success", successDownNum+"");
				paramMap.put("export_state", ExportState.EXPORT_SUCCESS.toString());
				burnDao.updateExportFile(paramMap);

			} catch (Exception e) {
				 paramMap.put("number_success", successDownNum+"");
				paramMap.put("export_state", ExportState.EXPORT_FAILD.toString());
				burnDao.updateExportFile(paramMap);
				logger.error("任务export_file_record eid ["+paramMap.get("eid")+"]下载失败:"+e.getMessage());
			} finally {
				ftpUtil.closeFTP(client);
			}
		}
		//合并split文件
	
		int rsInt = RunCommand.execute(cmd, username, servers,"'"+ soucePath+"'", targetPath.trim(), passwd);
		if (-1 != rsInt) {
			paramMap.put("export_state", ExportState.MEGE_SUCCESS.toString());

		} else {
			paramMap.put("export_state", ExportState.MEGE_FAILD.toString());
		}
		burnDao.updateExportFile(paramMap);		
		    paramMap.put("number_success", successDownNum+"");
			burnDao.updateExportFile(paramMap);

	
		return true;

	}
	
	
	
	
    @Override
	public boolean savefileExportTask(String soucePath, String exportpath,String serverInfo) throws ServiceException {
		boolean isSucc = true;
		Map<String, Object> exportMap = new HashMap<String, Object>();

		exportMap.put("fileList", soucePath);
		exportMap.put("number_success", 0);
		exportMap.put("export_state", "0");
		exportMap.put("export_desc", "准备下载");
		exportMap.put("export_path", exportpath);
		exportMap.put("server", serverInfo);
		// exportMap.put("c_user", null);
		insertExportFileRecord(exportMap);

		return true;

	}
    
   

    
	public static void main(String[] args) {

//	String testdata = "/0022_000010000020101/2013年工程/检测2013-18越洋广场项目对轨道交通六号线大剧院站、千厮门大桥引桥及C、D匝道影响第三方监测（2013.10）-王新胜/检测2013-18越洋广场项目对轨道交通六号线大剧院站、千厮门大桥引桥及C、D匝道影响第三方监测报告及原始资料（2013年10月份）/爆破振动监测波形/1010B20.DOC \n"
//				+ "/0022_000010000020101/2013年工程/检测2013-18越洋广场项目对轨道交通六号线大剧院站、千厮门大桥引桥及C、D匝道影响第三方监测（2013.10）-王新胜/检测2013-18越洋广场项目对轨道交通六号线大剧院站、千厮门大桥引桥及C、D匝道影响第三方监测报告及原始资料（2013年10月份）/爆破振动监测波形/1011B19.DOC \n"
//				+ "/0022_000010000020101/2013年工程/检测2013-18越洋广场项目对轨道交通六号线大剧院站、千厮门大桥引桥及C、D匝道影响第三方监测（2013.10）-王新胜/检测2013-18越洋广场项目对轨道交通六号线大剧院站、千厮门大桥引桥及C、D匝道影响第三方监测报告及原始资料（2013年10月份）/爆破振动监测波形/1013B20.DOC \n"
//				+"/tmp/a_split\n /tmp/b_split\n /tmp/c_split\n";
// 	return testdata;
//
//	}

		
		
  String a=" /0077_????/testdata2/testfile012/t12t26.jpg";
  int b=a.lastIndexOf("/");
   System.out.println(a.substring(0,b));
	}
	 

}
