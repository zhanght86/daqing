package com.midas.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;


import com.midas.constant.SysConstant;
import com.midas.context.SpringContextHelper;
import com.midas.enums.DataType;
import com.midas.service.StandingbookService;
import com.midas.uitls.date.DateStyle;
import com.midas.uitls.date.DateUtil;
import com.midas.uitls.tools.EnumUtils;
import com.midas.uitls.tools.ServletUtils;
import com.midas.uitls.tools.StringTools;
import com.midas.vo.StandingBook;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
@Controller
public class StandingbookController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StandingbookService service;

    @RequestMapping(value="/standingbook/rawData")
    public String rawStandingbook(HttpServletRequest request) {
        Map<String, Object> map = ServletUtils.getParameters(request);
        map.put("data_type", DataType.RAW_DATA.getType());
        map.put("states", "1");
        Page<?> page = new Page<>(1, SysConstant.PAGE_SIZE);
        PageInfo<Map<String, Object>> pageInfo = service.queryStandingbookListByPage(map, page);
        logger.info("获取到数据量为： {}", pageInfo);
        request.setAttribute("pageInfo", pageInfo);
        
        request.setAttribute("work_area", map.get("work_area"));
        request.setAttribute("construction_year", map.get("construction_year"));
        request.setAttribute("startDate", map.get("startDate"));
        request.setAttribute("endDate", map.get("endDate"));
        request.setAttribute("type", map.get("type"));
        return "standingbook/rawStandingbook";
    }
    
    @RequestMapping(value="/standingbook/tData")
    public String tStandingbook(HttpServletRequest request) {
        Map<String, Object> map = ServletUtils.getParameters(request);
        map.put("data_type", DataType.TWO_DATA.getType());
        map.put("states", "1");
        Page<?> page = new Page<>(1, SysConstant.PAGE_SIZE);
        PageInfo<Map<String, Object>> pageInfo = service.queryStandingbookListByPage(map, page);
        logger.info("获取到数据量为： {}", pageInfo);
        request.setAttribute("pageInfo", pageInfo);
        request.setAttribute("work_area", map.get("work_area"));
        request.setAttribute("construction_year", map.get("construction_year"));
        request.setAttribute("startDate", map.get("startDate"));
        request.setAttribute("endDate", map.get("endDate"));
        request.setAttribute("type", map.get("type"));
        return "standingbook/tStandingbook";
    }
    
    @RequestMapping(value="/standingbook/dData")
    public String dStandingbook(HttpServletRequest request) {
        Map<String, Object> map = ServletUtils.getParameters(request);
        map.put("data_type", DataType.THREE_DATA.getType());
        map.put("states", "1");
        Page<?> page = new Page<>(1, SysConstant.PAGE_SIZE);
        PageInfo<Map<String, Object>> pageInfo = service.queryStandingbookListByPage(map, page);
        logger.info("获取到数据量为： {}", pageInfo);
        request.setAttribute("pageInfo", pageInfo);
        request.setAttribute("work_area", map.get("work_area"));
        request.setAttribute("construction_year", map.get("construction_year"));
        request.setAttribute("startDate", map.get("startDate"));
        request.setAttribute("endDate", map.get("endDate"));
        request.setAttribute("type", map.get("type"));
        return "standingbook/dStandingbook";
    }
    @RequestMapping(value="/standingbook/mData")
    public String mStandingbook(HttpServletRequest request) {
        Map<String, Object> map = ServletUtils.getParameters(request);
        map.put("data_type", DataType.MIDDLE_DATA.getType());
        map.put("states", "1");
        Page<?> page = new Page<>(1, SysConstant.PAGE_SIZE);
        PageInfo<Map<String, Object>> pageInfo = service.queryStandingbookListByPage(map, page);
        logger.info("获取到数据量为： {}", pageInfo);
        request.setAttribute("pageInfo", pageInfo);
        request.setAttribute("work_area", map.get("work_area"));
        request.setAttribute("construction_year", map.get("construction_year"));
        request.setAttribute("startDate", map.get("startDate"));
        request.setAttribute("endDate", map.get("endDate"));
        request.setAttribute("type", map.get("type"));
        
        return "standingbook/mStandingbook";
    }
    
    
    
    @RequestMapping(value="/standingbook/rawExcel")
    public String rawExcel(HttpServletRequest request, HttpServletResponse response)throws Exception {
        Map<String, Object> map = ServletUtils.getParameters(request);
    
        
        
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("work_area", map.get("work_area"));
        paramMap.put("construction_year", formatDate(ObjectUtils.toString(map.get("construction_year"))));
        paramMap.put("startDate", map.get("startDate"));
        paramMap.put("endDate", map.get("endDate"));
        paramMap.put("type", map.get("type"));
        logger.info("查询条件为： {}", paramMap);
        paramMap.put("data_type", DataType.RAW_DATA.getType());
        paramMap.put("states", "1");
        
        logger.info("查询条件为： {}",  paramMap);
        
        Page<?> page = new Page<>(1, SysConstant.PAGE_SIZE);
        PageInfo<Map<String, Object>> pageInfo = service.queryStandingbookListByPage(paramMap, page);
        logger.info("获取到数据量为： {}", pageInfo);
        
        
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.addAll(pageInfo.getList());

        OutputStream os = response.getOutputStream();// 取得输出流
        response.reset();// 清空输出流
        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode("原始数据清单.xls", "utf-8"));// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型

        WritableWorkbook wbook = null;
        try {
            wbook = Workbook.createWorkbook(os); // 建立excel文件
            String tmptitle = "原始数据清单"; // 标题
            WritableSheet wsheet = wbook.createSheet(tmptitle, 0); // sheet名称
            // 设置excel标题
            // WritableFont wfont = new WritableFont(WritableFont.ARIAL,
            // 16,WritableFont.BOLD,
            // false,UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
            // WritableCellFormat wcfFC = new WritableCellFormat(wfont);
            // wcfFC.setBackground(Colour.AQUA);
            // wsheet.addCell(new Label(1, 0, tmptitle, wcfFC));
            // wfont = new jxl.write.WritableFont(WritableFont.ARIAL,
            // 14,WritableFont.BOLD,
            // false, UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
            // wcfFC = new WritableCellFormat(wfont);
            // setAlignment(jxl.format.Alignment.CENTRE);
//            WritableCellFormat wcf = new WritableCellFormat();
//            wcf.setAlignment(jxl.format.Alignment.CENTRE);
//            Label l = new Label(0, 0, "原始数据清单", wcf);
//            wsheet.mergeCells(0, 0, 10, 0);
//            wsheet.addCell(l);

            // 开始生成主体内容
            wsheet.addCell(new Label(0, 0, "编号"));
            wsheet.addCell(new Label(1, 0, "项目名"));
            wsheet.addCell(new Label(2, 0, "卷标号"));
            wsheet.addCell(new Label(3, 0, "归档日期"));
            wsheet.addCell(new Label(4, 0, "处理单位"));
            wsheet.addCell(new Label(5, 0, "数据大小(MB)"));
            wsheet.addCell(new Label(6, 0, "数据大小(GB)"));
            wsheet.addCell(new Label(7, 0, "刻盘数量"));
            wsheet.addCell(new Label(8, 0, "开始刻录"));
            wsheet.addCell(new Label(9, 0, "刻录结束时间"));
            wsheet.addCell(new Label(10, 0, "刻录用时(小时)"));
            wsheet.addCell(new Label(11, 0, "类型"));

            for (int j = 0; j < list.size(); j++) {
                wsheet.addCell(new Label(0, j + 1, ObjectUtils.toString(list.get(j).get("sid"))));
                wsheet.addCell(new Label(1, j + 1, ObjectUtils.toString(list.get(j).get("work_area"))));
             
                
                
                wsheet.addCell(new Label(2, j + 1, ObjectUtils.toString(list.get(j).get("volume_label"))));
                
                wsheet.addCell(new Label(3, j + 1, ObjectUtils.toString(list.get(j).get("construction_year"))));
                wsheet.addCell(new Label(4, j + 1, ObjectUtils.toString(list.get(j).get("construction_unit"))));
                
                long   data_quantity= (long) list.get(j).get("data_quantity") ; 
                    
                int mb=1024*1024;
                int gbb=1024*1024*1024;
                BigInteger mb1=new BigInteger(mb+"");
                BigInteger gb=new BigInteger(gbb+"");
                wsheet.addCell(new Label(5, j + 1, ObjectUtils.toString(data_quantity/1024/1024)));
                wsheet.addCell(new Label(6, j + 1, ObjectUtils.toString(data_quantity/1024/1024/1024)));
                wsheet.addCell(new Label(7, j + 1, ObjectUtils.toString(list.get(j).get("burn_count"))));
                
                wsheet.addCell(new Label(8, j + 1, ObjectUtils.toString(list.get(j).get("create_time"))));
                wsheet.addCell(new Label(9, j + 1, ObjectUtils.toString(list.get(j).get("update_time"))));
                
           
                Date create_time=(Date) list.get(j).get("create_time");
                Date update_time= (Date)list.get(j).get("update_time");
                logger.info("获取create_time 为： {},update_time={}",  create_time,update_time);
//                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.d");
               if(StringTools.isNotEmpty(create_time)&& StringTools.isNotEmpty(update_time) ){
//                    Date create_time1=sdf.parse(create_time);
//                    Date end_time= sdf.parse(update_time);
//              
                wsheet.addCell(new Label(10, j + 1, ObjectUtils.toString((create_time.getTime()- update_time.getTime())/1000/60/60 )));
                }else{
                    wsheet.addCell(new Label(10, j + 1, ObjectUtils.toString("")));
                    
                }
                
               String type= ObjectUtils.toString(list.get(j).get("type"));
               wsheet.addCell(new Label(11, j + 1, type.equals("1")?"刻录":"导出"));
           
          
               
            }
            // 主体内容生成结束
            wbook.write(); // 写入文件
        } catch (Exception e) {
            logger.error("下载Excel失败", e);
        } finally {
            if (null != wbook) {
                wbook.close();
            }
            if (os != null) {
                os.close(); // 关闭流
            }
        }
        
        
        
        
        
        
        request.setAttribute("pageInfo", pageInfo);
        
        request.setAttribute("work_area", map.get("work_area"));
        request.setAttribute("construction_year", map.get("construction_year"));
        request.setAttribute("startDate", map.get("startDate"));
        request.setAttribute("endDate", map.get("endDate"));
        request.setAttribute("type", map.get("type"));
        return "standingbook/rawStandingbook";
    }
    
    @RequestMapping(value="/standingbook/tExcel")
    public String tExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = ServletUtils.getParameters(request);
        
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("work_area", map.get("work_area"));
        paramMap.put("construction_year", formatDate(ObjectUtils.toString(map.get("construction_year"))));
        paramMap.put("startDate", map.get("startDate"));
        paramMap.put("endDate", map.get("endDate"));
        paramMap.put("type", map.get("type"));
        logger.info("查询条件为： {}", paramMap);
        paramMap.put("data_type", DataType.TWO_DATA.getType());
        paramMap.put("states", "1");
        Page<?> page = new Page<>(1, SysConstant.PAGE_SIZE);
        PageInfo<Map<String, Object>> pageInfo = service.queryStandingbookListByPage(paramMap, page);
        logger.info("获取到数据量为： {}", pageInfo);
        
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.addAll(pageInfo.getList());

        OutputStream os = response.getOutputStream();// 取得输出流
        response.reset();// 清空输出流
        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode("原始数据清单.xls", "utf-8"));// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型

        WritableWorkbook wbook = null;
        try {
            wbook = Workbook.createWorkbook(os); // 建立excel文件
            String tmptitle = "二维数据清单"; // 标题
            WritableSheet wsheet = wbook.createSheet(tmptitle, 0); // sheet名称
            // 设置excel标题
            // WritableFont wfont = new WritableFont(WritableFont.ARIAL,
            // 16,WritableFont.BOLD,
            // false,UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
            // WritableCellFormat wcfFC = new WritableCellFormat(wfont);
            // wcfFC.setBackground(Colour.AQUA);
            // wsheet.addCell(new Label(1, 0, tmptitle, wcfFC));
            // wfont = new jxl.write.WritableFont(WritableFont.ARIAL,
            // 14,WritableFont.BOLD,
            // false, UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
            // wcfFC = new WritableCellFormat(wfont);
            // setAlignment(jxl.format.Alignment.CENTRE);
//            WritableCellFormat wcf = new WritableCellFormat();
//            wcf.setAlignment(jxl.format.Alignment.CENTRE);
//            Label l = new Label(0, 0, "原始数据清单", wcf);
//            wsheet.mergeCells(0, 0, 10, 0);
//            wsheet.addCell(l);

            // 开始生成主体内容
            wsheet.addCell(new Label(0, 0, "编号"));
            wsheet.addCell(new Label(1, 0, "项目名"));
            wsheet.addCell(new Label(2, 0, "卷标号"));
            wsheet.addCell(new Label(3, 0, "归档日期"));
            wsheet.addCell(new Label(4, 0, "处理单位"));
            wsheet.addCell(new Label(5, 0, "数据大小(MB)"));
            wsheet.addCell(new Label(6, 0, "数据大小(GB)"));
            wsheet.addCell(new Label(7, 0, "刻盘数量"));
            wsheet.addCell(new Label(8, 0, "开始刻录"));
            wsheet.addCell(new Label(9, 0, "刻录结束时间"));
            wsheet.addCell(new Label(10, 0, "刻录用时(小时)"));
            wsheet.addCell(new Label(11, 0, "类型"));

            for (int j = 0; j < list.size(); j++) {
                wsheet.addCell(new Label(0, j + 1, ObjectUtils.toString(list.get(j).get("sid"))));
                wsheet.addCell(new Label(1, j + 1, ObjectUtils.toString(list.get(j).get("work_area"))));
             
                
                
                wsheet.addCell(new Label(2, j + 1, ObjectUtils.toString(list.get(j).get("volume_label"))));
                
                wsheet.addCell(new Label(3, j + 1, ObjectUtils.toString(list.get(j).get("construction_year"))));
                wsheet.addCell(new Label(4, j + 1, ObjectUtils.toString(list.get(j).get("construction_unit"))));
                
                long   data_quantity= (long) list.get(j).get("data_quantity") ; 
                    
                int mb=1024*1024;
                int gbb=1024*1024*1024;
                BigInteger mb1=new BigInteger(mb+"");
                BigInteger gb=new BigInteger(gbb+"");
                wsheet.addCell(new Label(5, j + 1, ObjectUtils.toString(data_quantity/1024/1024)));
                wsheet.addCell(new Label(6, j + 1, ObjectUtils.toString(data_quantity/1024/1024/1024)));
                wsheet.addCell(new Label(7, j + 1, ObjectUtils.toString(list.get(j).get("burn_count"))));
                
                wsheet.addCell(new Label(8, j + 1, ObjectUtils.toString(list.get(j).get("create_time"))));
                wsheet.addCell(new Label(9, j + 1, ObjectUtils.toString(list.get(j).get("update_time"))));
                
           
                Date create_time=(Date) list.get(j).get("create_time");
                Date update_time= (Date)list.get(j).get("update_time");
                logger.info("获取create_time 为： {},update_time={}",  create_time,update_time);
//                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.d");
               if(StringTools.isNotEmpty(create_time)&& StringTools.isNotEmpty(update_time) ){
//                    Date create_time1=sdf.parse(create_time);
//                    Date end_time= sdf.parse(update_time);
//              
                wsheet.addCell(new Label(10, j + 1, ObjectUtils.toString((create_time.getTime()- update_time.getTime())/1000/60/60 )));
                }else{
                    wsheet.addCell(new Label(10, j + 1, ObjectUtils.toString("")));
                    
                }
                
       
               String type= ObjectUtils.toString(list.get(j).get("type"));
               wsheet.addCell(new Label(11, j + 1, type.equals("1")?"刻录":"导出"));
               
            }
            // 主体内容生成结束
            wbook.write(); // 写入文件
        } catch (Exception e) {
            logger.error("下载Excel失败", e);
        } finally {
            if (null != wbook) {
                wbook.close();
            }
            if (os != null) {
                os.close(); // 关闭流
            }
        }
        
        request.setAttribute("pageInfo", pageInfo);
        request.setAttribute("work_area", map.get("work_area"));
        request.setAttribute("construction_year", map.get("construction_year"));
        request.setAttribute("startDate", map.get("startDate"));
        request.setAttribute("endDate", map.get("endDate"));
        request.setAttribute("type", map.get("type"));
        return "standingbook/tStandingbook";
    }
    
    @RequestMapping(value="/standingbook/dExcel")
    public String dExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = ServletUtils.getParameters(request);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("work_area", map.get("work_area"));
        paramMap.put("construction_year", formatDate(ObjectUtils.toString(map.get("construction_year"))));
        paramMap.put("startDate", map.get("startDate"));
        paramMap.put("endDate", map.get("endDate"));
        paramMap.put("type", map.get("type"));
        logger.info("查询条件为： {}", paramMap);
        paramMap.put("data_type", DataType.THREE_DATA.getType());
        paramMap.put("states", "1");
        Page<?> page = new Page<>(1, SysConstant.PAGE_SIZE);
        PageInfo<Map<String, Object>> pageInfo = service.queryStandingbookListByPage(paramMap, page);
        logger.info("获取到数据量为： {}", pageInfo);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.addAll(pageInfo.getList());

        OutputStream os = response.getOutputStream();// 取得输出流
        response.reset();// 清空输出流
        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode("原始数据清单.xls", "utf-8"));// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型

        WritableWorkbook wbook = null;
        try {
            wbook = Workbook.createWorkbook(os); // 建立excel文件
            String tmptitle = "三维数据清单"; // 标题
            WritableSheet wsheet = wbook.createSheet(tmptitle, 0); // sheet名称
            // 设置excel标题
            // WritableFont wfont = new WritableFont(WritableFont.ARIAL,
            // 16,WritableFont.BOLD,
            // false,UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
            // WritableCellFormat wcfFC = new WritableCellFormat(wfont);
            // wcfFC.setBackground(Colour.AQUA);
            // wsheet.addCell(new Label(1, 0, tmptitle, wcfFC));
            // wfont = new jxl.write.WritableFont(WritableFont.ARIAL,
            // 14,WritableFont.BOLD,
            // false, UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
            // wcfFC = new WritableCellFormat(wfont);
            // setAlignment(jxl.format.Alignment.CENTRE);
//            WritableCellFormat wcf = new WritableCellFormat();
//            wcf.setAlignment(jxl.format.Alignment.CENTRE);
//            Label l = new Label(0, 0, "原始数据清单", wcf);
//            wsheet.mergeCells(0, 0, 10, 0);
//            wsheet.addCell(l);

            // 开始生成主体内容
            wsheet.addCell(new Label(0, 0, "编号"));
            wsheet.addCell(new Label(1, 0, "项目名"));
            wsheet.addCell(new Label(2, 0, "卷标号"));
            wsheet.addCell(new Label(3, 0, "归档日期"));
            wsheet.addCell(new Label(4, 0, "处理单位"));
            wsheet.addCell(new Label(5, 0, "数据大小(MB)"));
            wsheet.addCell(new Label(6, 0, "数据大小(GB)"));
            wsheet.addCell(new Label(7, 0, "刻盘数量"));
            wsheet.addCell(new Label(8, 0, "开始刻录"));
            wsheet.addCell(new Label(9, 0, "刻录结束时间"));
            wsheet.addCell(new Label(10, 0, "刻录用时(小时)"));
            wsheet.addCell(new Label(11, 0, "类型"));

            for (int j = 0; j < list.size(); j++) {
                wsheet.addCell(new Label(0, j + 1, ObjectUtils.toString(list.get(j).get("sid"))));
                wsheet.addCell(new Label(1, j + 1, ObjectUtils.toString(list.get(j).get("work_area"))));
             
                
                
                wsheet.addCell(new Label(2, j + 1, ObjectUtils.toString(list.get(j).get("volume_label"))));
                
                wsheet.addCell(new Label(3, j + 1, ObjectUtils.toString(list.get(j).get("construction_year"))));
                wsheet.addCell(new Label(4, j + 1, ObjectUtils.toString(list.get(j).get("construction_unit"))));
                
                long   data_quantity= (long) list.get(j).get("data_quantity") ; 
                    
                int mb=1024*1024;
                int gbb=1024*1024*1024;
                BigInteger mb1=new BigInteger(mb+"");
                BigInteger gb=new BigInteger(gbb+"");
                wsheet.addCell(new Label(5, j + 1, ObjectUtils.toString(data_quantity/1024/1024)));
                wsheet.addCell(new Label(6, j + 1, ObjectUtils.toString(data_quantity/1024/1024/1024)));
                wsheet.addCell(new Label(7, j + 1, ObjectUtils.toString(list.get(j).get("burn_count"))));
                
                wsheet.addCell(new Label(8, j + 1, ObjectUtils.toString(list.get(j).get("create_time"))));
                wsheet.addCell(new Label(9, j + 1, ObjectUtils.toString(list.get(j).get("update_time"))));
                
           
                Date create_time=(Date) list.get(j).get("create_time");
                Date update_time= (Date)list.get(j).get("update_time");
                logger.info("获取create_time 为： {},update_time={}",  create_time,update_time);
//                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.d");
               if(StringTools.isNotEmpty(create_time)&& StringTools.isNotEmpty(update_time) ){
//                    Date create_time1=sdf.parse(create_time);
//                    Date end_time= sdf.parse(update_time);
//              
                wsheet.addCell(new Label(10, j + 1, ObjectUtils.toString((create_time.getTime()- update_time.getTime())/1000/60/60 )));
                }else{
                    wsheet.addCell(new Label(10, j + 1, ObjectUtils.toString("")));
                    
                }
                
               String type= ObjectUtils.toString(list.get(j).get("type"));
               wsheet.addCell(new Label(11, j + 1, type.equals("1")?"刻录":"导出"));
           
          
               
            }
            // 主体内容生成结束
            wbook.write(); // 写入文件
        } catch (Exception e) {
            logger.error("下载Excel失败", e);
        } finally {
            if (null != wbook) {
                wbook.close();
            }
            if (os != null) {
                os.close(); // 关闭流
            }
        }
        request.setAttribute("pageInfo", pageInfo);
        request.setAttribute("work_area", map.get("work_area"));
        request.setAttribute("construction_year", map.get("construction_year"));
        request.setAttribute("startDate", map.get("startDate"));
        request.setAttribute("endDate", map.get("endDate"));
        request.setAttribute("type", map.get("type"));
        return "standingbook/dStandingbook";
    }
    @RequestMapping(value="/standingbook/mExcel")
    public String mExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = ServletUtils.getParameters(request);
        
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("work_area", map.get("work_area"));
        paramMap.put("construction_year", formatDate(ObjectUtils.toString(map.get("construction_year"))));
        paramMap.put("startDate", map.get("startDate"));
        paramMap.put("endDate", map.get("endDate"));
        paramMap.put("type", map.get("type"));
        logger.info("查询条件为： {}", paramMap);
        
        paramMap.put("data_type", DataType.MIDDLE_DATA.getType());
        paramMap.put("states", "1");
        Page<?> page = new Page<>(1, SysConstant.PAGE_SIZE);
        PageInfo<Map<String, Object>> pageInfo = service.queryStandingbookListByPage(paramMap, page);
        logger.info("获取到数据量为： {}", pageInfo);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.addAll(pageInfo.getList());

        OutputStream os = response.getOutputStream();// 取得输出流
        response.reset();// 清空输出流
        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode("原始数据清单.xls", "utf-8"));// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型

        WritableWorkbook wbook = null;
        try {
            wbook = Workbook.createWorkbook(os); // 建立excel文件
            String tmptitle = "中间数据清单"; // 标题
            WritableSheet wsheet = wbook.createSheet(tmptitle, 0); // sheet名称
            // 设置excel标题
            // WritableFont wfont = new WritableFont(WritableFont.ARIAL,
            // 16,WritableFont.BOLD,
            // false,UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
            // WritableCellFormat wcfFC = new WritableCellFormat(wfont);
            // wcfFC.setBackground(Colour.AQUA);
            // wsheet.addCell(new Label(1, 0, tmptitle, wcfFC));
            // wfont = new jxl.write.WritableFont(WritableFont.ARIAL,
            // 14,WritableFont.BOLD,
            // false, UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
            // wcfFC = new WritableCellFormat(wfont);
            // setAlignment(jxl.format.Alignment.CENTRE);
//            WritableCellFormat wcf = new WritableCellFormat();
//            wcf.setAlignment(jxl.format.Alignment.CENTRE);
//            Label l = new Label(0, 0, "原始数据清单", wcf);
//            wsheet.mergeCells(0, 0, 10, 0);
//            wsheet.addCell(l);

            // 开始生成主体内容
            wsheet.addCell(new Label(0, 0, "编号"));
            wsheet.addCell(new Label(1, 0, "项目名"));
            wsheet.addCell(new Label(2, 0, "卷标号"));
            wsheet.addCell(new Label(3, 0, "归档日期"));
            wsheet.addCell(new Label(4, 0, "处理单位"));
            wsheet.addCell(new Label(5, 0, "数据大小(MB)"));
            wsheet.addCell(new Label(6, 0, "数据大小(GB)"));
            wsheet.addCell(new Label(7, 0, "刻盘数量"));
            wsheet.addCell(new Label(8, 0, "开始刻录"));
            wsheet.addCell(new Label(9, 0, "刻录结束时间"));
            wsheet.addCell(new Label(10, 0, "刻录用时(小时)"));
            wsheet.addCell(new Label(11, 0, "类型"));

            for (int j = 0; j < list.size(); j++) {
                wsheet.addCell(new Label(0, j + 1, ObjectUtils.toString(list.get(j).get("sid"))));
                wsheet.addCell(new Label(1, j + 1, ObjectUtils.toString(list.get(j).get("work_area"))));
             
                
                
                wsheet.addCell(new Label(2, j + 1, ObjectUtils.toString(list.get(j).get("volume_label"))));
                
                wsheet.addCell(new Label(3, j + 1, ObjectUtils.toString(list.get(j).get("construction_year"))));
                wsheet.addCell(new Label(4, j + 1, ObjectUtils.toString(list.get(j).get("construction_unit"))));
                
                long   data_quantity= (long) list.get(j).get("data_quantity") ; 
                    
                int mb=1024*1024;
                int gbb=1024*1024*1024;
                BigInteger mb1=new BigInteger(mb+"");
                BigInteger gb=new BigInteger(gbb+"");
                wsheet.addCell(new Label(5, j + 1, ObjectUtils.toString(data_quantity/1024/1024)));
                wsheet.addCell(new Label(6, j + 1, ObjectUtils.toString(data_quantity/1024/1024/1024)));
                wsheet.addCell(new Label(7, j + 1, ObjectUtils.toString(list.get(j).get("burn_count"))));
                
                wsheet.addCell(new Label(8, j + 1, ObjectUtils.toString(list.get(j).get("create_time"))));
                wsheet.addCell(new Label(9, j + 1, ObjectUtils.toString(list.get(j).get("update_time"))));
                
           
                Date create_time=(Date) list.get(j).get("create_time");
                Date update_time= (Date)list.get(j).get("update_time");
                logger.info("获取create_time 为： {},update_time={}",  create_time,update_time);
//                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.d");
               if(StringTools.isNotEmpty(create_time)&& StringTools.isNotEmpty(update_time) ){
//                    Date create_time1=sdf.parse(create_time);
//                    Date end_time= sdf.parse(update_time);
//              
                wsheet.addCell(new Label(10, j + 1, ObjectUtils.toString((create_time.getTime()- update_time.getTime())/1000/60/60 )));
                }else{
                    wsheet.addCell(new Label(10, j + 1, ObjectUtils.toString("")));
                    
                }
                
       
              String type= ObjectUtils.toString(list.get(j).get("type"));
              wsheet.addCell(new Label(11, j + 1, type.equals("1")?"刻录":"导出"));
          
               
            }
            // 主体内容生成结束
            wbook.write(); // 写入文件
        } catch (Exception e) {
            logger.error("下载Excel失败", e);
        } finally {
            if (null != wbook) {
                wbook.close();
            }
            if (os != null) {
                os.close(); // 关闭流
            }
        }
        request.setAttribute("pageInfo", pageInfo);
        request.setAttribute("work_area", map.get("work_area"));
        
        request.setAttribute("construction_year", map.get("construction_year"));
        request.setAttribute("startDate", map.get("startDate"));
        request.setAttribute("endDate", map.get("endDate"));
        request.setAttribute("type", map.get("type"));
        return "standingbook/mStandingbook";
    }
    
    
    
    
    @RequestMapping(value = "/standingbook/forInsert")
    public String forInsert(HttpServletRequest request, String url) {
       
        return "standingbook/standingbookInsert";
    }
    
    @RequestMapping(value = "/standingbook/insert")
    public String insert(HttpServletRequest request, String url) {
      
          String sid=request.getParameter("sid");
          String volume_label=request.getParameter("volume_label");//卷标号
          String data_type=request.getParameter("data_type");//数据类型
          logger.info("获取到数据类型为： {}", data_type);
          String work_area =request.getParameter("work_area");//工区
          String construction_unit=request.getParameter("construction_unit") ;//单位
          String construction_year=request.getParameter("construction_year");//日期
          String states =request.getParameter("states") ;//状态
          String data_quantity =request.getParameter("data_quantity");//数据大小
          String  burn_count=request.getParameter("burn_count");//刻盘数量
          SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
          String  create_time= sdf.format(new Date());//创建时间
          String update_time =request.getParameter("update_time");//刻盘耗时
        StandingBook vo=new StandingBook();
        
        
//        vo.setSid(Integer.parseInt(sid));
        vo.setData_type(data_type);
        vo.setBurn_count(burn_count);
        vo.setConstruction_unit(construction_unit);
        vo.setConstruction_year(construction_year);
        vo.setCreate_time(create_time);
        vo.setData_quantity(Integer.parseInt(data_quantity));
        vo.setStates(states);
        vo.setUpdate_time(update_time);
        vo.setVolume_label(volume_label);
        vo.setWork_area(work_area);
        volume_label=request.getParameter("volume_label");//卷标号
//       
        
        service.save(vo);
        System.out.println(">>>" + url);
        return "standingbook/rawStandingbook";
    }
    
    @RequestMapping(value = "/standingbook/delete")
    public String delete(String sid, String dataType, String url) {
//        setDataService(dataType);
        service.delete(sid);
 
        return "redirect:/" + url;
    }
//    public void setDataService(String dataType) {
//        DataType dataTypeEnum = (DataType) EnumUtils.convertEnum(DataType.class, dataType);
//        this.service = SpringContextHelper.getBean(dataTypeEnum.getBean(), StandingbookService.class);
//    }
    private static String formatDate(String date) {
        String result = null;
        if (null != date && !"".equals(date)) {
            String[] dates = date.split("[^\\d]");
            List<Object> list = new ArrayList<Object>();
            for (String s : dates) {
                if (StringTools.isNotEmpty(s)) {
                    try {
                        int i = Integer.parseInt(s);
                        list.add(i);
                    } catch (Exception e) {
                        list.add(s);
                    }
                }
            }
            result = "%" + StringUtils.join(list, "%") + "%";
            // if (dates.length > 1) {
            // String year = dates[0].substring(2, 4);
            // String month = dates[1].replace("0", "");
            // return "%" + year + "%" + month;
            // } else {
            // return "%" + date + "%";
            // }
        }
        return result;
    }

}
