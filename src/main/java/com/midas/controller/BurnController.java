package com.midas.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.midas.constant.SysConstant;
import com.midas.enums.BurnState;
import com.midas.enums.ExportState;
import com.midas.exception.ServiceException;
import com.midas.service.BurnBusiness;
import com.midas.service.BurnService;
import com.midas.service.CommonService;
import com.midas.service.StandingbookService;
import com.midas.uitls.FtpUtil;
import com.midas.uitls.tools.CommonsUtils;
import com.midas.uitls.tools.ServletUtils;
import com.midas.uitls.tools.StringTools;
import com.midas.vo.FileVo;
import com.midas.vo.KeyValue;

/**
 * 刻录任务
 * 
 * @author arron
 *
 */
@Controller
public class BurnController extends BaseDataController {
    
    public static Map<String,List> searchMap =new HashMap<String,List>();

    private Logger      logger = LoggerFactory.getLogger(BurnController.class);
    @Autowired
    private BurnService burnService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private BurnBusiness business;
    @Autowired
    private StandingbookService standingbookService;
    @RequestMapping(value = "/burn/list")
    public String list(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = ServletUtils.getParameters(request);
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(getCurPage(map.get("pageNum")),
                SysConstant.PAGE_SIZE);
        PageInfo<Map<String, Object>> pageInfo = burnService.list(map, page);
        List<String> dirLists= getDir(null);
        request.setAttribute("dirLists", dirLists);
        request.setAttribute("pageInfo", pageInfo);
        request.setAttribute("burning_state", map.get("burning_state"));
        return "kepan/burn";
    }

    @RequestMapping(value = "/burn/cancel")
    public String cancal(HttpServletRequest request) {
        String mid = request.getParameter("mid");
        String volLabel = request.getParameter("volLabel");
        logger.info("用户取消刻录任务, volLabel: {}, mid : {}", volLabel, mid);
        burnService.updateState(volLabel, BurnState.CANCEL.getKey(), "手动取消", 0);
        return "redirect:/burn/list.do";
    }
    
    @RequestMapping(value="/burn/delete")
    public String delete(HttpServletRequest request) {
        String volLabel = request.getParameter("volLabel");
        String dataType = request.getParameter("dataType");
        logger.info("删除数据:{}, 数据类型： {}", volLabel, dataType);
        burnService.delete(volLabel, dataType);
        return "redirect:/burn/list.do";
    }

