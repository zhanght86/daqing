package com.midas.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.midas.dao.StandingbookDao;
import com.midas.mapper.StandingbookMapper;
import com.midas.vo.StandingBook;

@Repository
public class StandingbookDaoImpl implements StandingbookDao {

    @Autowired
    private StandingbookMapper mapper;
    
    @Override
    public PageInfo<Map<String, Object>> queryStandingbookListByPage(Map<String, Object> map, Page<?> page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<Map<String, Object>> list;
        PageInfo<Map<String, Object>> pageInfo;
        try {
            list = mapper.queryStandingbookListByPage(map);
          pageInfo = new PageInfo<>(list);
          return pageInfo;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
        return null;
      
    }
    
    @Override
    public int insert(Map<String, Object> map) {
        return mapper.insert(map);
    }
    
    @Override
    public int insertBatch(Map<String, Object> map) {
        return mapper.insertBatch(map);
    }
    
    @Override
    public int update(Map<String, Object> map) {
        return mapper.update(map);
    }
    
    @Override
    public List<Map<String, Object>> getStandingbook(Map<String, Object> map) {
        return mapper.getStandingbook(map);
    }

    @Override
    public int save(StandingBook vo) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(Map<String, Object> map) {
        // TODO Auto-generated method stub
        return mapper.delete(map);
    }

}
