package com.midas.dao;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.midas.vo.StandingBook;

public interface StandingbookDao {

    /**
     * 原始数据台账信息
     * 
     * @param map
     * @return
     */
    public PageInfo<Map<String, Object>> queryStandingbookListByPage(Map<String, Object> map, Page<?> page);

    /**
     * 添加数据
     * 
     * @return
     */
    public int insert(Map<String, Object> map);

    /**
     * 修改数据内容
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
    
    public int save(StandingBook vo);

    int insertBatch(Map<String, Object> map);
    public int delete(Map<String, Object> map);
    
}
