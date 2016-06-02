package com.midas.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 * 下载数据管理
 * 
 * @author arron
 *
 */
public interface DownloadDataInfoService {

    /**
     * 条件查询列表
     * 
     * @param map
     * @return
     */
    public List<Map<String, Object>> list(Map<String, Object> map);

    /**
     * 通过条件查询
     * 
     * @param map
     *            查询条件
     * @return 分页数据 PageInfo<Map<String, Object>>
     */
    public PageInfo<Map<String, Object>> list(Map<String, Object> map, Page<?> page);

    /**
     * 修改下载数据
     * 
     * @param map
     * @return
     */
    public int update(Map<String, Object> map);

    /**
     * 查询修改{初始化修改为下载中}
     * 
     * @param map
     * @return
     */
    public Map<String, Object> updateBySelect();

    /**
     * 添加记录
     * 
     * @param map
     * @return
     */
    public int insert(Map<String, Object> map);

    /**
     * 根据条件获取单个对象
     * @param map
     * @return
     */
    public int getCountByCondition(Map<String, Object> map);
}
