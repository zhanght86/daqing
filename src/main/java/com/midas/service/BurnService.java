package com.midas.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.midas.exception.ServiceException;

/**
 * 刻录任务管理
 * 
 * @author arron
 *
 */
public interface BurnService {

    /**
     * 获取空闲的盘库机器信息
     * 
     * @return Map<String, Object> 盘库
     * @throws ServiceException
     */
    public Map<String, Object> getFreeBurn() throws ServiceException;

    /**
     * 获取刻录信息列表
     * 
     * @param map
     * @return
     */
    public List<Map<String, Object>> list(Map<String, Object> map);

    /**
     * 添加刻录信息
     * 
     * @param map
     * @return
     */
    public int insert(Map<String, Object> map);

    /**
     * 添加刻录任务
     * 
     * @param taskName
     *            任务名称
     * @param discType
     *            光盘类型
     * @param dataSource
     *            数据来源， 网络驱动or硬盘
     * @param burnMachine
     *            刻录盘库
     * @return
     */
    public String insertBurn(String taskName, String discType, String dataSource, String burnMachine, String dataType,String exportPath);

    /**
     * 通过卷标号获取刻录的任务
     * 
     * @param volLabel
     *            卷标号 唯一
     * @return Map<String, Object>
     */
    public Map<String, Object> getBurnByVolLabel(String volLabel);

    /**
     * 更新刻录状态
     * 
     * @param volLabel
     *            卷标号
     * @param state
     *            状态
     * @param desc
     *            描述
     * @param discNumber
     *            刻录的盘数
     * @return
     */
    public boolean updateState(String volLabel, String state, String desc, int discNumber);
    public boolean updateState(String volLabel,String burn_progress);
    
    /**
     * 分页查询列表
     * 
     * @param map
     * @param page
     * @return
     */
    public PageInfo<Map<String, Object>> list(Map<String, Object> map, Page<Map<String, Object>> page);

    /**
     * 通过卷标号获取位置
     * 
     * @param volLabel
     *            卷标号
     * @return List < Map <String, Object>>
     */
    public List<Map<String, Object>> listPosition(String volLabel);

    /**
     * 添加刻录位置说明
     * 
     * @param volLabel
     *            卷标号
     * @param electronic_tag
     *            电子标签号
     * @param disc_position
     * @return
     */
    public boolean insertBurnDetail(String volLabel, String electronic_tag, String disc_position, String isoFilename,
            String time);

    /**
     * 1. 该任务目前未在导出 2. 光盘是否都在盘库中 3. 盘库目前不是繁忙状态
     * 
     * @param volLabel
     *            唯一卷标号
     * @return true ： 可以导出 <br />
     *         false:不可用导出
     */
    public boolean checkMerge(String volLabel);

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
     * 查找所有正在导出的内容
     * 
     * @param volLabel
     *            卷标
     * @param state
     *            状态
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> listExportRecord(String volLabel, String state,String task_name);

    /**
     * 根据唯一号, 查找导出数据内容
     * 
     * @param volLabel
     *            卷标号
     * @return List<Map>
     */
    public List<Map<String, Object>> listExportRecord(String volLabel,String task_name);

    /**
     * 更新刻录的大小
     * 
     * @param volLabel
     * @param size
     */
    public void updateSize(String volLabel, BigDecimal size);
    /**
     * 检查任务是否已经取消
     * @param volLabel
     * @return
     */
    public boolean isCancel(String volLabel);

    /**
     * 删除刻录任务
     * @param volLabel
     * @param dataType
     */
    public void delete(String volLabel, String dataType);
    
    public void  deleteExport(String eid);
    
    
    /**
     * 根据卷标号,文件名搜索结果
     * 
     * @param keyWordl
     *            查询关键字
     * @return List<Map>
     */
    public List<Map<String, Object>> listExportFileList(String keyWord);
    


	
    /**
     * 列出导出任务
     * @param param
     * @return
     */
	List<Map<String, Object>> listExportTask(String param);



    /**
     * 可导出文件任务检查
     * @param soucePath
     * @return
     * @throws Exception
     */
	boolean CheckTaskAndRun(String soucePath) throws Exception;
    /**
     * 导出文件任务保存
     * @param soucePath
     * @param exportpath
     * @return
     * @throws ServiceException
     */
	boolean savefileExportTask(String soucePath, String exportpath) throws ServiceException;
    
	/**
	 * 检查可导出的任务
	 * @param volLabel
	 * @param state
	 * @param task_name
	 * @return
	 */
	Map<String, Object> listExportRecordCheck(String volLabel, String state, String task_name);

	/**
	 * 导出文件任务列表
	 * @param volLabel
	 * @param state
	 * @param task_name
	 * @return
	 */

	PageInfo<Map<String, Object>> listExportFileRecord(Map<String, Object> paramMap, Page page);

	/**
	 * 离线文件位置查询
	 * @param fileName
	 * @return
	 */
	List<Map<String, Object>> listExportFileListOffline(String fileName);
    
	
	/**
	 * 文件导出任务执行
	 * @param paramMap
	 * @return
	 */
	public boolean RunTask( Map<String, Object> paramMap);

	/**
	 * 导出文件任务删除
	 * @param eid
	 */
	void deleteExportFile(String eid);

	/**
	 * 查询离线柜查询
	 * @param volLabel
	 * @return
	 */
	List<Map<String, Object>> listPositionOffline(String volLabel);

	/**
	 * 任务重新执行
	 * @param eid
	 */
	void reRunExportFile(String eid);

	

	



	

}
