package com.midas.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 公共方法
 * 
 * @author arron
 *
 */
public interface CommonMapper {

    /**
     * 通过日期和类型查找指定唯一值
     * 
     * @param cdate yyyyMMdd
     * @param ctype 如：R T D M
     * @return
     */
    public int seqUniqueNextVal(@Param("cdate")String cdate, @Param("ctype")String ctype);
    
    
    /**
     * 根据参数获取系统参数类别
     * @param map 查询条件
     * @return {@link List} <String, Object>
     */
    public List<Map<String, Object>> listSystemParameters(Map<String, Object> map);

}
