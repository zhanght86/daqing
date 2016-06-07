package com.midas.service;

import java.util.Map;

import com.midas.exception.ServiceException;

public interface BurnBusiness {

    /**
     * 主流程
     * 
     * @param filename
     *            文件或者任务名称
     * @param localFile
     *            本地文件路径
     * @param dataType
     *            数据类型
     * @param dataSource
     *            数据来源
     * @param discType
     *            光盘类型
     * @throws ServiceException
     */
    public void master(String filename, String localFile, String dataType, String dataSource, String discType,String exportPath,String server)
            throws ServiceException;

    /**
     * 运行刻录任务
     * 
     * @param volLabel
     *            任务ID or 唯一卷标
     */
    public void masterBurn(String volLabel);

    /**
     * 启动刻录
     * 
     * @param burnMachine
     * @param volLabel
     * @param discType
     * @throws Exception
     */
    public void masterMidasBurn(String burnMachine, String volLabel, String discType) throws Exception;

    /**
     * 合并卷标内容
     * 
     * @param volLabel
     *            卷标
     * @throws ServiceException
     *             业务异常
     */
    public boolean masterMerge(String volLabel, String exportpath) throws ServiceException;

    /**
     * 获取导出结果, 并合并为文件
     */
    public void masterMergeNotify(String volLabel);

    /**
     * 查询后续结果
     */
    public void masterNotify(Map<String, Object> map);
    
    public String getLocalPath(String volLabel);
    
    public boolean masterMergeTaskSave(String volLabel, String exportpath) throws ServiceException;

}
