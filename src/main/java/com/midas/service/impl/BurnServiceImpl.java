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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.midas.service.StandingbookService;
import com.midas.uitls.FtpUtil;
import com.midas.uitls.date.DateStyle;
import com.midas.uitls.date.DateUtil;
import com.midas.uitls.file.FileOper;
import com.midas.uitls.file.LocalFileOper;
import com.midas.uitls.runtime.RunCommand;
import com.midas.uitls.socket.SSHHelper;
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

    @Autowired
    private StandingbookService standingbookService;

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

    public  List<Map<String, Object>> listExportFileInfo(Map<String, Object> map) {
        return burnDao.listExportFileInfo(map);
    }
    
    public int insertExportFileRecord(Map<String, Object> map) {
        return burnDao.insertExportFileRecord(map);
    }
    
    public int insertExportFileDetail(Map<String, Object> map) {
        return burnDao.insertExportFileDetail(map);
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
    
    @Override
    public void reRunExportFile(String eid) {
        // TODO Auto-generated method stub
        burnDao.reRunExportFile(eid);
    }
    
    
    
    //TODO 删除
	
//	/**
//	 * 在线文件导出列表
//	 */
//    @Override
//	public List<Map<String, Object>> listExportFileList(String fileName) {
//		List<Map<String, Object>> rsList = new ArrayList<>();
//
//		String searchResult = null;
//		List<Map<String, Object>> serverList = commonService.getAllMachine();
//
//		for (Map<String, Object> macheInfo : serverList) {
//			// Map<String, Object>
//			// macheInfo=commonService.getSystemParameters(row.get("sp_value1")+"");
//			String ip = macheInfo.get("sp_value1") + "";
//			String server = macheInfo.get("sp_code") + "";
//			searchResult = commonService.executeFindFile(server, fileName);
//			if (StringUtils.isNotEmpty(searchResult)) {
//				String[] matchInfo = searchResult.split("\n");
//				for (int i = 0; i < matchInfo.length; i++) {
//					Map<String, Object> rowMap = new HashMap<>();
//					rowMap.put("filePath", matchInfo[i]);
//					rowMap.put("ip", ip);
//					rowMap.put("server", server);
//					rsList.add(rowMap);
//
//				}
//			}
//
//		}
//
//		return rsList;
//	}
    @SuppressWarnings("unchecked")
    @Override
	public List<Map<String, Object>> listExportFileList(String fileName) {
	 
    	 String[] keyWord=fileName.split("\\|");
    	 List<Map<String, Object>> rsList=new ArrayList<>();
    	 for (String word : keyWord) {
    		 List<Map<String, Object>> tmplist=listExportFileListOne(word);
    		 rsList.addAll(tmplist);
		}
    	return rsList;
	}
    @SuppressWarnings("unchecked")
 	public List<Map<String, Object>> listExportFileListOne(String fileName) {
 		List<Map<String, Object>> rsList = new ArrayList<>();
 		
        HashMap<String, Object> pmap=parseSearchWord(fileName);
        String keyWord=pmap.get("word")+""; //获取搜索关键字        
		ArrayList<String> keylist=(ArrayList<String>)pmap.get("list");//得到范围数组
		
 		String searchResult = null;
 		List<Map<String, Object>> serverList = commonService.getAllMachine();
 		for (Map<String, Object> macheInfo : serverList) {
 			// Map<String, Object>
 			// macheInfo=commonService.getSystemParameters(row.get("sp_value1")+"");
 			String ip = macheInfo.get("sp_value1") + "";
 			String server = macheInfo.get("sp_code") + "";
 			searchResult = commonService.executeFindFile(server, keyWord);
 		
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
 		//如果有需要匹配的序列号
		if (null != keylist && keylist.size() > 0) {
			List<Map<String, Object>> dataList = new ArrayList<>();
			for (String keyNum : keylist) {
				for (Map<String, Object> map : rsList) {
					String filePath = map.get("filePath") + "";
					if (filePath.indexOf(keyNum) > 0) {
						dataList.add(map);
					}
				}
			}
			return dataList;
		}
 		
 		

 		return rsList;
 	}
	
	/**
	 * 离线文件导出列表
	 */
    @Override
	public List<Map<String, Object>> listExportFileListOffline(String fileName) {
    	
    	
    	 String[] keyWord=fileName.split("\\|");
    	 List<Map<String, Object>> rsList=new ArrayList<>();
    	 for (String word : keyWord) {
    		 List<Map<String, Object>> tmplist=listExportFileListOfflineOne(word);
    		 rsList.addAll(tmplist);
		}
		return rsList;
	}
	
    
    /**
     * 离线单次查询
     * @param fileName
     * @return
     */
	public List<Map<String, Object>> listExportFileListOfflineOne(String fileName) {
    	
    	HashMap<String, Object> pmap=parseSearchWord(fileName);
        String keyWord=pmap.get("word")+""; //获取搜索关键字        
  		ArrayList<String> keylist=(ArrayList<String>)pmap.get("list");//得到范围数组
    	
		List<Map<String, Object>> rsList = new ArrayList<>();
		String searchResult = null;
		List<Map<String, Object>> serverList = commonService.getAllMachine();

		for (Map<String, Object> macheInfo : serverList) {
			// Map<String, Object>
			// macheInfo=commonService.getSystemParameters(row.get("sp_value1")+"");
			String ip = macheInfo.get("sp_value1") + "";
			String server = macheInfo.get("sp_code") + "";
			searchResult = commonService.executeFindFileOffLine(macheInfo,server, keyWord);
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
		
		
		//如果有需要匹配的序列号
		if (null != keylist && keylist.size() > 0) {
			List<Map<String, Object>> dataList = new ArrayList<>();
			for (String keyNum : keylist) {
				for (Map<String, Object> map : rsList) {
					String filePath = map.get("iso_file") + "";
					if (filePath.indexOf(keyNum) > 0) {
						dataList.add(map);
					}
				}
			}
			return dataList;
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
	            boolean isbusy = commonService.isBusyV2(server.get("sp_code")+"");
	            if (isbusy) {
	                logger.debug("服务器{}维护中或正在被使用， 不能进行导出");
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
	



	public boolean RunTask( Map<String, Object> paramMap)
	{
		boolean isReadyMerg=false;
		Map exportInfo = commonService.getSystemParameters(SysConstant.EXPORT_ENV);
		List<Map<String, Object>> serverList = commonService.getAllMachine();
		String servers = "";
		String rootPath=exportInfo.get("sp_value4") ==null?"/groups/":exportInfo.get("sp_value4")+"";
		int  successDownNum=0;
		String cmd = exportInfo.get("sp_value3") + "";
		String dcmd = exportInfo.get("sp_value5") + "";
		String username = exportInfo.get("sp_value1") + "";
		String passwd = exportInfo.get("sp_value2") + "";
		String soucePath = paramMap.get("fileList") + "";
		String targetPath = paramMap.get("export_path") + "";
		String tmpServer=paramMap.get("server")+"";
		String volumeLabel=paramMap.get("volume_label")+"";
		BigDecimal sumDumpSize=new BigDecimal(0);
		int failNum=0;
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
			String serverPath=machine.get("sp_code") + "";
			String[] fileList = soucePath.split(",");
			ExecutorService executorService = Executors.newFixedThreadPool(4);
			try {

				boolean isbusy = commonService.isBusyV2(machine.get("sp_code") + "");
				if (isbusy) {
					logger.debug("服务器{}, 正在被使用， 不能进行合并",paramMap.get("eid"));
					throw new ServiceException(ErrorConstant.CODE2000, "盘库设备:" + servers + " 正在运行， 请空闲的时候在进行合并");
				}
				List<Future<?>> listFutures = new ArrayList<Future<?>>();
				for (String file : fileList) {
					String ftpPath = file.substring(0, file.lastIndexOf("/")); // 截取路径名称
					String filename = file.substring(file.lastIndexOf("/") + 1);// 获取文件名
					String filePath =rootPath + ftpPath;
					logger.info("下载的文件路径为v3 {}", filePath);								
					System.out.println("最新版本V1");
					Thread.sleep(10*1000L);
					Future<?> downRs = executorService.submit(new runDownfile( filePath.replaceAll("//", "/"), filename, targetPath,servers,  username, passwd,dcmd));             
					listFutures.add(downRs);
					successDownNum++;

				}
				for (Future<?> f : listFutures) {
					  try {
						  runDownfile rs=(runDownfile)f.get();
			              String copysize=rs.copySize;
			              boolean isNum=StringTools.isNumericDigit(copysize);
			              if ("0".equals(copysize)||!isNum||"".equals(copysize)) {
			            	  logger.error(rs.path+" 文件下载失败:");
			            	  failNum++;
						}
			              else
			              {
			            	  sumDumpSize=sumDumpSize.add(new BigDecimal(copysize));
			              }
			            } catch (Exception e) {
			                logger.error("下载数据失败ExecutionException", e);			                
			                isReadyMerg = false;
			                executorService.shutdownNow();
			                throw new Exception(e);
			            }
				}
				isReadyMerg=failNum==0?true:false;
				paramMap.put("number_sum", successDownNum+"");
				paramMap.put("number_success", (successDownNum-failNum)+"");
				paramMap.put("export_state",isReadyMerg? ExportState.EXPORT_SUCCESS.toString():ExportState.EXPORT_FAILD.toString());
				burnDao.updateExportFile(paramMap);
				

			} catch (Exception e) {
				paramMap.put("number_success", (successDownNum-failNum)+"");
				paramMap.put("number_sum", successDownNum+"");
				paramMap.put("export_state", ExportState.EXPORT_FAILD.toString());
				burnDao.updateExportFile(paramMap);
				logger.error("任务export_file_record eid ["+paramMap.get("eid")+"]下载失败:"+e.getMessage());
			} 
			finally {
					executorService.shutdown();
		
			}
			
		}
		
		// 合并split文件
		if (isReadyMerg) {
			// int rsInt = RunCommand.execute(cmd, username, servers,"'"+
			// soucePath+"'", targetPath.trim(), passwd);
			int rsInt = RunCommand.execute(cmd, targetPath.trim());
			if (-1 != rsInt) {
				paramMap.put("export_state", ExportState.MEGE_SUCCESS.toString());

			} else {
				paramMap.put("export_state", ExportState.MEGE_FAILD.toString());
			}
			burnDao.updateExportFile(paramMap);

			// 更新台帐数据
			if (!StringUtils.isEmpty(volumeLabel)) {
				Map<String, Object> standingMap = new HashMap<String, Object>();
				standingMap.put("eid", paramMap.get("eid") + "");
				standingMap.put("volume_label", volumeLabel);
				standingMap.put("states", "1");
				// 修改为最后一个刻录完成的时间
				standingMap.put("update_time", new Date());
				standingMap.put("type", 2);
				standingbookService.update(standingMap);
			}

		}

		return true;

	}
	
	
	 class runDownfile implements Callable<runDownfile> 
	 {
	
		 String path=null;
		 String fileName=null;
		 String localPath=null;
		 String server=null;
		 String userName=null;
		 String passwd=null;
		 String copySize=null;
		 String cmd=null;
		 public runDownfile( String path, String fileName, String localPath,String server,String userName,String passwd,String cmd)
		 {
			
			 this.path=path;
			 this.fileName=fileName;
			 this.localPath=localPath;
			 this.server=server;
			 this.userName=userName;
			 this.passwd=passwd;
			 this.cmd=cmd;
		 }
		 
		 
		 @SuppressWarnings("unused")
		@Override
	     public runDownfile call() throws ServiceException {
			 
			FtpUtil ftpUtil = new FtpUtil();
			FTPClient client = null;
			try {
				long st = System.currentTimeMillis();
				System.out.println("path:"+path+"   localPath:"+localPath+"   fileName:"+fileName);
				//String loclDir=fileNameToCreateDir(fileName);
				String targetFile=localPath+File.separator+fileName;
				String sourceFile=path+File.separator+fileName;
			//	String cmd="mkdir -p "+localPath+"; cp  -f "+sourceFile+" "+localPath+ ";ls -lk "+targetFile+" | awk '{print $5}'";
				String slot=sourceFile.substring(sourceFile.indexOf("_")-4,sourceFile.indexOf("_"));
				String execCmd=cmd+" "+sourceFile+" " +localPath +" "+fileName +" "+slot ;				
				execCmd=execCmd.replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)");
				System.out.println("执行下载命令:"+execCmd);
				copySize=SSHHelper.exec(server, userName, passwd, 22, execCmd);
				System.out.println("执行下载命令:"+execCmd+" \n 下载大小:"+copySize);
	         //BigDecimal size = fileOper.copyV2(path, localPath, fileName, fileName);
	    
	         
				logger.error("文件{}下载耗时,{} 毫秒", path + fileName, System.currentTimeMillis() - st);
			
				return this;
			} catch (Exception e) {
				logger.error("下载文件数据失败" + fileName + " 源路径:" + path + " 目标路径 : " + localPath, e);
				return this;
			} 
		
		}
	}
		 
//		 @Override
//	        public Boolean call() throws ServiceException {
//			 
//			FtpUtil ftpUtil = new FtpUtil();
//			FTPClient client = null;
//			try {
//
//				client = ftpUtil.getConnectionFTP(server, 21, userName, passwd);
////				client.setDefaultTimeout(600* 1000);
////				client.setConnectTimeout(600* 1000);
////				client.setDataTimeout(20 * 1000);
//                client.setBufferSize(1024);
//				System.out.println("超时时间设置");
//				if (null == client)
//					throw new Exception("ftp服务器:" + server + " user: " + userName + " 连接失败,请检查服务是否正常");
//				long st = System.currentTimeMillis();
//				boolean rs = ftpUtil.downFileV2(client, path, fileName, localPath);
//				logger.error("文件{}下载耗时,{} 毫秒", path + fileName, System.currentTimeMillis() - st);
//				client.logout();
//				return rs;
//			} catch (Exception e) {
//				logger.error("下载文件数据失败" + fileName + " 源路径:" + path + " 目标路径 : " + localPath, e);
//				return false;
//			} finally {
//				ftpUtil.closeFTP(client);
//			}
//		
//		}
		 
//		 @Override
//	        public Boolean call() throws ServiceException {
//			 
//			FtpUtil ftpUtil = new FtpUtil();
//			FTPClient client = null;
//			try {
//				   // 文件操作
//			    FileOper   fileOper = new LocalFileOper();
//				long st = System.currentTimeMillis();
//				System.out.println("path:"+path+"   localPath:"+localPath+"   fileName:"+fileName);
//				//String loclDir=fileNameToCreateDir(fileName);
//               BigDecimal size = fileOper.copyV2(path, localPath, fileName, fileName);
//                if (size.doubleValue()<=0) {
//                   return false;
//                }
//                logger.info("copy文件或者目录： {}， 大小为： {}", path, size);
//				logger.error("文件{}下载耗时,{} 毫秒", path + fileName, System.currentTimeMillis() - st);
//			
//				return true;
//			} catch (Exception e) {
//				logger.error("下载文件数据失败" + fileName + " 源路径:" + path + " 目标路径 : " + localPath, e);
//				return false;
//			} 
//		
//		}
//	 }
	 
	 
//	 @Override
//     public Boolean call() throws ServiceException {
//		 
//		FtpUtil ftpUtil = new FtpUtil();
//		FTPClient client = null;
//		try {
//			   // 文件操作
//		    //FileOper   fileOper = new LocalFileOper();
//			long st = System.currentTimeMillis();
//			String filePath=path+File.separator+fileName;
//			filePath=filePath.replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)");
//			System.out.println("filePathV5:"+filePath+"   localPath:"+localPath+"   fileName:"+fileName);
//             
//              int rs=RunCommand.execute("/tmp/cpyExport.sh",filePath,localPath);
//          if(rs==-1)
//          {
//        	  throw new Exception("文件"+path+fileName+"下载失败");
//          }
//			logger.error("文件{}下载耗时,{} 毫秒", filePath, System.currentTimeMillis() - st);
//		
//			return true;
//		} catch (Exception e) {
//			logger.error("下载文件数据失败" + fileName + " 源路径:" + path + " 目标路径 : " + localPath, e);
//			return false;
//		} 
//	
//	}
//}
	 
	@Override
	public boolean savefileExportTask(String soucePath, String exportpath) throws ServiceException {
		boolean isSucc = true;
		Map<String, Object> exportMap = new HashMap<String, Object>();

		// 剥离server字符串---------------------------------------------------------------------
		String[] sourcePathList = soucePath.split(",");
		String serverInfo = "";
		StringBuffer saveFilePathBuff = new StringBuffer();
		for (String tmpPath : sourcePathList) {
			if (tmpPath.indexOf(":") > 0) {
				String tmpServerInfo = tmpPath.substring(0, tmpPath.indexOf(":"));
				saveFilePathBuff.append(tmpPath.substring(tmpPath.indexOf(":") + 1)).append(",");
				if (serverInfo.indexOf(tmpServerInfo) < 0) {
					serverInfo += tmpServerInfo + ",";
				}
			} else {
				saveFilePathBuff.append(tmpPath);
			}

		}

		if (serverInfo.lastIndexOf(",") > 1)
			serverInfo = serverInfo.substring(0, serverInfo.lastIndexOf(","));
		String saveFilePath = saveFilePathBuff + "";
		if (saveFilePath.lastIndexOf(",") > 1)
			saveFilePath = saveFilePath.substring(0, saveFilePath.lastIndexOf(","));
		
		saveFilePath=saveFilePath.replaceAll(" ", "");
		exportpath=exportpath.replaceAll(" ", "");
	
		// ------------------------------------------------------------------------------
		// online

		int beginNum = saveFilePath.indexOf("_");
		int endNum = saveFilePath.indexOf("(");
		if (endNum<0||endNum-beginNum>20) {
			endNum= saveFilePath.indexOf("/",beginNum);
		}
		String volumeLabel = "";
		if (beginNum > 0 && endNum > 0) {
			volumeLabel = saveFilePath.substring(beginNum + 1, endNum);
		} else {
			if (saveFilePath != null && saveFilePath.length() > 21) {
				volumeLabel = saveFilePath.substring(6, 21);
			}

		}

		// 导出任务保存
		exportMap.put("fileList", saveFilePath);
		exportMap.put("number_success", 0);
		exportMap.put("export_state", "0");
		exportMap.put("export_desc", "准备下载");
		exportMap.put("export_path", exportpath);
		exportMap.put("server", serverInfo);
		exportMap.put("volume_label", volumeLabel);
		// exportMap.put("c_user", null);
		insertExportFileRecord(exportMap);
		// List<Map<String, Object>> fileInfo=listExportFileInfo(exportMap);
		// String eid=null;
		// if (null!=fileInfo&&fileInfo.size()>0) {
		// Map map=fileInfo.get(0);
		// eid=map.get("eid")+"";
		// }
		// Map<String, Object> fileDetailMap = new HashMap<String, Object>();
		// for (String tmpPath : sourcePathList) {
		// fileDetailMap.put("eid", eid);
		// insertExportFileDetail(fileDetailMap);
		// }

		Map<String, Object> paraMap = new HashMap<String, Object>();

		// 更新台帐
		if (!StringUtils.isEmpty(volumeLabel)) {
			paraMap.put("volume_label", volumeLabel);
			paraMap.put("type", 1);
			List<Map<String, Object>> mergelist = standingbookService.getStandingbook(paraMap);
			for (Map<String, Object> o : mergelist) {
				Map<String, Object> standingMap = new HashMap<String, Object>();
				standingMap.put("eid", exportMap.get("eid"));
				standingMap.put("volume_label", volumeLabel);
				standingMap.put("data_type", o.get("data_type"));
				standingMap.put("work_area", o.get("work_area"));
				standingMap.put("construction_unit", o.get("construction_unit"));
				standingMap.put("construction_year", o.get("construction_year"));
				standingMap.put("data_quantity", o.get("data_quantity"));
				standingMap.put("burn_count", o.get("burn_count"));
				standingMap.put("create_time", new Date());
				standingMap.put("type", "2");
				standingbookService.insert(standingMap);
			}
		}

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
//		List<Future> list=new ArrayList<>();
//		int i=0;
//		ExecutorService executorService = Executors.newFixedThreadPool(3);  
//       while(i<10)
//       {
//    	   
//		Future<?> downRs = BurnBase.executeSubmitS(new Callable<String>() {
//			@Override
//			public String call() throws Exception {
//		
//					Thread.sleep(5000);
//				System.out.println("线程运行:"+Thread.currentThread().getId());
//				return Thread.currentThread().getId()+"------------------return ";
//			}
//		});
//		i++;
//		list.add(downRs);
//       }
//       for (Future future : list) {
//		try {
//			System.out.println(future.get());
//		} catch (InterruptedException | ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
		BurnServiceImpl bb=new BurnServiceImpl();
		System.out.println(bb.parseSearchWord("W116321-116337"));
		

	}
	 
	/**
	 * 关键字解析
	 * @param keyword
	 * @return
	 */
	private HashMap parseSearchWord(String keyword) {
		HashMap<String, Object> rsMap = new HashMap<>();
		if (StringUtils.isEmpty(keyword))
			return null;
		ArrayList<String> difflist = new ArrayList<>();
		String searchWord = "";
		String rsStr = "";
		String startNum = "";
		String endNum = "";
		int splitPos = 0;
		try {

			if (keyword.indexOf("-") > 0) {
				startNum = keyword.substring(0, keyword.indexOf("-"));
				endNum = keyword.substring(keyword.indexOf("-") + 1);
				if (Character.isLetter(startNum.charAt(0))) {
					startNum = startNum.substring(1);
				}
				if (Character.isLetter(endNum.charAt(0))) {
					endNum = endNum.substring(1);
				}
				// 比较2字符串不同的部分截取位置
				char[] a1 = startNum.toCharArray();
				char[] b1 = endNum.toCharArray();
				int c = a1.length < b1.length ? a1.length : b1.length;
				for (int i = 0; i < c; i++) {
					if (a1[i] != b1[i]) {
						splitPos = i;
						// System.out.println("错误位置"+i);
						break;
					}
				}
				searchWord = keyword.substring(0, splitPos + 1); // 得到模糊匹配字符串
				int diffStartNum = Integer.parseInt(startNum.substring(splitPos));
				int diffEndNum = Integer.parseInt(endNum.substring(splitPos));
				if (diffStartNum > diffEndNum) {
					int tmpNum = diffEndNum;
					diffEndNum = diffStartNum;
					diffStartNum = tmpNum;

				}
				for (int j = diffStartNum; j <= diffEndNum; j++) {
					difflist.add(searchWord + j);
				}
				rsMap.put("list", difflist);
				rsMap.put("word", searchWord);
			} else {

				rsMap.put("list", null);
				rsMap.put("word", keyword);
			}
			return rsMap;
		} catch (Exception e) {
			logger.error("搜索关键字解析错误" + e);
			rsMap.put("list", null);
			rsMap.put("word", keyword);
			return rsMap;
		}

	}

}
