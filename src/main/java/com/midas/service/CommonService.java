package com.midas.service;

import java.util.List;
import java.util.Map;

import com.midas.enums.DataType;
import com.midas.vo.BurnProgress;
import com.midas.vo.FileVo;

public interface CommonService {

    /**
     * 获取固定固定长度的唯一字符串
     * 
     * @param cdate
     *            日期
     * @param ctype
     *            类型
     * @return
     */
    public String seqUniqueNextVal(String cdate, String ctype);

    /**
     * 获取固定固定长度的唯一字符串
     * 
     * @param cdate
     *            日期
     * @param ctype
     *            类型枚举
     * @return
     */
    public String seqUniqueNextVal(String cdate, DataType dataType);

    /**
     * 根据Code获取有效的参数列表
     * 
     * @param code
     *            键
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> listSystemParameters(String code);

    /**
     * 根据条件获取单条记录, 如有多条记录, 采用limit 1 方式保证唯一
     * 
     * @param map
     * @return
     */
    public Map<String, Object> getSystemParameters(Map<String, Object> map);

    /**
     * 获取有效的网络共享数据信息
     * 
     * @param code
     * @return
     */
    public Map<String, Object> getSystemParameters(String code);

    /**
     * 查看盘库是否繁忙
     * 
     * @param server
     *            系统参数表， 盘库的配置KEY
     * @return true ： 繁忙 false ： 闲置
     */
    public boolean isBusy(String server);

    /**
     * 检查服务器是否连接
     * 
     * @param map
     * @return
     */
    public boolean isConnect(Map<String, Object> map);

    /**
     * 查看有效盘数量
     * 
     * @param sever
     * @return 系统参数表， 盘库的配置KEY
     */
    public int effectiveQuantity(String sever);

    /**
     * 查找刻录状态
     * 
     * @param server
     *            服务
     * @param jobName
     *            卷标号
     * @return BurnProgress
     */
    public BurnProgress getBurnStatus(String server, String jobName);

    /**
     * 根据电子标签号或者所在位置
     * 
     * @param tag
     * @return Map中存在参数 serverName,server,position
     */
    public Map<String, Object> getTagposition(String tag);

    /**
     * 往指定机器发送DUMPMEDIA指令
     * 
     * @param server
     *            指定机器
     * @param position
     *            文件名ISO
     * @return true：成功<br />
     *         false：失败
     */
    public boolean executeDUMPMEDIA(String server, String position);

    /**
     * 获取所有的刻录机器
     * 
     * @return
     */
    public List<Map<String, Object>> getAllMachine();
    
    public String executeFindFile(String server,String name);
    public String executeFindFileOffLine(Map<String, Object> map,String server ,String name);
    public List<FileVo> executeFindFileBySocket(String server,String name);

}
