package com.midas.vo;

public class FileVo {

private String filepath;
public String getFilepath() {
    return filepath;
}
public void setFilepath(String filepath) {
    this.filepath = filepath;
}
public String getVol() {
    return vol;
}
public void setVol(String vol) {
    this.vol = vol;
}
private String vol;
private int id;
private String splitstatus;
public String getSplitstatus() {
    return splitstatus;
}
public void setSplitstatus(String splitstatus) {
    this.splitstatus = splitstatus;
}
public String getSplitcount() {
    return splitcount;
}
public void setSplitcount(String splitcount) {
    this.splitcount = splitcount;
}
private String splitcount;
private String splitnames;
public String getSplitnames() {
    return splitnames;
}
public void setSplitnames(String splitnames) {
    this.splitnames = splitnames;
}
public int getId() {
    return id;
}
public void setId(int id) {
    this.id = id;
}
}
