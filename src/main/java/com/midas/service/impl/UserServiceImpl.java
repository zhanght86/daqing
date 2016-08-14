package com.midas.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.midas.constant.ErrorConstant;
import com.midas.dao.UserDao;
import com.midas.exception.ServiceException;
import com.midas.service.UserService;
import com.midas.uitls.tools.CommonsUtils;

@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Override
    public PageInfo<Map<String, Object>> list(Map<String, Object> map, Page<?> page) {
        return userDao.list(map, page);
    }

    @Override
    public int getCountByCondition(Map<String, Object> map) {

        logger.info("验证用户名是否重复： {}", map);
        int result = userDao.getCountByCondition(map);
        logger.info("验证用户名是否重复： {}, 响应的结果为： {}", map, result);
        return result;
    }

    @Override
    public int addUser(Map<String, Object> map, int createUserId) throws ServiceException {

        int i = getCountByCondition(map);
        if (i > 0) {
            logger.info("用户记录存在:{}, 请检查username: {}", i, map);
            throw new ServiceException(ErrorConstant.CODE2000, "用户名重复");
        }

        String password = CommonsUtils.encryptPassword(ObjectUtils.toString(map.get("user_name")),
                ObjectUtils.toString(map.get("password")));

        map.put("password", password);
        map.put("created_user", createUserId);
        map.put("created_time", new Date());
        map.put("updatePassword_time", null);
        map.put("department_id", null);
        map.put("updated_user", null);
        map.put("updated_time", null);
        map.put("status", 1);
        return userDao.add(map);
    }

    @Override
    public boolean login(String username, String password) {
        String encrPassword = CommonsUtils.encryptPassword(username, password);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_name", username);
        map.put("password", encrPassword);
        List<Map<String, Object>> list = userDao.list(map);
        return null != list && !list.isEmpty() ? true : false;
    }

    @Override
    public boolean resetPassword(String username) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", username);
        map.put("password", CommonsUtils.encryptPassword(username, "123456"));
        int i = userDao.update(map);
        return i > 0 ? true : false;
    }
    
    @Override
    public int delete(String id) {
        if(null == id || "".equals(id)) {
           logger.error("删除不成功， 参数ID为空");
            return 0;
        }
        if("0".equals(id)) {
            logger.warn("0.为root用户不能删除！！！");
            return 0;
        }
        int result = userDao.delete(id);
        logger.info("根据编号： {}, 删除用户信息, 返回结果为: {}", id, result);
        return result;
    }

    public int updateUser(Map<String, Object> map, int updateUserId) {
        return 0;
    }
    
    @Override
    public Map<String, Object> getUserById(int id) {
        logger.info("根据编号：{}， 查找用户信息", id);
        Map<String, Object> map = userDao.getUserById(id);
        logger.info("查询结果为： {}", map);
        return map;
    }

}
