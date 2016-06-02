package com.midas.constant;

public class SysConstant {

    /**
     * 系统编码
     */
    public static final String ENCODING       = "UTF-8";
    /**
     * 连接成功的标志文件
     */
    public static final String LINK_FLAG_FILE = "LINK_FLAG_FILE";
    /**
     * 每页大小
     */
    public static final int    PAGE_SIZE      = 20;

    /////////////////////////// comm.properties 中获取
    /**
     * 上传的Excel所在目录
     */
    public static final String EXCEL_BACK_DIR = "EXCEL_BACK_DIR";
    /**
     * 环境判断， DEV 开发， TEST, 测试， RRO 生产
     */
    public static final String ENVIRONMENTAL  = "ENVIRONMENTAL";
    /**
     * 合并指令
     */
    public static final String MERGE_ELF      = "MERGE_ELF";
    /**
     * 合并文件目录
     */
    public static final String READ_PATH      = "READ_PATH";
    /**
     * 输出目录
     */
    public static final String EXPORT_PATH    = "EXPORT_PATH";

    /**
     * 文件在本地存储的地址
     */
    // public static final String DATA_LOCAL_PATH = "DATA_LOCAL_PATH";
    /////////////////////////// 数据库系统参数表中获取
    /**
     * 盘库配置信息
     */
    public static final String DISC_LIBRARY           = "DISC_LIBRARY";
    /**
     * 离线柜
     */
    public static final String OFF_LINE_CABINET       = "OFF_LINE_CABINET";
    /**
     * 光盘类型
     */
    public static final String DISC_TYPE              = "DISC_TYPE";
    /**
     * 网络共享地址
     */
    public static final String SHARED_NETWORK_ADDRESS = "SHARED_NETWORK_ADDRESS";
    /**
     * 错误码
     */
    public static final String ERROR_CODE             = "ERROR_CODE";
    /**
     * 本地文件路径
     */
    public static final String HARDDISCK              = "/jukebox/harddisk";

}
