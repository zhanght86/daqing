package com.midas.vo.burn;

import java.io.Serializable;

/**
 * 驱动器
 * 
 * @author arron
 *
 */
public class Drivers implements Serializable {

    private static final long serialVersionUID = -1535655958947564095L;

    /**
     * 驱动器ID
     */
    private String id;
    /**
     * 是否存在光盘 0. 未知， 1. 存在， 2. 不存在
     */
    private int    ishavemedia;
    /**
     * 地址（有光盘的话是光盘所在的盘槽号)
     */
    private int    cd_src;
    /**
     * 未知
     */
    private String progress;

    /**
     * 只有在未知或者存在的情况下返回true证明繁忙 如果驱动器为空的情况下不判断
     * 
     * @return true:繁忙<br />
     *         false:空闲
     */
    public boolean isBusy() {
        if (ishavemedia == 0 || ishavemedia == 1) {
            return true;
        }
        return false;
    }

    /**
     * 是否可用驱动器，
     * 
     * @return true 可用<br />
     *         false 不可用
     */
    public boolean isValid() {
        if (null == id || "".equals(id.trim())) {
            return false;
        }
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + cd_src;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ishavemedia;
        result = prime * result + ((progress == null) ? 0 : progress.hashCode());
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
        Drivers other = (Drivers) obj;
        if (cd_src != other.cd_src)
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (ishavemedia != other.ishavemedia)
            return false;
        if (progress == null) {
            if (other.progress != null)
                return false;
        } else if (!progress.equals(other.progress))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Drivers [id=" + id + ", ishavemedia=" + ishavemedia + ", cd_src=" + cd_src + ", progress=" + progress
                + "]";
    }

}
