package com.midas.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.midas.constant.ErrorConstant;
import com.midas.dao.DataDao;
import com.midas.exception.ServiceException;
import com.midas.mapper.DDataMapper;

@Repository("dDataDao")
public class DDataDaoImpl implements DataDao {

    @Autowired
    private DDataMapper baseMapper;

    @Override
    public PageInfo<Map<String, Object>> list(Map<String, Object> map, Page<?> page) {
        try {
            PageHelper.startPage(page.getPageNum(), page.getPageSize());
            List<Map<String, Object>> list = baseMapper.list(map);
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
            return pageInfo;
        } catch (Exception e) {
            throw new ServiceException(ErrorConstant.CODE2000, "查询数据列表失败", e);
        }
    }

    @Override
    public int insert(Map<String, Object> map) {
        try {
            return baseMapper.insert(map);
        } catch (Exception e) {
            throw new ServiceException(ErrorConstant.CODE2000, "添加数据失败", e);
        }
    }

    @Override
    public Map<String, Object> getByUniquePk(Map<String, Object> map) {
        try {
            return baseMapper.get(map);
        } catch (Exception e) {
            throw new ServiceException(ErrorConstant.CODE2000, "查找数据失败", e);
        }
    }

    @Override
    public int delete(Map<String, Object> map) {
        return baseMapper.delete(map);
    }

    @Override
    public List<Map<String, Object>> findByVolLabel(String volLabel) {
        return baseMapper.findByVolLabel(volLabel);
    }

    @Override
    public Map<String, Object> findById(String id) {
        return baseMapper.findById(id);
    }

    @Override
    public void update(Map<String, Object> map) {
        baseMapper.update(map);
    }
    
    @Override
    public void deleteByVolLabel(String volLabel) {
        baseMapper.deleteByVolLabel(volLabel);
    }

}
