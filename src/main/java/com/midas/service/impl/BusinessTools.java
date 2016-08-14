package com.midas.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.midas.constant.ErrorConstant;
import com.midas.exception.ServiceException;
import com.midas.uitls.tools.ConfigReader;

public class BusinessTools {

    /**
     * 通过data.ini文件把数组转换为Map对象
     * 
     * @param data
     * @param type
     * @return
     */
    public static Map<String, Object> convertIni(String[] data, String type) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            Map<String, String> colsMap = ConfigReader.getInstance().get(type);
            if (null == colsMap || colsMap.isEmpty()) {
                throw new ServiceException(ErrorConstant.CODE2000, "请检查data.ini中是否存在:" + type);
            }
            Set<Entry<String, String>> set = colsMap.entrySet();
            Iterator<Entry<String, String>> iter = set.iterator();
            while (iter.hasNext()) {
                Entry<String, String> entry = iter.next();
                // 看情况特殊处理
                int colIndex = Integer.parseInt(entry.getValue());
                map.put(entry.getKey(), data[colIndex]);
            }
            return map;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ErrorConstant.CODE2000, "数组转换Map失败", e);
        }
    }


    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    
}


