package com.midas.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BaseMapper {

    /**
     * 添加单条记录
     * 
     * @param map
     * @return
     */
    public int insert(Map<String, Object> map);

    /**
     * 根据条件删除指定记录
     * 
     * @param map
     * @return
     */
    public int delete(Map<String, Object> map);

    /**
     * 根据条件获取单条记录
     * 
     * @param map
     * @return
     */
    public Map<String, Object> get(Map<String, Object> map);

    /**
     * 根据条件获取多条记录
     * 
     * @param map
     * @return
     */
    public List<Map<String, Object>> list(Map<String, Object> map);

    /**
     * 批量添加数据
     * 
     * @param list
     * @return 影响记录条数
     */
    public int insertBatch(List<Map<String, Object>> list);

    /**
     * 根据唯一卷标号查找需要刻录的数据
     * 
     * @param volLabel
     * @return
     */
    public List<Map<String, Object>> findByVolLabel(String volLabel);
    /**
     * 修改数据
     */
    public int update(Map<String, Object> map);
    /**
     * 根据编号指定记录数据
     * @param id
     * @return
     */
    public Map<String, Object> findById(@Param("id")String id);
    
    /**
     * 根据卷标号删除指定内容
     * @param volLabel
     */
    void deleteByVolLabel(@Param("volume_label")String volLabel);

}
