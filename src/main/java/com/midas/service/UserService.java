package com.midas.service;

import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.midas.exception.ServiceException;

public interface UserService {

    /**
     * 通过条件查询用户信息
     * 
     * @param map
     *            查询条件
     * @return 分页数据 PageInfo<Map<String, Object>>
     */
    public PageInfo<Map<String, Object>> list(Map<String, Object> map, Page<?> page);

    /**
     * 根据条件查询记录条数
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
    public int addUser(Map<String, Object> map, int createUserId) throws ServiceException;

    /**
     * 修改用户信息
     * 
     * @param map
     * @param updateUserId
     * @return
     */
    public int updateUser(Map<String, Object> map, int updateUserId);

    /**
     * 用户登录
     * 
     * @param username
     *            用户名
     * @param password
     *            密码
     * @return
     */
    public boolean login(String username, String password);

    /**
     * 根据用户名重置密码
     * 
     * @param username
     * @return
     */
    public boolean resetPassword(String username);

    /**
     * 删除用户记录
     * 
     * @param id
     *            用户编号
     * @return 删除条数
     */
    public int delete(String id);

    /**
     * 根据编号获取对象
     * 
     * @param id
     *            用户编号
     * @return Map<String, Object>
     */
    public Map<String, Object> getUserById(int id);

}
