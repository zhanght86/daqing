package com.midas.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BurnMapper {

    /**
     * 添加记录
     * 
     * @param map
     * @return int
     */
    public int insert(Map<String, Object> map);

    /**
     * 添加记录
     * 
     * @param map
     * @return int
     */
    public int insertInit(Map<String, Object> map);

    /**
     * 更新内容, 每次全量更新
     * 
     * @param map
     * @return int
     */
    public int update(Map<String, Object> map);

    /**
     * 查询所有内容 目前有三个参数
     * 
     * @param map
     * @return
     */
    public List<Map<String, Object>> list(Map<String, Object> map);

    /**
     * 查找最后一条记录
     * 
     * @param map
     * @return
     */
    public Map<String, Object> getLast(Map<String, Object> map);

    /**
     * 根据唯一编号获取指定内容
     * 
     * @param volLabel
     *            唯一卷标号
     * @return Map<String, Object>
     */
    public Map<String, Object> getBurnByVolLabel(String volLabel);

    /**
     * 更新记录状态
     * 
     * @param map
     * @return
     */
    public boolean updateState(Map<String, Object> map);

    /**
     * 根据卷标号获取电子标签
     * 
     * @param volLabel
     * @return
     */
    public List<Map<String, Object>> listPosition(String volLabel);

    /**
     * 卷标号， 电子标签， 位置查找内容
     * 
     * @param volLabel
     * @param electronicTag
     * @param discPosition
     * @return
     */
    public List<Map<String, Object>> getBurnDetailByCondition(@Param("volLabel") String volLabel,
            @Param("elTag") String electronicTag, @Param("discPos") String discPosition);

    /**
     * 添加详细数据
     * 
     * @param map
     * @return
     */
    public int insertDetail(Map<String, Object> map);

    /**
     * 查找是否有导出的记录
     * 
     * @param volLabel
     *            唯一号
     * @param state
     *            状态
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> listExportRecord(@Param("volLabel") String volLabel, @Param("state") String state, @Param("task_name") String task_name);

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
     * 修改合并记录
     * 
     * @param map
     * @return
     */
    public int updateExportRecordNew(Map<String, Object> map);

    /**
     * 历史导出记录
     * 
     * @param volLabel
     * @return
     */
    public List<Map<String, Object>> listExportRecordByVol(@Param("volLabel") String volLabel,@Param("task_name") String task_name);

    /**
     * 更新记录的大小内容
     * 
     * @param map
     */
    public void updateSize(Map<String, Object> map);

    /**
     * 删除卷标内容
     * 
     * @param volLabel
     */
    public void delete(@Param("volLabel")String volLabel);
    /**
     * 删除卷标详细刻录数据
     * 
     * @param volLabel
     */
    public void deleteDetail(@Param("volLabel")String volLabel);
    
    public void deleteExport(@Param("eid")String eid);
    
    /**
     * 导出文件
     * 
     * @param map
     * @return
     */
    public int insertExportFileRecord(Map<String, Object> map);
    /**
     * 文件导出任务
     * @param param
     * @return
     */
	public List<Map<String, Object>> listExportTask(String param);
	
	/**
	 * 修改任务状态
	 * @param map
	 * @return
	 */
	public int updateExportFile(Map<String, Object> map);
	
	/**
	 * 检查可用任务
	 * @param map
	 * @return
	 */
	List<Map<String, Object>>  listExportRecordCheck();

	/**
	 * 文件任务列表
	 * @return
	 */
	public List<Map<String, Object>> listExportFileRecord(Map<String, Object> paramMap);

	public List<Map<String, Object>> listExportRecordCheck(String state);
    
	
}
