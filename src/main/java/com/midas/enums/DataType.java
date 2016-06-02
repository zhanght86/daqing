package com.midas.enums;

public enum DataType {
    // R: 原始数据清单
    // T: 二维数据
    // D: 三维数据
    // M: 中间数据
    RAW_DATA("R", "原始数据清单", "rDataService"),

    TWO_DATA("T", "二维数据", "tDataService"),

    THREE_DATA("D", "三维数据", "dDataService"),

    MIDDLE_DATA("M", "中间数据", "mDataService");

    private String type;
    private String value;
    private String bean;

    private DataType(String type, String value, String bean) {
        this.type = type;
        this.value = value;
        this.bean = bean;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public String getBean() {
        return bean;
    }

    @Override
    public String toString() {
        return type;
    }

}
