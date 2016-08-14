package com.midas.vo;

import java.io.Serializable;
import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
 

public class StandingBook implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public int getSid() {
        return sid;
    }
    public void setSid(int sid) {
        this.sid = sid;
    }
    public String getVolume_label() {
        return volume_label;
    }
    public void setVolume_label(String volume_label) {
        this.volume_label = volume_label;
    }
    public String getData_type() {
        return data_type;
    }
    public void setData_type(String data_type) {
        this.data_type = data_type;
    }
    public String getWork_area() {
        return work_area;
    }
    public void setWork_area(String work_area) {
        this.work_area = work_area;
    }
    public String getConstruction_unit() {
        return construction_unit;
    }
    public void setConstruction_unit(String construction_unit) {
        this.construction_unit = construction_unit;
    }
    public String getConstruction_year() {
        return construction_year;
    }
    public void setConstruction_year(String construction_year) {
        this.construction_year = construction_year;
    }
    public String getStates() {
        return states;
    }
    public void setStates(String states) {
        this.states = states;
    }
    public int getData_quantity() {
        return data_quantity;
    }
    public void setData_quantity(int data_quantity) {
        this.data_quantity = data_quantity;
    }
    public String getBurn_count() {
        return burn_count;
    }
    public void setBurn_count(String burn_count) {
        this.burn_count = burn_count;
    }
    public String getCreate_time() {
        return create_time;
    }
    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
    public String getUpdate_time() {
        return update_time;
    }
    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    @SerializedName("sid")
    private int sid ;//编号
    @SerializedName("volume_label")
    private String volume_label;//卷标号
    private String data_type;//数据类型
    private String work_area ;//工区
    private String construction_unit ;//单位
    private String construction_year;//日期
    private String states  ;//状态
    private int data_quantity ;//数据大小
    private String  burn_count;//刻盘数量
    private String  create_time;//创建时间
    private String update_time ;//刻盘耗时



}
