package com.midas.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.midas.vo.StandingBook;

public interface StandingbookService {
    /**
     * 原始数据台账信息
     * 
     * @param map
     * @return
     */
    public PageInfo<Map<String, Object>> queryStandingbookListByPage(Map<String, Object> map, Page<?> page);

    /**
     * 添加台账信息
     * 
     * @param map
     * @return
     */
    public int insert(Map<String, Object> map);

    /**
     * 修改台账信息
     * 
     * @param map
     * @return
     */
    public int update(Map<String, Object> map);
    public int save(StandingBook vo);
    
    public int delete(String sid);
    public List<Map<String, Object>> getStandingbook(Map<String, Object> map);
}
