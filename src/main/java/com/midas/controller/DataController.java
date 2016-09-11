package com.midas.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.codehaus.janino.util.StringPattern;
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
import com.jcraft.jsch.Session;
import com.midas.constant.SysConstant;
import com.midas.context.SpringContextHelper;
import com.midas.enums.BurnState;
import com.midas.enums.DataType;
import com.midas.exception.ServiceException;
import com.midas.service.ApplicationDataService;
import com.midas.service.BurnBusiness;
import com.midas.service.BurnService;
import com.midas.service.CommonService;
import com.midas.service.DataService;
import com.midas.uitls.tools.CommonsUtils;
import com.midas.uitls.tools.EnumUtils;
import com.midas.uitls.tools.ServletUtils;
import com.midas.uitls.tools.StringTools;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 备份数据操作
 * 
 * @author arron
 *
 */
@Controller
public class DataController extends BaseDataController {

    private Logger logger = LoggerFactory.getLogger(DataController.class);

    @Autowired
    @Qualifier("rDataService")
    private DataService   rawDataService;
    @Autowired
    @Qualifier("tDataService")
    private DataService   tDataService;
    @Autowired
    @Qualifier("dDataService")
    private DataService   dDataService;
    @Autowired
    @Qualifier("mDataService")
    private DataService   mDataService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private BurnBusiness  burnBusiness;
    @Autowired
    private ApplicationDataService applicationDataService;

    private DataService dataService;
    
    @Autowired
    private BurnService  burnService;

