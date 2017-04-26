package com.midas.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.util.ObjectBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock.Limit;
import com.google.gson.Gson;
import com.midas.constant.ErrorConstant;
import com.midas.constant.SysConstant;
import com.midas.dao.BurnDao;
import com.midas.dao.CommonDao;
import com.midas.enums.DataType;
import com.midas.exception.ServiceException;
import com.midas.service.BurnService;
import com.midas.service.CommonService;
import com.midas.uitls.socket.TelnetOperator;
import com.midas.uitls.tools.CommonsUtils;
import com.midas.vo.BurnProgress;
import com.midas.vo.FileVo;
import com.midas.vo.Mag;
import com.midas.vo.Slot;
import com.midas.vo.burn.Drivers;
import com.midas.vo.burn.Machine;
import com.midas.vo.offline.OfflineMachine;

@Service
public class CommonServiceImpl implements CommonService {

    private Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    
    @Autowired
     private BurnDao burnDao;

    @Override
    public String seqUniqueNextVal(String cdate, DataType dataType) {
        return seqUniqueNextVal(cdate, dataType.getType());
    }

    @Override
    public String seqUniqueNextVal(String cdate, String ctype) {
        
        if(DataType.RAW_DATA.getType().equals(ctype)) {
            ctype = "W";
        } else if(DataType.TWO_DATA.getType().equals(ctype) || DataType.THREE_DATA.getType().equals(ctype)) {
            ctype = "R";
        }
        
        StringBuffer sb = new StringBuffer();
        int uniqueNumber = commonDao.seqUniqueNextVal(cdate, ctype);
        String seqNumber = String.format("%06d", uniqueNumber);
        sb.append(ctype);
        sb.append(cdate);
        sb.append(seqNumber);
        logger.info("using cdate: {}, ctype: {}, result unique number: {}, unique string : {}", cdate, ctype,
                uniqueNumber, sb);
        return sb.toString();
    }

