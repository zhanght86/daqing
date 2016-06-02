package com.midas.dao;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 * 原始数据
 * 
 * @author arron
 *
 */
public interface DataDao {

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
     * 根据联合主键查找对象
     * 
     * @param map
     * @return Map<String, Object>
     */
    public Map<String, Object> getByUniquePk(Map<String, Object> map);

    /**
     * 删除记录
     * 
     * @param map
     * @return
     */
    public int delete(Map<String, Object> map);

    /**
     * 根据卷标号找到刻录数据
     * 
     * @param volLabel
     * @return
     */
    public List<Map<String, Object>> findByVolLabel(String volLabel);

    /**
     * 根据ID查找内容
     * 
     * @param id
     * @return
     */
    public Map<String, Object> findById(String id);

    /**
     * 更新数据内容
     * 
     * @param map
     */
    public void update(Map<String, Object> map);

    /**
     * 根据卷标号删除内容
     * @param volLabel
     */
    public void deleteByVolLabel(String volLabel);

}