    /**
     * 原始数据列表
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/rawData/list")
    public String rawData(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        Map<String, Object> map = ServletUtils.getParameters(request);
        logger.info("原始数据清单， 请求参数内容为： {}", map);

        Page<?> page = new Page<Object>(getCurPage(map.get("pageNum")), SysConstant.PAGE_SIZE);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("work_area", map.get("work_area"));
        paramMap.put("construction_year", formatDate(ObjectUtils.toString(map.get("construction_year"))));
        paramMap.put("volume_label", map.get("volume_label"));
        logger.info("查询条件为： {}", paramMap);
        PageInfo<Map<String, Object>> pageInfo = rawDataService.list(paramMap, page);
        List<Map<String, Object>> discTypes = commonService.listSystemParameters(SysConstant.DISC_TYPE);
        request.setAttribute("discType", discTypes);
        request.setAttribute("work_area", map.get("work_area"));
        request.setAttribute("construction_year", map.get("construction_year"));
        request.setAttribute("pageInfo", pageInfo);
        List<String> dirLists= getDir(null);
        request.setAttribute("dirLists", dirLists);
        
        Enumeration paramNames = request.getParameterNames();
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            Object [] values = request.getParameterValues(paramName);
            if (values == null || values.length == 0) {
                // Do nothing, no values found at all.
            } else {
                request.setAttribute(paramName, values[0]);
            }
        }
        
        
        return "data/rawlist";
    }
    
    
    @RequestMapping(value = "/data/applicationlist")
    public String applicationlist(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        Map<String, Object> map = ServletUtils.getParameters(request);
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(getCurPage(map.get("pageNum")),
                SysConstant.PAGE_SIZE);
        map.put("burning_state", BurnState.BURN_SUCCESS);
        PageInfo<Map<String, Object>> pageInfo = burnService.list(map, page);
        List<String> dirLists= getDir(null);
        request.setAttribute("dirLists", dirLists);
        request.setAttribute("pageInfo", pageInfo);
        request.setAttribute("burning_state", map.get("burning_state"));
        
        
        return "data/listToApplication";
    }
    
    
    @RequestMapping(value = "/data/applicationQuery")
    public String applicationQuery(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        Map<String, Object> map = ServletUtils.getParameters(request);
        logger.info("原始数据清单， 请求参数内容为： {}", map);

        Page<?> page = new Page<Object>(getCurPage(map.get("pageNum")), SysConstant.PAGE_SIZE);
       
      
        PageInfo<Map<String, Object>> pageInfo = applicationDataService.list(map, page);
        List<Map<String, Object>> discTypes = commonService.listSystemParameters(SysConstant.DISC_TYPE);

        request.setAttribute("pageInfo", pageInfo);
    //    List<String> dirLists= getDir(null);
      //  request.setAttribute("dirLists", dirLists);
            return "data/listApplication";
    }
    
    
    @RequestMapping(value = "/data/ApplicationData")
    public String ApplicationData(HttpServletRequest request, String volLabel,String reMark,String phone,String date ,HttpServletResponse response)
            throws UnsupportedEncodingException {
        Map<String, Object> map = ServletUtils.getParameters(request);
        logger.info("原始数据清单， 请求参数内容为： {}", map);
        try {
        	Subject currentUser = SecurityUtils.getSubject();
        	String[] volary=volLabel.split(",");
        	for (String vol : volary) {
        		map.put("volLabel",vol);
        		map.put("type", "0");
            	map.put("user",currentUser.getPrincipal());
        		applicationDataService.insertApplication(map);
			}
      
		} catch (Exception e) {
			logger.error("申请数据保存失败",e.getMessage());
		
			
		}

	  request.setAttribute("desc", "获取数据申请已经提交成功，请等待管理员审核！");
	  request.setAttribute("backUrl", "/data/applicationlist.do");
		return "kepan/success";
        //return "data/rawlistApplication";
    }
    
    
    @RequestMapping(value = "/data/ApplicationUpdate")
    public String ApplicationUpdate(HttpServletRequest request, String volLabel,String reMark,String phone,String date ,HttpServletResponse response)
            throws UnsupportedEncodingException {
        Map<String, Object> map = ServletUtils.getParameters(request);
       
        
        		try {
					applicationDataService.updateApplication(map);
				} catch (Exception e) {
					logger.error("同意数据申请失败"+e);
					 request.setAttribute("desc", "同意数据申请失败"+e);
					
				}
	 
		return "redirect:/data/applicationQuery.do";
        //return "data/rawlistApplication";
    }
    

    @RequestMapping(value = "/rawData/excel")
    public void rawDataExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = ServletUtils.getParameters(request);
        logger.info("原始数据清单， 请求参数内容为： {}", map);

        Page<?> page = new Page<Object>(1, 2);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("work_area", map.get("work_area"));
        paramMap.put("construction_year", formatDate(ObjectUtils.toString(map.get("construction_year"))));
        paramMap.put("volume_label", map.get("volume_label"));
        logger.info("查询条件为： {}", paramMap);
        PageInfo<Map<String, Object>> pageInfo = rawDataService.list(paramMap, page);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.addAll(pageInfo.getList());
        int i = pageInfo.getPages();
        for (int j = 2; j <= i; j++) {
            page.setPageNum(j);
            list.addAll(rawDataService.list(paramMap, page).getList());
        }

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
            wsheet.addCell(new Label(0, 0, "工区"));
            wsheet.addCell(new Label(1, 0, "测线（束）号"));
            wsheet.addCell(new Label(2, 0, "起止序号"));
            wsheet.addCell(new Label(3, 0, "记录长度"));
            wsheet.addCell(new Label(4, 0, "采用间隔（ms）"));
            wsheet.addCell(new Label(5, 0, "磁带编号"));
            wsheet.addCell(new Label(6, 0, "磁带盘数"));
            wsheet.addCell(new Label(7, 0, "施工年度"));
            wsheet.addCell(new Label(8, 0, "施工单位"));
            wsheet.addCell(new Label(9, 0, "数据量"));
            wsheet.addCell(new Label(10, 0, "备注"));

            for (int j = 0; j < list.size(); j++) {
                wsheet.addCell(new Label(0, j + 1, ObjectUtils.toString(list.get(j).get("work_area"))));
                wsheet.addCell(new Label(1, j + 1, ObjectUtils.toString(list.get(j).get("test_line_number"))));
                wsheet.addCell(new Label(2, j + 1, ObjectUtils.toString(list.get(j).get("se_number"))));
                wsheet.addCell(new Label(3, j + 1, ObjectUtils.toString(list.get(j).get("record_length"))));
                wsheet.addCell(new Label(4, j + 1, ObjectUtils.toString(list.get(j).get("use_interval"))));
                wsheet.addCell(new Label(5, j + 1, ObjectUtils.toString(list.get(j).get("tape_number"))));
                wsheet.addCell(new Label(6, j + 1, ObjectUtils.toString(list.get(j).get("tape_size"))));
                wsheet.addCell(new Label(7, j + 1, ObjectUtils.toString(list.get(j).get("construction_year"))));
                wsheet.addCell(new Label(8, j + 1, ObjectUtils.toString(list.get(j).get("construction_unit"))));
                wsheet.addCell(new Label(9, j + 1, ObjectUtils.toString(list.get(j).get("data_quantity"))));
                wsheet.addCell(new Label(10, j + 1, ObjectUtils.toString(list.get(j).get("remarks"))));
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

    }

    /**
     * 二维数据
     * 
     * @return
     */
    @RequestMapping(value = "/tData/list")
    public String dimensionalData(HttpServletRequest request) {
        Map<String, Object> map = ServletUtils.getParameters(request);
        logger.info("二维数据清单， 请求参数内容为： {}", map);
        Page<?> page = new Page<Object>(getCurPage(map.get("pageNum")), SysConstant.PAGE_SIZE);
        PageInfo<Map<String, Object>> pageInfo = tDataService.list(map, page);

        List<Map<String, Object>> discTypes = commonService.listSystemParameters(SysConstant.DISC_TYPE);
        request.setAttribute("discType", discTypes);
        List<String> dirLists= getDir(null);
        request.setAttribute("dirLists", dirLists);
        request.setAttribute("pageInfo", pageInfo);
        
        Enumeration paramNames = request.getParameterNames();
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            Object [] values = request.getParameterValues(paramName);
            if (values == null || values.length == 0) {
                // Do nothing, no values found at all.
            } else {
                request.setAttribute(paramName, values[0]);
            }
        }
        
        
        return "data/tlist";
    }
    
    /**
     * 二维数据Excel
     * 
     * @return
     */
    @RequestMapping(value = "/tData/excel")
    public void dimensionalDataExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = ServletUtils.getParameters(request);
        logger.info("二维数据清单， 请求参数内容为： {}", map);
        Page<?> page = new Page<Object>(1, SysConstant.PAGE_SIZE);
        PageInfo<Map<String, Object>> pageInfo = tDataService.list(map, page);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.addAll(pageInfo.getList());
        int i = pageInfo.getPages();
        for (int j = 2; j <= i; j++) {
            page.setPageNum(j);
            list.addAll(tDataService.list(map, page).getList());
        }
        OutputStream os = response.getOutputStream();// 取得输出流
        response.reset();// 清空输出流
        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode("二维数据清单.xls", "utf-8"));// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        
        WritableWorkbook wbook = null;
        try {
            wbook = Workbook.createWorkbook(os); // 建立excel文件
            String tmptitle = "二维数据清单"; // 标题
            WritableSheet wsheet = wbook.createSheet(tmptitle, 0); // sheet名称

            wsheet.addCell(new Label(0, 0, "项目名称"));
            wsheet.addCell(new Label(1, 0, "测线（束）号"));
            wsheet.addCell(new Label(2, 0, "资料内容"));
            wsheet.addCell(new Label(3, 0, "资料编号"));
            wsheet.addCell(new Label(4, 0, "记录长度"));
            wsheet.addCell(new Label(5, 0, "采样间隔"));
            wsheet.addCell(new Label(6, 0, "处理单位"));
            wsheet.addCell(new Label(7, 0, "归档日期"));
            wsheet.addCell(new Label(8, 0, "数据量(GB)"));
            wsheet.addCell(new Label(9, 0, "备注"));
            
            for (int j = 0; j < list.size(); j++) {
                wsheet.addCell(new Label(0, j + 1, ObjectUtils.toString(list.get(j).get("project_name"))));
                wsheet.addCell(new Label(1, j + 1, ObjectUtils.toString(list.get(j).get("test_line_number"))));
                wsheet.addCell(new Label(2, j + 1, ObjectUtils.toString(list.get(j).get("record_content"))));
                wsheet.addCell(new Label(3, j + 1, ObjectUtils.toString(list.get(j).get("tape_number"))));
                wsheet.addCell(new Label(4, j + 1, ObjectUtils.toString(list.get(j).get("record_length"))));
                wsheet.addCell(new Label(5, j + 1, ObjectUtils.toString(list.get(j).get("use_interval"))));
                wsheet.addCell(new Label(6, j + 1, ObjectUtils.toString(list.get(j).get("filing_unit"))));
                wsheet.addCell(new Label(7, j + 1, ObjectUtils.toString(list.get(j).get("filing_date"))));
                wsheet.addCell(new Label(8, j + 1, ObjectUtils.toString(list.get(j).get("data_quantity"))));
                wsheet.addCell(new Label(9, j + 1, ObjectUtils.toString(list.get(j).get("remarks"))));
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
    }

    /**
     * 三维数据
     * 
     * @return
     */
    @RequestMapping(value = "/dData/list")
    public String dresultData(HttpServletRequest request) {
        Map<String, Object> map = ServletUtils.getParameters(request);
        logger.info("三维数据， 请求参数内容为： {}", map);
        Page<?> page = new Page<Object>(getCurPage(map.get("pageNum")), SysConstant.PAGE_SIZE);
        PageInfo<Map<String, Object>> pageInfo = dDataService.list(map, page);

        List<Map<String, Object>> discTypes = commonService.listSystemParameters(SysConstant.DISC_TYPE);
        request.setAttribute("discType", discTypes);
        List<String> dirLists= getDir(null);
        request.setAttribute("dirLists", dirLists);
        request.setAttribute("pageInfo", pageInfo);
        
        Enumeration paramNames = request.getParameterNames();
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            Object [] values = request.getParameterValues(paramName);
            if (values == null || values.length == 0) {
                // Do nothing, no values found at all.
            } else {
                request.setAttribute(paramName, values[0]);
            }
        }
        
        
        return "data/dlist";
    }
    
    /**
     * 三维数据下载
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/dData/excel")
    public void dDataListExcel(HttpServletRequest request, HttpServletResponse response) throws Exception { 
        Map<String, Object> map = ServletUtils.getParameters(request);
        logger.info("三维数据， 请求参数内容为： {}", map);
        Page<?> page = new Page<Object>(1, SysConstant.PAGE_SIZE);
        PageInfo<Map<String, Object>> pageInfo = dDataService.list(map, page);
        
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.addAll(pageInfo.getList());
        int i = pageInfo.getPages();
        for (int j = 2; j <= i; j++) {
            page.setPageNum(j);
            list.addAll(dDataService.list(map, page).getList());
        }
        OutputStream os = response.getOutputStream();// 取得输出流
        response.reset();// 清空输出流
        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode("三维数据清单.xls", "utf-8"));// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        
        WritableWorkbook wbook = null;
        try {
            wbook = Workbook.createWorkbook(os); // 建立excel文件
            String tmptitle = "三维维数据清单"; // 标题
            WritableSheet wsheet = wbook.createSheet(tmptitle, 0); // sheet名称

            wsheet.addCell(new Label(0, 0, "项目名称"));
            wsheet.addCell(new Label(1, 0, "记带内容"));
            wsheet.addCell(new Label(2, 0, "资料编号"));
            wsheet.addCell(new Label(3, 0, "INLINE范围"));
            wsheet.addCell(new Label(4, 0, "CROSSLINE范围"));
            wsheet.addCell(new Label(5, 0, "INLINE位置"));
            wsheet.addCell(new Label(6, 0, "CROSSLINE位置"));
            wsheet.addCell(new Label(7, 0, "X坐标"));
            wsheet.addCell(new Label(8, 0, "Y坐标"));
            wsheet.addCell(new Label(9, 0, "记录长度"));
            wsheet.addCell(new Label(10, 0, "采样间隔"));
            wsheet.addCell(new Label(11, 0, "处理单位"));
            wsheet.addCell(new Label(12, 0, "归档日期"));
            wsheet.addCell(new Label(13, 0, "数据量(GB)"));
            wsheet.addCell(new Label(14, 0, "备注"));
            
            for (int j = 0; j < list.size(); j++) {
                wsheet.addCell(new Label(0, j + 1, ObjectUtils.toString(list.get(j).get("project_name"))));
                wsheet.addCell(new Label(1, j + 1, ObjectUtils.toString(list.get(j).get("record_content"))));
                wsheet.addCell(new Label(2, j + 1, ObjectUtils.toString(list.get(j).get("tape_number"))));
                wsheet.addCell(new Label(3, j + 1, ObjectUtils.toString(list.get(j).get("inline_range"))));
                wsheet.addCell(new Label(4, j + 1, ObjectUtils.toString(list.get(j).get("crossline_range"))));
                wsheet.addCell(new Label(5, j + 1, ObjectUtils.toString(list.get(j).get("inline_position"))));
                wsheet.addCell(new Label(6, j + 1, ObjectUtils.toString(list.get(j).get("crossline_position"))));
                wsheet.addCell(new Label(7, j + 1, ObjectUtils.toString(list.get(j).get("x_position"))));
                wsheet.addCell(new Label(8, j + 1, ObjectUtils.toString(list.get(j).get("y_position"))));
                wsheet.addCell(new Label(9, j + 1, ObjectUtils.toString(list.get(j).get("record_length"))));
                wsheet.addCell(new Label(10, j + 1, ObjectUtils.toString(list.get(j).get("use_interval"))));
                wsheet.addCell(new Label(11, j + 1, ObjectUtils.toString(list.get(j).get("filing_unit"))));
                wsheet.addCell(new Label(12, j + 1, ObjectUtils.toString(list.get(j).get("filing_date"))));
                wsheet.addCell(new Label(13, j + 1, ObjectUtils.toString(list.get(j).get("data_quantity"))));
                wsheet.addCell(new Label(14, j + 1, ObjectUtils.toString(list.get(j).get("remarks"))));
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
    }

    /**
     * 中间成果数据
     * 
     * @return
     */
    @RequestMapping(value = "/mData/list")
    public String intermediateData(HttpServletRequest request) {
        Map<String, Object> map = ServletUtils.getParameters(request);
        logger.info("中间成果数据， 请求参数内容为： {}", map);

        
        
        
        Page<?> page = new Page<Object>(getCurPage(map.get("pageNum")), SysConstant.PAGE_SIZE);
        PageInfo<Map<String, Object>> pageInfo = mDataService.list(map, page);

        List<Map<String, Object>> discTypes = commonService.listSystemParameters(SysConstant.DISC_TYPE);
        request.setAttribute("discType", discTypes);
        List<String> dirLists= getDir(null);
        request.setAttribute("dirLists", dirLists);
        request.setAttribute("pageInfo", pageInfo);
        
        Enumeration paramNames = request.getParameterNames();
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            Object [] values = request.getParameterValues(paramName);
            if (values == null || values.length == 0) {
                // Do nothing, no values found at all.
            } else {
                request.setAttribute(paramName, values[0]);
            }
        }
        
        return "data/mlist";
    }

    
    
    @RequestMapping(value = "/mData/excel")
    public void mDataExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = ServletUtils.getParameters(request);
        logger.info("中间成果数据， 请求参数内容为： {}", map);

        Page<?> page = new Page<Object>(getCurPage(map.get("pageNum")), SysConstant.PAGE_SIZE);
        PageInfo<Map<String, Object>> pageInfo = mDataService.list(map, page);

        List<Map<String, Object>> discTypes = commonService.listSystemParameters(SysConstant.DISC_TYPE);
        request.setAttribute("discType", discTypes);
        List<String> dirLists= getDir(null);
        request.setAttribute("dirLists", dirLists);
        request.setAttribute("pageInfo", pageInfo);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.addAll(pageInfo.getList());
        int i = pageInfo.getPages();
        for (int j = 2; j <= i; j++) {
            page.setPageNum(j);
            list.addAll(tDataService.list(map, page).getList());
        }
        OutputStream os = response.getOutputStream();// 取得输出流
        response.reset();// 清空输出流
        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode("中间数据清单.xls", "utf-8"));// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
        
        WritableWorkbook wbook = null;
        try {
            wbook = Workbook.createWorkbook(os); // 建立excel文件
            String tmptitle = "二维数据清单"; // 标题
            WritableSheet wsheet = wbook.createSheet(tmptitle, 0); // sheet名称

            wsheet.addCell(new Label(0, 0, "项目名称"));
            wsheet.addCell(new Label(1, 0, "工区名称"));
            wsheet.addCell(new Label(2, 0, "负责人"));
            wsheet.addCell(new Label(3, 0, "科室名称"));
            wsheet.addCell(new Label(4, 0, "数据量（GB）"));
            wsheet.addCell(new Label(5, 0, "备份路径"));
//            wsheet.addCell(new Label(6, 0, "应用软件"));
            wsheet.addCell(new Label(6, 0, "备份时间"));
            wsheet.addCell(new Label(7, 0, "备注"));
            
            for (int j = 0; j < list.size(); j++) {
                wsheet.addCell(new Label(0, j + 1, ObjectUtils.toString(list.get(j).get("project_name"))));
                wsheet.addCell(new Label(1, j + 1, ObjectUtils.toString(list.get(j).get("area_block_name"))));
                wsheet.addCell(new Label(2, j + 1, ObjectUtils.toString(list.get(j).get("project_leader"))));
                wsheet.addCell(new Label(3, j + 1, ObjectUtils.toString(list.get(j).get("department_name"))));
                wsheet.addCell(new Label(4, j + 1, ObjectUtils.toString(list.get(j).get("data_quantity"))));
                wsheet.addCell(new Label(5, j + 1, ObjectUtils.toString(list.get(j).get("tape_number"))));
//                wsheet.addCell(new Label(6, j + 1, ObjectUtils.toString(list.get(j).get("software"))));
                wsheet.addCell(new Label(6, j + 1, ObjectUtils.toString(list.get(j).get("back_date"))));
                wsheet.addCell(new Label(7, j + 1, ObjectUtils.toString(list.get(j).get("remarks"))));
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
    }
    
    /**
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/data/delete")
    public String delete(String sid, String dataType, String url) {
        setDataService(dataType);
        dataService.delete(sid);
        System.out.println(">>>" + url);
        return "redirect:/" + url;
    }
    
   

    /**
     * 上传Excel数据
     * 
     * @param file
     *            上传的文件
     * @param dataType
     *            数据类型（原始数据类型， 二维数据类型， 三维数据类型， 中间数据）
     * @param positionType
     *            数据存放位置（本地硬盘or网络路径）
     * @return
     */
    @RequestMapping(value = "/data/upload")
    public String upload(HttpServletRequest request, @RequestParam(value = "file", required = true) MultipartFile file,
            String dataType, String dataSource, String discType,String exportPath,String server) {
        int result = 0;
        try {
            String filename = file.getOriginalFilename();
            logger.info("获取上传数据类型为(1:移动硬盘， 2:smb协议)： {}, 文件名为: {}, 光盘类型： {} 路径{}", dataSource, filename, discType,exportPath,server);
            exportPath=exportPath.replace("\\", "\\\\");
            String localfile = uploadFile(file);
            burnBusiness.master(filename, localfile, dataType, dataSource, discType,exportPath,server);
        } catch (ServiceException e) {
            logger.error("上传数据业务异常", e);
            request.setAttribute("info", e);
            return "data/uploaderror";
        } catch (Exception e) {
            logger.error("上传数据未知异常", e);
            request.setAttribute("info", e);
            return "data/uploaderror";
        }
        request.setAttribute("result", result);
        return "data/result";
    }

    @RequestMapping(value = "/data/rawUpdateInit")
    public String rawUpdateInit(HttpServletRequest request, String sid) {
        Map<String, Object> map = rawDataService.findById(sid);
        request.setAttribute("data", map);
        return "data/rawupdate";
    }

    @RequestMapping(value = "/data/tUpdateInit")
    public String tUpdateInit(HttpServletRequest request, String sid) {
        Map<String, Object> map = tDataService.findById(sid);
        request.setAttribute("data", map);
        return "data/tupdate";
    }

    @RequestMapping(value = "/data/dUpdateInit")
    public String dUpdateInit(HttpServletRequest request, String sid) {
        Map<String, Object> map = dDataService.findById(sid);
        request.setAttribute("data", map);
        return "data/dupdate";
    }

    @RequestMapping(value = "/data/mUpdateInit")
    public String mUpdateInit(HttpServletRequest request, String sid) {
        Map<String, Object> map = mDataService.findById(sid);
        request.setAttribute("data", map);
        return "data/mupdate";
    }

    @RequestMapping(value = "/data/update")
    public String update(HttpServletRequest request, String dataType, String url) {
        Map<String, Object> param = ServletUtils.getParameters(request);
        logger.info("修改数据类型为： {}, 修改内容为： {}， 跳转页面为： {}", dataType, param, url);
        setDataService(dataType);
        dataService.update(param);
        return "redirect:/" + url;
    }
    
    @RequestMapping(value = "/data/rawInsertInit")
    public String rawInsertInit(HttpServletRequest request, String sid,String valLabel) {
        request.setAttribute("sid", sid);
        request.setAttribute("valLabel", valLabel);
        return "data/rawinsert";
    }

    @RequestMapping(value = "/data/tInsertInit")
    public String tInsertInit(HttpServletRequest request, String sid,String valLabel) {
//        Map<String, Object> map = tDataService.findById(sid);
//        request.setAttribute("data", map);
        request.setAttribute("sid", sid);
        request.setAttribute("valLabel", valLabel);
        return "data/tinsert";
    }

    @RequestMapping(value = "/data/dInsertInit")
    public String dInsertInit(HttpServletRequest request, String sid,String valLabel) {
//        Map<String, Object> map = dDataService.findById(sid);
//        request.setAttribute("data", map);
        request.setAttribute("sid", sid);
        request.setAttribute("valLabel", valLabel);
        return "data/dinsert";
    }

    @RequestMapping(value = "/data/mInsertInit")
    public String mInsertInit(HttpServletRequest request, String sid,String valLabel) {
        request.setAttribute("sid", sid);
        request.setAttribute("valLabel", valLabel);
        return "data/minsert";
    }
    
    @RequestMapping(value = "/data/insert")
    public String insert(HttpServletRequest request, String dataType, String url,String valLabel) {
        Map<String, Object> param = ServletUtils.getParameters(request);
        logger.info("补录入数据类型为： {}, 修改内容为： {}， 跳转页面为： {}，卷标号为:{}", dataType, param, url,valLabel);
        setDataService(dataType);
        dataService.insert(param,valLabel);
        return "redirect:/" + url;
    }
    

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

    /**
     * 设置不同的实现类
     * 
     * @param dataType
     */
    public void setDataService(String dataType) {
        DataType dataTypeEnum = (DataType) EnumUtils.convertEnum(DataType.class, dataType);
        this.dataService = SpringContextHelper.getBean(dataTypeEnum.getBean(), DataService.class);
    }

    public static void main(String[] args) {
        System.out.println(formatDate(null));
    }
    @RequestMapping("/data/openDir")
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
}