    /**
     * 获取光盘所在位置
     * 
     * @param volLabel
     * @return
     */
    @RequestMapping(value = "/burn/position")
    public String position(HttpServletRequest request, String volLabel) {

        try {
            volLabel = new String(volLabel.getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e1) {
        }

        
        List<String> dirLists= getDir(null);
        request.setAttribute("dirLists", dirLists);
        List<Map<String, Object>> list = burnService.listPosition(volLabel);
        request.setAttribute("list", list);
        request.setAttribute("volLabel", volLabel);
    
        
        return "kepan/position";
    }
    @RequestMapping(value = "/burn/downloadfile")
    public String downloadfile(HttpServletRequest request, String server,String filename,String volLabel,String slot,String findstr,String newfindstr) {

        List<String> dirLists= getDir(null);
        request.setAttribute("dirLists", dirLists);
        
        logger.info("server==={}", server);
        StringBuffer findstrtmp = new StringBuffer();
        if (!StringTools.isEmpty(slot)) {
            findstrtmp.append(slot + "_");
        }
        if (!StringTools.isEmpty(volLabel)) {
            findstrtmp.append("S000000006");
        }
        if (!StringTools.isEmpty(filename)) {
            findstrtmp.append("**" +filename.trim());
        }
        List<FileVo> fileList =null;
        if(StringTools.isNotEmpty(newfindstr)){
           fileList  = commonService.executeFindFileBySocket(server, newfindstr.toString());
           findstrtmp=new StringBuffer(newfindstr);
        }else{
            fileList  = commonService.executeFindFileBySocket(server, findstrtmp.toString());
        }
        logger.info("{}", findstrtmp.toString());
       
      
        
        searchMap.put(findstrtmp.toString(), fileList);
        
        if (fileList != null && fileList.size() > 0) {
            for (int i=0;i<fileList.size();i++) {
                FileVo filevo=fileList.get(i);
                filevo.setVol(volLabel);
                filevo.setId(i);
                
               if (filevo.getFilepath().lastIndexOf(".split")>0){
                   filevo.setSplitstatus("1"); //分割过
                   
                 int splitcount=Integer.parseInt(filevo.getFilepath().substring(filevo.getFilepath().length()-11, filevo.getFilepath().length()-10));
                   
                   filevo.setSplitcount(filevo.getFilepath().substring(filevo.getFilepath().length()-11, filevo.getFilepath().length()-10)) ;
                   //filevo.getFilepath().substring(filevo.getFilepath().length()-9, filevo.getFilepath().length()-6)
                   String splitNo= filevo.getFilepath().substring(filevo.getFilepath().length()-9, filevo.getFilepath().length()-6);
                   String names="";
                   for(int j=0;j<splitcount;j++){
                       String extname=filevo.getFilepath().substring(filevo.getFilepath().lastIndexOf("/")+1);
                    
                       String name="";
                       String splitename = String.format("%03d", i); 
                       name= extname.replace("_"+splitNo+".split", "_"+splitename+".split");
                       names+=","+name;
                       filevo.setSplitnames(names) ;
                   }
                   
             logger.info("split names:{}",names);    
                   
                 
               }else{
                   filevo.setSplitcount("0");
                   filevo.setSplitstatus("0"); //未分割过
               }
            //    _midas_2_001.split
                
            }
        }
        request.setAttribute("slotlist", getSlotList());  
        logger.info("sizs={}", fileList.size());
        request.setAttribute("fileList", fileList);
        request.setAttribute("findstr", findstrtmp.toString());
        request.setAttribute("server", server);
        request.setAttribute("volLabel", volLabel);
        return "kepan/downloadfile";
    }
    
    @RequestMapping(value = "/burn/mergefile", method = RequestMethod.GET)
    public String mergefile(String volLabel,String server,String fileselecttmp,String exportPath,String findstr) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("验证合并内容: {} 的唯一卷标内容,server:{},file{}.path:{}", volLabel, server, fileselecttmp, exportPath);
        Map<String, Object> servermap = commonService.getSystemParameters(server);
        boolean bool = false;
        String desc = "OK";
        
        List<FileVo> fileList=   searchMap.get(findstr.toString());
        
        String[] ids=null;
        if(StringTools.isNotEmpty(fileselecttmp)){
            
             ids=fileselecttmp.split(",");
        }
        
        FtpUtil ftpUtil = new FtpUtil();
        FTPClient client = ftpUtil.getConnectionFTP(servermap.get("sp_value1").toString(), 21, "admin", "jvcnas");
        try {
            for (String file : ids) {
                for (FileVo vo : fileList) {
                    if (vo.getId() == Integer.parseInt(file)) {
                        
                        String fName = vo.getFilepath();  
                        
                        String fileName = fName.substring(fName.lastIndexOf("/")+1);  
                        //或者  
                        String filepath ="/groups"+ fName.substring(0,fName.lastIndexOf("/"));  
                          
                        System.out.println("fileName = " + fileName);  
                        
                        ftpUtil.downFile(client, filepath, fileName, exportPath);
                    }

                }
            }
        } catch (ServiceException e) {
            desc = e.getMsg();
        }

        ftpUtil.closeFTP(client);
//        resultMap.put("result", bool);
//        resultMap.put("desc", desc);
        return "redirect:/burn/downloadfile.do?volLabel=" + volLabel+"&server="+server+"&newfindstr="+findstr;
    }
    /**
     * 检查是否允许合并数据
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/burn/mergeCheck", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> mergeCheck(String volLabel) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("验证合并内容: {} 的唯一卷标内容", volLabel);
        boolean bool = false;
        String desc = "OK";
        try {
            bool = burnService.checkMerge(volLabel);
        } catch (ServiceException e) {
            desc = e.getMsg();
        }
        resultMap.put("result", bool);
        resultMap.put("desc", desc);
        return resultMap;
    }

    
    @RequestMapping(value = "/burn/mergefileCheck", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> mergefileCheck(String volLabel) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        logger.info("验证合并内容: {} 的唯一卷标内容", volLabel);
        boolean bool = false;
        String desc = "OK";
        try {
            bool = true;
        } catch (ServiceException e) {
            desc = e.getMsg();
        }
        resultMap.put("result", bool);
        resultMap.put("desc", desc);
        return resultMap;
    }

    
    @RequestMapping(value = "/burn/merge")
    public String merge(HttpServletRequest request, String volLabel, String exportPath) {
        boolean bool = false;
        String desc = null;
        try {
            bool = business.masterMerge(volLabel, exportPath);
        } catch (ServiceException e) {
            logger.error("合并异常", e);
            desc = e.getMsg();
        }
        request.setAttribute("volLabel", volLabel);
        request.setAttribute("desc", desc);
        if (bool) {
            return "redirect:/burn/mergeList.do?volLabel=" + volLabel;
        }
        return "kepan/result";
    }
    
    @RequestMapping(value="/burn/exportCancel")
    public String mergeCancel(HttpServletRequest request) {
        String eid = request.getParameter("eid");
        String volume_label = request.getParameter("volume_label");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eid", eid);
        map.put("export_state", ExportState.MEGE_CANCEL.getKey());
        map.put("export_desc", "用户手动结束任务");
        burnService.updateExportRecord(map);
        return "redirect:/burn/mergeList.do?volLabel=" + volume_label;
    }

    @RequestMapping(value = "/burn/mergeList")
    public String mergeList(HttpServletRequest request, String volLabel,String task_name) {
        List<Map<String, Object>> list = burnService.listExportRecord(volLabel,task_name);
        request.setAttribute("list", list);
        request.setAttribute("volLabel", volLabel);
        request.setAttribute("task_name", task_name);
        return "kepan/export";
    }

    
    @RequestMapping(value="/burn/deleteExport")
    public String deleteExport(HttpServletRequest request) {
        String eid = request.getParameter("eid");
 
        logger.info("删除数据:{}", eid);
        burnService.deleteExport(eid);;
        return "redirect:/burn/mergeList.do";
    }
    
    @RequestMapping(value = "/burn/detailData")
    public String detailData(String dataType, String volLabel) {
        // /tData/list 二维
        // /dData/list 三维
        // /mData/list 中间
        // /rawData/list 原始数据
        String result = null;
        if ("R".equals(dataType)) {
            result = "/rawData/list";
        } else if ("T".equals(dataType)) {
            result = "/tData/list";
        } else if ("D".equals(dataType)) {
            result = "/dData/list";
        } else if ("M".equals(dataType)) {
            result = "/mData/list";
        }
        return "redirect:" + result + ".do?volume_label=" + volLabel;
    }
    @RequestMapping("/burn/openDir")
    @ResponseBody
    public List<String> openDir(String path) {
       
   
        
        String folderPath=path;
        
       List<String> dirList= getDir(folderPath);
            
        
            return  dirList;
 
     
       
    }
    
    public List<String> getDir(String folderPath){
        
        logger.info("当前路径为:", folderPath);
        if(StringUtils.isEmpty(folderPath)){
         try {
                folderPath=CommonsUtils.getPropertiesValue("defaltDir");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
       }
       
       File folderList=new File(folderPath);
       File list[]=folderList.listFiles();
       List<String> dirList=new ArrayList<String>();
       for(int i=0;i<list.length;i++)
        if(list[i].isDirectory()) 
            dirList.add(list[i].toString());
        
       return dirList;
    }
    
    public List<KeyValue> getSlotList(){
        
        List<KeyValue> list=new ArrayList<KeyValue>(); 
        for(int i=1;i<601;i++){
            String isoname = String.format("%04d", i); 
            list.add(new KeyValue(i,isoname));
        }
        return list;
    }
    
    
    public static void main(String[] args){
        
        String path="/groups/0204_W20160316000002(172-129)/古城东三维/W115569.001.SGY_midas_2_001.split";
        System.out.println(path.lastIndexOf(".split"));
        
        if(path.lastIndexOf(".split")>0){
            
            System.out.println(path.substring(path.length()-11, path.length()-10));
            System.out.println(path.substring(path.length()-9, path.length()-6));
            
        }
    }

}