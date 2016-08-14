package com.midas.vo;

import java.io.Serializable;

/**
 * 盘槽
 * 
 * @author arron
 *
 */
public class Slot implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4111182129457734066L;

    /**
     * 盘槽号
     */
    private int    id;
    /**
     * 0:未知 1:存在 2:不存在
     */
    private int    cdexist;
    /**
     * 0:未知 1:存在 2:不存在
     */
    private int    trayexist;
    /**
     * 0:未盘点 1:已盘点
     */
    private int    ischecked;
    /**
     * 0:非空 1:空盘 2:坏盘
     */
    private int    isblank;
    /**
     * 光盘类型
     */
    private String mediatype;
    /**
     * 光盘卷标
     */
    private String label;
    /**
     * 盘槽状态: 空闲:N(78) 导出:D(68) 出盘:E(69) 盘点:P(80) 刻录:B(66) 打印:C(67)
     */
    private int    slot_status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCdexist() {
        return cdexist;
    }

    public void setCdexist(int cdexist) {
        this.cdexist = cdexist;
    }

    public int getTrayexist() {
        return trayexist;
    }

    public void setTrayexist(int trayexist) {
        this.trayexist = trayexist;
    }

    public int getIschecked() {
        return ischecked;
    }

    public void setIschecked(int ischecked) {
        this.ischecked = ischecked;
    }

    public int getIsblank() {
        return isblank;
    }

    public void setIsblank(int isblank) {
        this.isblank = isblank;
    }

    public String getMediatype() {
        return mediatype;
    }

    public void setMediatype(String mediatype) {
        this.mediatype = mediatype;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getSlot_status() {
        return slot_status;
    }

    public void setSlot_status(int slot_status) {
        this.slot_status = slot_status;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + cdexist;
        result = prime * result + id;
        result = prime * result + isblank;
        result = prime * result + ischecked;
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        result = prime * result + ((mediatype == null) ? 0 : mediatype.hashCode());
        result = prime * result + slot_status;
        result = prime * result + trayexist;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Slot other = (Slot) obj;
        if (cdexist != other.cdexist)
            return false;
        if (id != other.id)
            return false;
        if (isblank != other.isblank)
            return false;
        if (ischecked != other.ischecked)
            return false;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        if (mediatype == null) {
            if (other.mediatype != null)
                return false;
        } else if (!mediatype.equals(other.mediatype))
            return false;
        if (slot_status != other.slot_status)
            return false;
        if (trayexist != other.trayexist)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Slot [id=" + id + ", cdexist=" + cdexist + ", trayexist=" + trayexist + ", ischecked=" + ischecked
                + ", isblank=" + isblank + ", mediatype=" + mediatype + ", label=" + label + ", slot_status="
                + slot_status + "]";
    }

}
