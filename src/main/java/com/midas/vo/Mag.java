package com.midas.vo;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 盘仓
 * 
 * @author arron
 *
 */
public class Mag implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8651558944020483762L;
    /**
     * 盘仓号
     */
    @SerializedName("MagNo")
    private int        magNo;
    /**
     * 电子标签
     */
    @SerializedName("Rfid")
    private String     rfid;
    /**
     * 盘槽
     */
    @SerializedName("Slot")
    private List<Slot> slot;

    public int getMagNo() {
        return magNo;
    }

    public void setMagNo(int magNo) {
        this.magNo = magNo;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public List<Slot> getSlot() {
        return slot;
    }

    public void setSlot(List<Slot> slot) {
        this.slot = slot;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + magNo;
        result = prime * result + ((rfid == null) ? 0 : rfid.hashCode());
        result = prime * result + ((slot == null) ? 0 : slot.hashCode());
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
        Mag other = (Mag) obj;
        if (magNo != other.magNo)
            return false;
        if (rfid == null) {
            if (other.rfid != null)
                return false;
        } else if (!rfid.equals(other.rfid))
            return false;
        if (slot == null) {
            if (other.slot != null)
                return false;
        } else if (!slot.equals(other.slot))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Mag [magNo=" + magNo + ", rfid=" + rfid + ", slot=" + slot + "]";
    }

}
