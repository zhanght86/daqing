package com.midas.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 数据库操作
 * 
 * @author arron
 *
 */
public interface UserMapper {

    /**
     * 查询用户列表
     * 
     * @param map
     *            请求参数
     * @return List<Map<String, Object>> 想要内容
     */
    public List<Map<String, Object>> list(Map<String, Object> map);

    /**
     * 根据条件查询记录条数
     * 
     * @param map
     *            查询条件
     * @return
     */
    public int getCountByCondition(Map<String, Object> map);

    /**
     * 条件用户
     * 
     * @param map
     * @return
     */
    public int add(Map<String, Object> map);

    /**
     * 更新用户信息
     * 
     * @param map
     * @return
     */
    public int update(Map<String, Object> map);

    /**
     * 删除记录
     * 
     * @param id
     * @return
     */
    public int delete(String id);

    /**
     * 根据编号查找用户信息
     * 
     * @param id
     *            用户编号
     * @return Map
     */
    public Map<String, Object> getUserById(@Param("id")int id);

}
