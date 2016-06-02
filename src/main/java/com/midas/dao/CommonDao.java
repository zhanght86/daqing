package com.midas.dao;

import java.util.List;
import java.util.Map;

public interface CommonDao {

    /**
     * 通过日期和类型查找指定唯一值
     * 
     * @param cdate
     *            yyyyMMdd
     * @param ctype
     *            如：R T D M
     * @return
     */
    public int seqUniqueNextVal(String cdate, String ctype);
    
    
    /**
     * 根据参数获取系统参数类别
     * @param map 查询条件
     * @return {@link List} <String, Object>
     */
    public List<Map<String, Object>> listSystemParameters(Map<String, Object> map);

}
