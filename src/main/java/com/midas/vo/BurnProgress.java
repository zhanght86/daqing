package com.midas.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 刻录进度信息
 * 
 * @author arron
 *
 */
public class BurnProgress implements Serializable {

    private static final long serialVersionUID = -4340851827953380469L;

    /**
     * 分盘数量
     */
    private int                       splitsNubmer;
    /**
     * 刻录的警告信息
     */
    private List<Map<String, String>> warnings;
    /**
     * 刻录结果
     */
    private List<Map<String, String>> ends;

    /**
     * 是否获取返回成功
     * 
     * @return true : 成功 <br />
     *         false ： 失败
     */
    private List<Map<String, String>> infos;
    public List<Map<String, String>> getInfos() {
        return infos;
    }

    public void setInfos(List<Map<String, String>> infos) {
        this.infos = infos;
    }

    public boolean isSuccess() {
        if (splitsNubmer > 0) {
            return true;
        }
        return false;
    }

    public int getSplitsNubmer() {
        return splitsNubmer;
    }

    public void setSplitsNubmer(int splitsNubmer) {
        this.splitsNubmer = splitsNubmer;
    }

    public List<Map<String, String>> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<Map<String, String>> warnings) {
        this.warnings = warnings;
    }

    public List<Map<String, String>> getEnds() {
        return ends;
    }

    public void setEnds(List<Map<String, String>> ends) {
        this.ends = ends;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ends == null) ? 0 : ends.hashCode());
        result = prime * result + splitsNubmer;
        result = prime * result + ((warnings == null) ? 0 : warnings.hashCode());
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
        BurnProgress other = (BurnProgress) obj;
        if (ends == null) {
            if (other.ends != null)
                return false;
        } else if (!ends.equals(other.ends))
            return false;
        if (splitsNubmer != other.splitsNubmer)
            return false;
        if (warnings == null) {
            if (other.warnings != null)
                return false;
        } else if (!warnings.equals(other.warnings))
            return false;
        else if (!infos.equals(other.infos))
        return false;
        return true;
    }

    @Override
    public String toString() {
        return "BurnProgress [splitsNubmer=" + splitsNubmer + ", warnings=" + warnings + ", ends=" + ends + "infos="+infos+"]";
    }
    
}
