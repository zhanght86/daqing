package com.midas.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.midas.dao.CommonDao;
import com.midas.mapper.CommonMapper;

@Repository
public class CommonDaoImp implements CommonDao {

    @Autowired
    private CommonMapper commonMapper;

    @Override
    public int seqUniqueNextVal(String cdate, String ctype) {
        return commonMapper.seqUniqueNextVal(cdate, ctype);
    }

    @Override
    public List<Map<String, Object>> listSystemParameters(Map<String, Object> map) {
        return commonMapper.listSystemParameters(map);
    }

}
