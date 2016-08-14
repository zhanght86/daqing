package com.midas.enums;

/**
 * 
 * 文件下载状态 初始化 开始下载 下载完成 开始拆分 拆分完成 开始刻录 刻录完成
 * 
 * @author arron
 *
 */
public enum DataDownState {

    /**
     * 初始
     */
    INIT("8", "初始"),

    /**
     * 下载中
     */
    BEGIN_DOWNLOAD("9", "下载中"),

    /**
     * 下载完成
     */
    DOWNLOAD_SUCCESS("7", "下载完成"),

    /**
     * 下载失败
     */
    DOWNLOAD_FAILD("-1", "下载失败"),

    /**
     * 拆分文件
     */
    SPLIT_FILE("2", "开始拆分文件"),

    SPLIT_FALID("3", "拆分失败"),

    SPLIT_SUCCESS("4", "拆分成功"),

    UPLOAD_INIT("5", "开始刻录"),

    UPLOAD_SUCCESS("5", "开始刻录"),

    UPLOAD_FAILD("5", "开始刻录"),

    BURN_INIT("5", "开始刻录"),

    BURN_FAILD("5", "开始刻录"),

    BURN_SUCCESS("5", "开始刻录");

    private String key;
    private String value;

    private DataDownState(String key, String value) {
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
