package com.midas.dao;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

public interface DownloadDataInfoDao {

    /**
     * 查询列表
     * 
     * @param map
     * @param page
     * @return
     */
    public List<Map<String, Object>> list(Map<String, Object> map);

    /**
     * 查询列表
     * 
     * @param map
     * @return
     */
    public PageInfo<Map<String, Object>> list(Map<String, Object> map, Page<?> page);

    /**
     * 添加记录
     * 
     * @param map
     * @return
     */
    public int insert(Map<String, Object> map);

    /**
     * 修改
     * 
     * @param map
     * @return
     */
    public int update(Map<String, Object> map);

    /**
     * 根据编号获取一条记录
     * 
     * @param id
     *            编号
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    public Map<String, Object> getById(String id);

    /**
     * 根据条件获取
     * 
     * @param map
     * @return
     */
    public int getCountByCondition(Map<String, Object> map);

    /**
     * 根据条件获取单个对象
     * 
     * @param map
     * @return
     */
    public Map<String, Object> getByCondtion(Map<String, Object> map);

}
