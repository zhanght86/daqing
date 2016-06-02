package com.midas.vo.offline;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.midas.vo.Mag;

public class OfflineMachine implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5174625381733844803L;
    /**
     * 机器类型
     */
    @SerializedName("DeviceType")
    private String            deviceType;
    /**
     * 机器名
     */
    @SerializedName("HostName")
    private String            hostName;
    /**
     * 是否开门
     */
    @SerializedName("DoorStatus")
    private int               doorStatus;
    /**
     * 盘仓
     */
    @SerializedName("Mag")
    private List<Mag>         mag;

    /**
     * 为true 开门， false： 关门
     * 
     * @return
     */
    public boolean isOpenDoor() {
        if (doorStatus == 0) {
            return true;
        }
        return false;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getDoorStatus() {
        return doorStatus;
    }

    public void setDoorStatus(int doorStatus) {
        this.doorStatus = doorStatus;
    }

    public List<Mag> getMag() {
        return mag;
    }

    public void setMag(List<Mag> mag) {
        this.mag = mag;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((deviceType == null) ? 0 : deviceType.hashCode());
        result = prime * result + doorStatus;
        result = prime * result + ((hostName == null) ? 0 : hostName.hashCode());
        result = prime * result + ((mag == null) ? 0 : mag.hashCode());
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
        OfflineMachine other = (OfflineMachine) obj;
        if (deviceType == null) {
            if (other.deviceType != null)
                return false;
        } else if (!deviceType.equals(other.deviceType))
            return false;
        if (doorStatus != other.doorStatus)
            return false;
        if (hostName == null) {
            if (other.hostName != null)
                return false;
        } else if (!hostName.equals(other.hostName))
            return false;
        if (mag == null) {
            if (other.mag != null)
                return false;
        } else if (!mag.equals(other.mag))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "OfflineMachine [deviceType=" + deviceType + ", hostName=" + hostName + ", doorStatus=" + doorStatus
                + ", mag=" + mag + "]";
    }

}
