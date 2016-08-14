package com.midas.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.midas.constant.ErrorConstant;
import com.midas.dao.DownloadDataInfoDao;
import com.midas.exception.ServiceException;
import com.midas.mapper.DownloadDataInfoMapper;

/**
 * 下载
 * 
 * @author arron
 *
 */
@Repository
public class DownloadDataInfoDaoImpl implements DownloadDataInfoDao {

    @Autowired
    private DownloadDataInfoMapper baseMapper;

    @Override
    public PageInfo<Map<String, Object>> list(Map<String, Object> map, Page<?> page) {
        try {
            PageHelper.startPage(page.getPageNum(), page.getPageSize());
            List<Map<String, Object>> list = list(map);
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
            return pageInfo;
        } catch (Exception e) {
            throw new ServiceException(ErrorConstant.CODE2000, "查询数据列表失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> list(Map<String, Object> map) {
        return baseMapper.list(map);
    }

    @Override
    public int insert(Map<String, Object> map) {
        return baseMapper.insert(map);
    }

    @Override
    public int update(Map<String, Object> map) {
        return baseMapper.update(map);
    }

    @Override
    public Map<String, Object> getById(String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return baseMapper.get(map);
    }

    @Override
    public Map<String, Object> getByCondtion(Map<String, Object> map) {
        return baseMapper.getByCondtion(map);
    }

    @Override
    public int getCountByCondition(Map<String, Object> map) {
        return baseMapper.getCountByCondition(map);
    }

}
