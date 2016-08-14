package com.midas.vo.burn;

import java.io.Serializable;

/**
 * 打印机
 * 
 * @author arron
 *
 */
public class Printer implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8819345423523185845L;

    /**
     * 插入状态: 0：未插入 1：已插入
     */
    private int status;
    /**
     * 是否存在光盘 0:未知 1:存在 2:不存在
     */
    private int ishavemedia;
    /**
     * 地址（有光盘的话是光盘所在的盘槽号）
     */
    private int cd_src;

    /**
     * 检查打印机的繁忙状态
     * 
     * @return true : 忙 <br />
     *         false : 闲置
     */
    public boolean isBusy() {
        if (ishavemedia == 0 || ishavemedia == 2) {
            return true;
        }
        if (status == 0) {
            return true;
        }
        return false;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIshavemedia() {
        return ishavemedia;
    }

    public void setIshavemedia(int ishavemedia) {
        this.ishavemedia = ishavemedia;
    }

    public int getCd_src() {
        return cd_src;
    }

    public void setCd_src(int cd_src) {
        this.cd_src = cd_src;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + cd_src;
        result = prime * result + ishavemedia;
        result = prime * result + status;
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
        Printer other = (Printer) obj;
        if (cd_src != other.cd_src)
            return false;
        if (ishavemedia != other.ishavemedia)
            return false;
        if (status != other.status)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Printer [status=" + status + ", ishavemedia=" + ishavemedia + ", cd_src=" + cd_src + "]";
    }

}
