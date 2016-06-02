package com.midas.enums;

/**
 * 导出状态
 * 
 * @author arron
 *
 */
public enum ExportState {

    /*
     * 1. 正在导出数据 2. 导出成功 3. 导出失败',
     */

    EXPORTTING("1", "下载数据"),

    /**
     * 导出成功
     */
    EXPORT_SUCCESS("2", "下载成功"),

    /**
     * 导出失败
     */
    EXPORT_FAILD("3", "下载失败"),

    MEGE_SUCCESS("4", "导出成功"),

    MEGE_FAILD("5", "导出失败"),
    
    MEGE_CANCEL("6", "手动关闭")
    ;

    private String key;
    private String value;

    private ExportState(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.key;
    }

}
