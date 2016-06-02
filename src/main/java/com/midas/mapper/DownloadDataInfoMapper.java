package com.midas.mapper;

import java.util.Map;

public interface DownloadDataInfoMapper extends BaseMapper {

    /**
     * 更新指定记录
     * 
     * @param map
     */
    public int update(Map<String, Object> map);

    public Map<String, Object> getByCondtion(Map<String, Object> map);

    public int getCountByCondition(Map<String, Object> map);

}
