package com.midas.mapper;

import java.util.List;
import java.util.Map;

/**
 * 台账信息管理
 * 
 * @author arron
 *
 */
public interface StandingbookMapper {

    /**
     * 原始数据台账信息
     * 
     * @param map
     * @return
     */
    public List<Map<String, Object>> queryStandingbookListByPage(Map<String, Object> map);

    /**
     * 添加记录
     * 
     * @param map
     * @return
     */
    public int insert(Map<String, Object> map);

    /**
     * 修改记录
     * 
     * @param map
     * @return
     */
    public int update(Map<String, Object> map);

    /**
     * 根据参数获取记录信息
     * 
     * @param map
     * @return
     */
    public List<Map<String, Object>> getStandingbook(Map<String, Object> map);
    public int insertBatch(Map<String, Object> map);
    public int delete(Map<String, Object> map);
}
