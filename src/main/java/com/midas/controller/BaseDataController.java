package com.midas.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.midas.constant.ErrorConstant;
import com.midas.constant.SysConstant;
import com.midas.exception.ServiceException;
import com.midas.uitls.date.DateUtil;
import com.midas.uitls.tools.CommonsUtils;

public class BaseDataController {

    
    /**
     * 获取当前页
     * 
     * @param curpage
     * @return
     */
    protected static int getCurPage(Object curpage) {
        int pageNum = 1;
        if (null != curpage && !"".equals(curpage)) {
            String pageNumStr = ObjectUtils.toString(curpage);
            try {
                pageNum = Integer.parseInt(pageNumStr);
            } catch (Exception e) {
            }
        }
        return pageNum;
    }
    
    /**
     * 上传文件到服务器
     * 
     * @param file
     * @return
     */
    protected String uploadFile(MultipartFile file) {
        String localfile = null;
        InputStream is = null;
        FileOutputStream os = null;
        try {
            String filename = file.getOriginalFilename();

            if (filename == null || "".equals(filename)) {
                throw new ServiceException(ErrorConstant.CODE2000, "上传文件内容为空");
            }
            String backDir = CommonsUtils.getPropertiesValue(SysConstant.EXCEL_BACK_DIR);
            File f = new File(backDir);
            if(!f.exists()) {
                f.mkdirs();
            }
            is = file.getInputStream();
            byte[] data = new byte[is.available()];
            is.read(data);

            localfile = getFile(backDir, filename);
            os = new FileOutputStream(localfile);
            os.write(data);
        } catch (ServiceException e) {
            throw new ServiceException(e);
        } catch (IOException e) {
            throw new ServiceException(ErrorConstant.CODE2000, "存储到本地失败", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
            }
        }
        return localfile;
    }

    public static void main(String[] args) {
        System.out.println(getFile("c:/", "原始数据清单.xls"));
    }

    private static String getFile(String dir, String file) {
        String randomStr = randomStr();
        String endStr = file.substring(file.lastIndexOf("."), file.length());
        return dir + file.replace(endStr, "." + randomStr) + endStr;
    }

    private static String randomStr() {
        StringBuffer sb = new StringBuffer();
        sb.append(DateUtil.DateToString(new Date(), "yyMMddHHmmsss"));
        Random r = new Random();
        for (int i = 0; i < 4; i++) {
            sb.append(r.nextInt(10));
        }
        return sb.toString();
    }
    
}
