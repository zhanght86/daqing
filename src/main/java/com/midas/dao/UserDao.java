package com.midas.dao;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

public interface UserDao {

    /**
     * 查询列表内容
     * 
     * @param map
     *            参数
     * @param page
     *            分页
     * @return PageInfo<Map<String, Object>>
     */
    public PageInfo<Map<String, Object>> list(Map<String, Object> map, Page<?> page);

    /**
     * 根据条件查询用户信息
     * 
     * @param map
     * @return
     */
    public List<Map<String, Object>> list(Map<String, Object> map);

    /**
     * 根据条件查询总记录条数
     * 
     * @param map
     *            查询条件
     * @return 记录条数
     */
    public int getCountByCondition(Map<String, Object> map);

    /**
     * 添加用户
     * 
     * @param map
     *            用户数据
     * @return 影响条数
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
     * 删除用户记录
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
     * @return
     */
    public Map<String, Object> getUserById(int id);

}
