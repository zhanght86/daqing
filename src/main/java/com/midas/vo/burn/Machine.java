package com.midas.vo.burn;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.midas.vo.Mag;

/**
 * 盘库机器
 * 
 * @author arron
 *
 */
public class Machine implements Serializable {

    private static final long serialVersionUID = -6820895988477010433L;
    /**
     * 机器类型
     */
    @SerializedName("MachineType")
    private String            machineType;
    /**
     * 开门状态 0:关门 1：开门
     */
    @SerializedName("DoorStatus")
    private int               doorStatus;
    @SerializedName("EventCnt")
    private int               eventCnt;
    @SerializedName("Mag")
    private List<Mag>         mag;
    @SerializedName("Drivers")
    private List<Drivers>     dirvers;
    @SerializedName("Printer")
    private Printer           printer;

    /**
     * 是否开门
     * 
     * @return true ： 开门， false：关门
     */
    public boolean isOpenDoor() {
        if (doorStatus == 1) {
            return true;
        }
        return false;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
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

    public List<Drivers> getDirvers() {
        return dirvers;
    }

    public void setDirvers(List<Drivers> dirvers) {
        this.dirvers = dirvers;
    }

    public Printer getPrinter() {
        return printer;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dirvers == null) ? 0 : dirvers.hashCode());
        result = prime * result + doorStatus;
        result = prime * result + eventCnt;
        result = prime * result + ((machineType == null) ? 0 : machineType.hashCode());
        result = prime * result + ((mag == null) ? 0 : mag.hashCode());
        result = prime * result + ((printer == null) ? 0 : printer.hashCode());
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
        Machine other = (Machine) obj;
        if (dirvers == null) {
            if (other.dirvers != null)
                return false;
        } else if (!dirvers.equals(other.dirvers))
            return false;
        if (doorStatus != other.doorStatus)
            return false;
        if (eventCnt != other.eventCnt)
            return false;
        if (machineType == null) {
            if (other.machineType != null)
                return false;
        } else if (!machineType.equals(other.machineType))
            return false;
        if (mag == null) {
            if (other.mag != null)
                return false;
        } else if (!mag.equals(other.mag))
            return false;
        if (printer == null) {
            if (other.printer != null)
                return false;
        } else if (!printer.equals(other.printer))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Machine [machineType=" + machineType + ", doorStatus=" + doorStatus + ", eventCnt=" + eventCnt
                + ", mag=" + mag + ", dirvers=" + dirvers + ", printer=" + printer + "]";
    }

}
