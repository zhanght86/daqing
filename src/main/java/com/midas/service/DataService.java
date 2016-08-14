package com.midas.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

public interface DataService {

    /**
     * 通过条件查询原始数据清单
     * 
     * @param map
     *            查询条件
     * @return 分页数据 PageInfo<Map<String, Object>>
     */
    public PageInfo<Map<String, Object>> list(Map<String, Object> map, Page<?> page);

    /**
     * 添加记录
     * 
     * @param map
     * @return
     */
    public int insert(Map<String, Object> map, String valLabel);

    /**
     * 批量添加数据
     * 
     * @param list
     * @return
     */
    public int insertBetch(List<Map<String, Object>> list, String valLabel);

    /**
     * 根据SID删除记录信息
     * 
     * @param sid
     * @return
     */
    public int delete(String sid);

    /**
     * 通过唯一卷标号查找数据
     * 
     * @param valLabel
     * @return
     */
    public List<Map<String, Object>> findByVolLabel(String volLabel);

    /**
     * 根据卷标号, 获取所有的下载文件路径
     * 
     * @param volLabel
     *            卷标号
     * @return List<String>
     */
    public List<String> getFilepath(String volLabel,String dataSource);

    /**
     * 根据ID查找内容
     * 
     * @param id
     * @return
     */
    public Map<String, Object> findById(String id);

    /**
     * 更新记录数据
     * 
     * @param map
     */
    public void update(Map<String, Object> map);
    /**
     * 根据卷标号删除所有记录
     * 
     * @param volLabel
     */
    public void deleteByVolLabel(String volLabel);
}
