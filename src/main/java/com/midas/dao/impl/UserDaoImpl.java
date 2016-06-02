package com.midas.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.midas.dao.UserDao;
import com.midas.mapper.UserMapper;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageInfo<Map<String, Object>> list(Map<String, Object> map, Page<?> page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<Map<String, Object>> list = userMapper.list(map);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
        return pageInfo;
    }

    @Override
    public List<Map<String, Object>> list(Map<String, Object> map) {
        return userMapper.list(map);
    }

    @Override
    public int getCountByCondition(Map<String, Object> map) {
        return userMapper.getCountByCondition(map);
    }

    @Override
    public int add(Map<String, Object> map) {
        return userMapper.add(map);
    }

    @Override
    public int update(Map<String, Object> map) {
        map.put("updated_time", new Date());
        return userMapper.update(map);
    }
    
    @Override
    public int delete(String id) {
        return userMapper.delete(id);
    }
    
    @Override
    public Map<String, Object> getUserById(int id) {
        return userMapper.getUserById(id);
    }
    
}
