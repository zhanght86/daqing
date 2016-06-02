package com.midas.enums;

/**
 * 数据来源
 * 
 * @author arron
 *
 */
public enum DataSource {

    /**
     * 1:移动硬盘， 2:smb协议
     */

    /**
     * 1:移动硬盘
     */
    HARD_DISK("1", "移动硬盘"),

    
    /**
     * 2:smb协议
     */
    NETWORK_SHARING("2", "SMB协议");

    private String key;
    private String value;

    private DataSource(String key, String value) {
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