    @Override
    public List<Map<String, Object>> listSystemParameters(String code) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sp_code", code);
        map.put("sp_state", BooleanUtils.toInteger(true));
        return commonDao.listSystemParameters(map);
    }

    @Override
    public Map<String, Object> getSystemParameters(Map<String, Object> map) {
        List<Map<String, Object>> list = commonDao.listSystemParameters(map);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public Map<String, Object> getSystemParameters(String code) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sp_code", code);
        map.put("sp_state", BooleanUtils.toInteger(true));
        return getSystemParameters(map);
    }

    // TODO 待补充方法
    /**
     * 通过服务获取机器可用光盘数量
     */
    @Override
    public int effectiveQuantity(String server) {
        int i = 0;
        try {
            Machine machine = commandQUERYSTATION(server);
            if (!machine.isOpenDoor()) {
                List<Mag> list = machine.getMag();
                for (Mag mag : list) {
                    for (Slot slot : mag.getSlot()) {
                        if (slot.getTrayexist() == 1 && slot.getIsblank() == 1) {
                            i++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("盘库出现异常, 不能使用", e);
        }
        return i;
    }

    /**
     * 只有在目录中存在.FSOK才是正常连接的服务
     */
    public boolean isConnect(Map<String, Object> map) {
        String server = ObjectUtils.toString(map.get("sp_value1"));
        Map<String, Object> machineInfo = this.getSystemParameters(server);
        try {
            String flagfile = CommonsUtils.getPropertiesValue(SysConstant.LINK_FLAG_FILE);
            File file = new File(machineInfo.get("sp_value4") + flagfile);
            if (file.exists()) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 通过指令获取机器是否繁忙 QUERYSTATION,\n
     * 
     * @param server
     *            机器
     */
    @Override
    public boolean isBusy(String server) {
        boolean isbusy = true;
        try {
            Machine machine = commandQUERYSTATION(server);
            if (!machine.isOpenDoor()) {
                List<Drivers> list = machine.getDirvers();
                for (Drivers d : list) {
                    if (d.isBusy() && d.isValid()) {
                        logger.info("驱动器繁忙: {}", d);
                        isbusy = true;
                        break;
                    }
                    isbusy = false;
                }
            }
        } catch (Exception e) {
            logger.error("盘库繁忙：", e);
        }
        return isbusy;
    }
    
    
   

    
    
    @Override
    public boolean isBusyV2(String server) {
        boolean isbusy = false;
        int  busyNum=0;
        try {
            Machine machine = commandQUERYSTATION(server);
            if (!machine.isOpenDoor()) {
                List<Drivers> list = machine.getDirvers();
                for (Drivers d : list) { 
                	  if(busyNum>4) //当空闲驱动器小于4 则提示繁忙
                      {
                		logger.info("驱动器繁忙,可用驱动器不足: {}", d);                      
                      	isbusy=true;
                      	break;
                      }
                    if (d.isBusy() && d.isValid()) {
                        busyNum++;
                    }                  
                }
                isbusy = false;
            }
            logger.info(server+" 繁忙光驱数: "+busyNum);
        } catch (Exception e) {
            logger.error("盘库繁忙：", e);
        }
        return isbusy;
    }


    /**
     * 通过发送 QOFFLINEJOG,JOBNAME,\N 指令获取刻录的信息 QOFFLINEJOB，卷标名，\n
     * 
     * @param jobName
     *            唯一卷标号
     * @return
     */
    public BurnProgress getBurnStatus(String server, String jobName) {

        BurnProgress bp = new BurnProgress();

        String command = "QOFFLINEJOB," + jobName + ",";
        String result = command(server, command);

        if ("Query aa error!".equals(result.trim())) {
            return bp;
        }

        String[] datas = result.split("\n");
        List<Map<String, String>> warnings = new ArrayList<Map<String, String>>();
        List<Map<String, String>> ends = new ArrayList<Map<String, String>>();
        List<Map<String, String>> infos = new ArrayList<Map<String, String>>();
        try {
            for (String s : datas) {
                if (null == s || "".equals(s)) {
                    continue;
                }
                if (s.startsWith("<SPLIT>")) {
                    String[] data = s.split(",");
                    try {
                        bp.setSplitsNubmer(Integer.parseInt(data[3]));
                    } catch (Exception e) {
                        throw new ServiceException(ErrorConstant.CODE2000, "返回结果获取刻录盘数失败");
                    }
                } else if (s.startsWith("<WARNING>")) {
                    String[] data = s.split(",");
                    String msg = data[2];
                    String[] m = msg.split(":");
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("time", data[1]);
                    map.put("errorCode", m[0].replace("<", ""));
                    map.put("isofile", m[1].trim());

                    warnings.add(map);
                } else if (s.startsWith("<INFO>")) {
                    String[] data = s.split(",");
                    String msg = data[2];
                    String[] m = msg.split(":");
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("time", data[1]);
                    map.put("info", data[2]);
                    map.put("message", data[3]);

                    infos.add(map);
                    
                } else if (s.startsWith("<END>")) {
                    String[] data = s.split(",");
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("time", data[1]);
                    map.put("isofile", data[2].trim());
                    map.put("position", data[3]);
                    map.put("rfid", data[4]);
                    ends.add(map);
                }
            }

            Iterator<Map<String, String>> iter = warnings.iterator();
            while (iter.hasNext()) {
                Map<String, String> map = iter.next();
                for (Map<String, String> m : ends) {
                    if (map.get("isofile").equalsIgnoreCase(m.get("isofile"))) {
                        iter.remove();
                    }
                }
            }
        } catch (Exception e) {
            throw new ServiceException(ErrorConstant.CODE2000, "获取刻录结果失败");
        }

        bp.setEnds(ends);
        bp.setWarnings(warnings);
        bp.setInfos(infos);
        return bp;
    }

    @Override
    public Map<String, Object> getTagposition(String tag) {
        int magNo = 0;
        // 盘库机器名称
        String serverInfo = null;
        // 盘库机器SERVER1， or SERVER2
        String server = null;
        Object spCode = null;
        boolean isOk = false;
        try {
            // 先找盘库设备
            List<Map<String, Object>> discList = this.listSystemParameters(SysConstant.DISC_LIBRARY);
            for (Map<String, Object> map : discList) {
                Machine machine = null;
                try {
                    machine = commandQUERYSTATION(ObjectUtils.toString(map.get("sp_value1")));
                } catch (Exception e) {
                    logger.error("服务： " + map.get("sp_value1") + " 不正常", e);
                    continue;
                }
                if (!machine.isOpenDoor()) {
                    List<Mag> magList = machine.getMag();
                    for (Mag mag : magList) {
                        
//                      List<Slot>  slotList= mag.getSlot();
//                      for (Slot slot : slotList) {
//                          if(slot.getIsblank()==2){  //
//                              
//                              logger.info("过滤掉刻录坏盘：{},{} ,{}" ,slot.getMediatype(),slot.getLabel(),slot.getIsblank());
//                              mag.getSlot().remove(slot);
//                        
//                      }
                      
                      
//                  }
                        
                              if (tag.equals(mag.getRfid().trim())) {
                                  magNo = mag.getMagNo() + 1;
                                  serverInfo = ObjectUtils.toString(map.get("sp_name"));
                                  server = ObjectUtils.toString(map.get("sp_value1"));
                                  spCode = map.get("sp_code");
                                  isOk = true;
                                  break;
                              } 
                              
                              

                       
                    }
                }
                if (isOk) {
                    break;
                }
            }
            // isOk 为false的情况下在盘库设备未找到相关信息, 需要查询离线柜
            if (!isOk) {
                List<Map<String, Object>> offlineList = this.listSystemParameters(SysConstant.OFF_LINE_CABINET);
                for (Map<String, Object> map : offlineList) {
                    String result = null;
                    try {
                        result = getOfflineInfo(map);
                        if (null == result || "".equals(result)) {
                            logger.error("离线柜：" + map + ", 查询返回结果为空");
                            continue;
                        }
                    } catch (Exception e) {
                        logger.error("离线柜查询失败", e);
                        continue;
                    }
                    Gson g = new Gson();
                    OfflineMachine offline = g.fromJson(result, OfflineMachine.class);
                    if (offline.isOpenDoor()) {
                        logger.info("离线柜开门状态： {}, 状态查询不正确", map);
                        continue;
                    }
                    for (Mag mag : offline.getMag()) {
                        if (tag.equals(mag.getRfid().trim())) {
                            magNo = mag.getMagNo();
                            serverInfo = ObjectUtils.toString(map.get("sp_name"));
                            server = ObjectUtils.toString(map.get("sp_value1"));
                            spCode = map.get("sp_code");
                            isOk = true;
                            break;
                        }
                    }
                    if (isOk) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new ServiceException(ErrorConstant.CODE3000, "未找到所在位置并出错", e);
        }
        // machine and position
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != serverInfo && !"".equals(serverInfo)) {
            map.put("position", magNo);
        }
        map.put("serverName", serverInfo);
        map.put("server", server);
        map.put("spCode", spCode);
        return map;
    }
    
    
    @Override
    public Map<String, Object> getTagpositionDirect(String tag) {
        int magNo = 0;
        // 盘库机器名称
        String serverInfo = null;
        // 盘库机器SERVER1， or SERVER2
        String server = null;
        Object spCode = null;
        boolean isOk = false;
        try {

          
                List<Map<String, Object>> offlineList = this.listSystemParameters(SysConstant.OFF_LINE_CABINET);
                for (Map<String, Object> map : offlineList) {
                    String result = null;
                    try {
                        result = getOfflineInfo(map);
                        if (null == result || "".equals(result)) {
                            logger.error("离线柜：" + map + ", 查询返回结果为空");
                            continue;
                        }
                    } catch (Exception e) {
                        logger.error("离线柜查询失败", e);
                        continue;
                    }
                    Gson g = new Gson();
                    OfflineMachine offline = g.fromJson(result, OfflineMachine.class);
                    if (offline.isOpenDoor()) {
                        logger.info("离线柜开门状态： {}, 状态查询不正确", map);
                        continue;
                    }
                    for (Mag mag : offline.getMag()) {
                        if (tag.equals(mag.getRfid().trim())) {
                            magNo = mag.getMagNo();
                            serverInfo = ObjectUtils.toString(map.get("sp_name"));
                            server = ObjectUtils.toString(map.get("sp_value1"));
                            spCode = map.get("sp_code");
                            isOk = true;
                            break;
                        }
                    }
                    if (isOk) {
                        break;
                    }
                }
       
        } catch (Exception e) {
            throw new ServiceException(ErrorConstant.CODE3000, "未找到所在位置并出错", e);
        }
        // machine and position
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != serverInfo && !"".equals(serverInfo)) {
            map.put("position", magNo);
        }
        map.put("serverName", serverInfo);
        map.put("server", server);
        map.put("spCode", spCode);
        return map;
    }

    @Override
    public boolean executeDUMPMEDIA(String server, String position) {
        boolean bool = true;
        try {
            commandOnly(server, "DUMPMEDIA," + position + ",");
        } catch (Exception e) {
            bool = false;
            logger.error("发送指令失败", e);
        }
        return bool;
    }

    public List<Map<String, Object>> getAllMachine() {
     // 获取所有机器配置
        List<Map<String, Object>> machines = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> listDiscLibrary = listSystemParameters(SysConstant.DISC_LIBRARY);
        for(Map<String, Object> map : listDiscLibrary) {
            machines.add(getSystemParameters(ObjectUtils.toString(map.get("sp_value1"))));
        }
        return machines;
    }
    
    public static void main(String[] args) {
        String s = "RET,DUMPMEDIA,%d,%d1";
        String[] ss = s.split(",");
        System.out.println(ss.length);
        System.out.println(ss[3]);
        CommonServiceImpl service=new CommonServiceImpl();
        service.executeFindFile("","db-logs.lock/db-logs.lock");
        
        
        // int i = 51;
        // System.out.println((i - 1) % 50 + 1);
        // File f = new File("c:/ab.txt");
        // System.out.println(f.getAbsolutePath());
    }

    private Machine commandQUERYSTATION(String server) {
        String command = "QUERYSTATION,";
        String result = command(server, command);
        if (null == result || "".equals(result)) {
            throw new ServiceException(ErrorConstant.CODE3000, "盘库未返回信息， 服务器关闭");
        }
        if (result.startsWith("Connect is down")) {
            throw new ServiceException(ErrorConstant.CODE3000, "盘库已关机:" + server);
        }
        Gson g = new Gson();
        return g.fromJson(result, Machine.class);
    }

    private String command(String server, String command) {
        Map<String, Object> machineInfo = this.getSystemParameters(server);
        if (null == machineInfo || machineInfo.isEmpty()) {
            throw new ServiceException(ErrorConstant.CODE2000, "未找到机器信息");
        }
        String ip = ObjectUtils.toString(machineInfo.get("sp_value1"));
        int port = Integer.parseInt(ObjectUtils.toString(machineInfo.get("sp_value5")));
        String reslt = null;
        try {
            reslt = TelnetOperator.commandBurnMachine(ip, port, command);
        } catch (Exception e) {
            throw new ServiceException(ErrorConstant.CODE3000,
                    "执行查询指令失败, 不能判断机器是否正常, ip: " + ip + ", port:" + port + ", command:" + command);
        }
        logger.debug("对机器: {}, 执行指令: {}, 返回结果为: {}", server, command, reslt);
        String msg = null;
        if (null != reslt && !"".equals(reslt)) {
            if (reslt.length() > 25) {
                msg = reslt.substring(0, 20);
            } else {
                msg = reslt;
            }
        }
        logger.info("对机器{}， 执行指令： {}, 返回结果为： {}", server, command, msg);
        return reslt;
    }

    private void commandOnly(String server, String command) {
        Map<String, Object> machineInfo = this.getSystemParameters(server);
        if (null == machineInfo || machineInfo.isEmpty()) {
            throw new ServiceException(ErrorConstant.CODE2000, "未找到机器信息");
        }
        String ip = ObjectUtils.toString(machineInfo.get("sp_value1"));
        int port = Integer.parseInt(ObjectUtils.toString(machineInfo.get("sp_value5")));
        try {
            TelnetOperator.commandOnly(ip, port, command);
        } catch (Exception e) {
            throw new ServiceException(ErrorConstant.CODE3000,
                    "执行查询指令失败, 不能判断机器是否正常, ip: " + ip + ", port:" + port + ", command:" + command);
        }
        logger.debug("对机器: {}, 执行指令: {}, 返回结果为: {}", server, command);
    }

    private String getOfflineInfo(Map<String, Object> map) {
        String result = null;
        String command = "QUERYSTATION,";
        if (null == map || map.isEmpty()) {
            throw new ServiceException(ErrorConstant.CODE2000, "未找到机器信息");
        }
        String ip = ObjectUtils.toString(map.get("sp_value1"));
        int port = Integer.parseInt(ObjectUtils.toString(map.get("sp_value5")));
        try {
            result = TelnetOperator.commandOffline(ip, port, command);
        } catch (Exception e) {
            logger.error("对盘库机器执行查询指令失败", e);
            throw new ServiceException(ErrorConstant.CODE3000, "对机器： {" + map + "} 执行查询指令失败, 不能判断机器是否正常");
        }
        logger.info("对离线柜： {}, 执行指令： {}, 返回结果为： {}", map, command, result);
        return result;
    }
    
    
    public String executeFindFile(String server,String name){
    	    	
//    	String testdata = "/0552_W20160512000001(84-80)/?38/W116331.001.SGY_midas_6_005.split \n"
//		+ "/0022_000010000020101/2013年工程/检测2013-18越洋广场项目对轨道交通六号线大剧院站、千厮门大桥引桥及C、D匝道影响第三方监测（2013.10）-王新胜/检测2013-18越洋广场项目对轨道交通六号线大剧院站、千厮门大桥引桥及C、D匝道影响第三方监测报告及原始资料（2013年10月份）/爆破振动监测波形/W20160(1-2).split\n"
//		+ "/0022_000010000020101/2013年工程/检测2013-18越洋广场项目对轨道交通六号线大剧院站、千厮门大桥引桥及C、D匝道影响第三方监测（2013.10）-王新胜/检测2013-18越洋广场项目对轨道交通六号线大剧院站、千厮门大桥引桥及C、D匝道影响第三方监测报告及原始资料（2013年10月份）/爆破振动监测波形/W20160408000001(34-10).split \n"
//		+"/tmp/a_split\n /tmp/b_split\n /tmp/c_split\n";
//              return testdata;
    	int  exportLimitNum=1000;
    	Map<String, Object> limitNumMap = this.getSystemParameters("EXPORT_LIMIT_NUM");
    	if(null!=limitNumMap)
    	{
    		String exportNumTemp=limitNumMap.get("sp_value1")+"";
    		exportNumTemp= (StringUtils.isEmpty(exportNumTemp)?1000:exportNumTemp)+"";
    	    exportLimitNum=Integer.parseInt(exportNumTemp);
    	}
    	
		Map<String, Object> machineInfo = this.getSystemParameters(server);
		String result = null;
		String command = "FINDFILE," + name + ",";

		if (null == machineInfo || machineInfo.isEmpty()) {
			throw new ServiceException(ErrorConstant.CODE2000, "未找到机器信息");
		}
		String ip = ObjectUtils.toString(machineInfo.get("sp_value1"));
		int port = Integer.parseInt(ObjectUtils.toString(machineInfo.get("sp_value5")));
		// String ip="192.168.0.227";
		// int port=2021;
		try {
			result = TelnetOperator.commandFileSearch(ip, port, command,exportLimitNum);
		} catch (Exception e) {
			throw new ServiceException(ErrorConstant.CODE3000,
					"执行查询指令失败, 不能判断机器是否正常, ip: " + ip + ", port:" + port + ", command:" + command);
		}
		logger.debug("对机器: {}, 执行指令: {}, 返回结果为: {}", server, command, result);
		return result;
    }
    
    public List<FileVo> executeFindFileBySocket(String server,String name){

        Map<String, Object> machineInfo = this.getSystemParameters(server);
        List<FileVo> fileList=null;
        String command = "FINDFILE,"+name+",";
        
        if (null == machineInfo || machineInfo.isEmpty()) {
            throw new ServiceException(ErrorConstant.CODE2000, "未找到机器信息");
        }
        String ip = ObjectUtils.toString(machineInfo.get("sp_value1"));
        int port = Integer.parseInt(ObjectUtils.toString(machineInfo.get("sp_value5")));
            try {
            Socket socket = new Socket(ip, port);
            // 发送命令
            OutputStream socketOut = socket.getOutputStream();
            command += "\r\n";
            socketOut.write(command.getBytes("UTF-8"));
            // 接收服务器的反馈
            BufferedReader br = new BufferedReader(new InputStreamReader(
            socket.getInputStream(), "UTF-8"));
            PrintWriter pw=getWriter(socket);
            String msg=null;
            
              fileList=new ArrayList<FileVo>();
            while((msg=br.readLine())!=null){
                FileVo vo=new FileVo();
                pw.println(msg);
                vo.setFilepath(msg);
                if(msg.contains(" match results in online !")){
                    break;
                }else{
                    fileList.add(vo);
                }
            }
            


            System.out.println("Server return fileList: " + fileList.size());
            // 发送关闭命令
//            socketOut.write("sb\r\n".getBytes("UTF-8"));// 服务器端接收到“sb”会终止本次连接
            
            socket.close();

            } catch (IOException e) {

            //e.printStackTrace();
                
            System.err.println("同服务器交互时"+e.getMessage());

            }

            return fileList;

            }
    private   PrintWriter getWriter(Socket socket)throws IOException{
        OutputStream socketOut=socket.getOutputStream();
        return new PrintWriter(socketOut,true);
    }
    
    public String executeFindFileOffLine(Map<String, Object> map,String server ,String name){
        
        String result = null;
        String command = "FINDFILEOFFLINE,"+name+",";
        if (null == map || map.isEmpty()) {
            throw new ServiceException(ErrorConstant.CODE2000, "未找到机器信息");
        }
        String ip = ObjectUtils.toString(map.get("sp_value1"));
        int port = Integer.parseInt(ObjectUtils.toString(map.get("sp_value5")));
//        ip="192.168.0.227";
//        port=2021;
        int  exportLimitNum=1000;
    	Map<String, Object> limitNumMap = this.getSystemParameters("EXPORT_LIMIT_NUM");
    	if(null!=limitNumMap)
    	{
    		String exportNumTemp=limitNumMap.get("sp_value1")+"";
    		exportNumTemp= (StringUtils.isEmpty(exportNumTemp)?1000:exportNumTemp)+"";
    	    exportLimitNum=Integer.parseInt(exportNumTemp);
    	}
    	
        try {
            result = TelnetOperator.commandFileSearch(ip, port, command,exportLimitNum);
            
        } catch (Exception e) {
            logger.error("对盘库机器执行查询指令失败", e);
            throw new ServiceException(ErrorConstant.CODE3000, "对机器： {" + map + "} 执行查询指令失败, 不能判断机器是否正常");
        }
        logger.info("对离线柜： {}, 执行指令： {}, 返回结果为： {}", map, command, result);
        return result;
    }
    
    
    /**
     * 更新光盘位置
     * @return
     */
    @Override
    public String flushDiscPosition()
    {
		logger.info("refreshDiscPosInfo--------同步光盘位置信息>>>>>>>>>" + System.currentTimeMillis());
		List<Map<String, Object>> servers=getAllMachine();
	    for (Map<String, Object> server : servers) {
			String ipdeviceIp=server.get("sp_value1")+"";
			String serverName=server.get("sp_code")+"";
			Integer port=server.get("sp_value5")==null?2021:Integer.parseInt(server.get("sp_value5")+"");
		
			try {
				Gson gson = new Gson();				
				//TODO TESTING DQ PROJECT
				//String srcStr="{\"MachineType\":\"MDS-CHGMC-8100\",\"DoorStatus\":0,\"EventCnt\":0,\"Mag\":[{\"MagNo\":0,\"Rfid\":\"0001060004\",\"Slot\":[{\"id\":1,\"cdexist\":1,\"trayexist\":1,\"ischecked\":1,\"isblank\":0,\"mediatype\":\"BD-RSRM\",\"label\":\"W20160125000001(30-2)\",\"slot_status\":78},{\"id\":50,\"cdexist\":1,\"trayexist\":1,\"ischecked\":1,\"isblank\":0,\"mediatype\":\"BD-RSRM\",\"label\":\"W20160125000001(30-1)\",\"slot_status\":78}]},{\"MagNo\":1,\"Rfid\":\"000009000120\",\"Slot\":[{\"id\":51,\"cdexist\":1,\"trayexist\":1,\"ischecked\":1,\"isblank\":0,\"mediatype\":\"DVD-ROM\",\"label\":\"W001(2-1)\",\"slot_status\":78},{\"id\":52,\"cdexist\":2,\"trayexist\":2,\"ischecked\":0,\"isblank\":0,\"mediatype\":\"\",\"label\":\"\",\"slot_status\":78},{\"id\":53,\"cdexist\":2,\"trayexist\":2,\"ischecked\":0,\"isblank\":0,\"mediatype\":\"\",\"label\":\"\",\"slot_status\":78},{\"id\":82,\"cdexist\":2,\"trayexist\":2,\"ischecked\":0,\"isblank\":0,\"mediatype\":\"\",\"label\":\"\",\"slot_status\":78}]}]}";
				//Machine machine =gson.fromJson(srcStr, Machine.class);
				Machine machine = commandQUERYSTATION(serverName);
				//logger.info("光盘位置刷新返回信息}",machine.toString());
				List<Mag> mags = machine.getMag();
				for (Mag mag : mags) {
					String rfid = mag.getRfid();
					if (StringUtils.isEmpty(rfid)) {
						continue;
					}
					rfid=rfid.trim();
					String magNo = (mag.getMagNo() +1)+ "";
					List<Slot> slots = mag.getSlot();
					for (Slot slotVo : slots) {
						String volabel = slotVo.getLabel();
						Integer soltId=slotVo.getId();
						if (null==volabel||"".equals(volabel.trim())) {
							continue;
						}
						logger.info("{}光盘位置定位盘槽号{}, 盘仓号{} 电子标签{}",volabel,soltId,magNo,rfid);
                      burnDao.updateDiscPosition(soltId+"", rfid, volabel.trim());
					}
				}

			
			} catch (Exception e) {
				logger.error("光盘信息更新失败{}", e.getMessage());
			
			}
	    }
			
			return "";
    }
    

}
