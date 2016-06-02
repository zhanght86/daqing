package com.midas.enums;

/**
 * 数据状态
 * 
 * @author arron
 *
 */
public enum BurnState {

    INPUT_SUCCESS("1", "初始化"),

    UPLOAD_INIT("2", "下载数据"),

    UPLOAD_SUCCESS("3", "下载成功"),

    UPLOAD_FAILD("4", "下载失败"),

    BURN_INIT_FAILD("5", "指令失败"),

    BURNNING("6", "刻录中."),

    BURN_WAIT("7", "刻录延迟"),

    BURN_SUCCESS("8", "刻录成功"),

    BURN_ERROR("9", "刻录失败"),
    
    CANCEL("11", "手动取消"),

    OTHER("10", "其他");
    

    private String key;
    private String value;

    private BurnState(String key, String value) {
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
