package com.midas.dao;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

public interface BurnDao {

    public int insert(Map<String, Object> map);

    public int update(Map<String, Object> map);

    public List<Map<String, Object>> list(Map<String, Object> map);

    /**
     * 分页查询
     * 
     * @param map
     * @param page
     * @return
     */
    public PageInfo<Map<String, Object>> list(Map<String, Object> map, Page<Map<String, Object>> page);

    /**
     * 获取最后一条数据
     * 
     * @param map
     * @return
     */
    public Map<String, Object> getLast(Map<String, Object> map);

    /**
     * 通过卷标号获取任务信息
     * 
     * @param volLabel
     * @return
     */
    public Map<String, Object> getBurnByVolLabel(String volLabel);

    /**
     * 更新记录的状态
     * 
     * @param map
     * @return
     */
    public boolean updateState(Map<String, Object> map);

    /**
     * 根据卷标号或者数据所在位置
     * 
     * @param volLabel
     * @return
     */
    public List<Map<String, Object>> listPosition(String volLabel);

    /**
     * 查询卷标号， 电子标签， 位置信息
     * 
     * @param volLabel
     * @param electronic_tag
     * @param disc_position
     */
    public List<Map<String, Object>> getBurnDetailByCondition(String volLabel, String electronic_tag,
            String disc_position);

    /**
     * 添加详细内容
     * 
     * @param map
     */
    public int insertDetail(Map<String, Object> map);

    /**
     * 查找导出记录
     * 
     * @param volLabel
     *            唯一号
     * @param state
     *            状态
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> listExportRecord(String volLabel, String state,String task_name);

    /**
     * 添加合并记录
     * 
     * @param map
     * @return
     */
    public int insertExportRecord(Map<String, Object> map);

    /**
     * 修改合并记录
     * 
     * @param map
     * @return
     */
    public int updateExportRecord(Map<String, Object> map);

    /**
     * 查询导出历史记录
     * 
     * @param volLabel
     * @return
     */
    public List<Map<String, Object>> listExportRecord(String volLabel,String task_name);

    /**
     * 更新刻录大小
     * 
     * @param map
     */
    public void updateSize(Map<String, Object> map);

    /**
     * 根据卷标号删除记录
     * 
     * @param volLabel
     */
    public void delete(String volLabel);

    /**
     * 删除详细
     * @param volLabel
     */
    void deleteDetail(String volLabel);
    
    void deleteExport(String eid);
    

}
