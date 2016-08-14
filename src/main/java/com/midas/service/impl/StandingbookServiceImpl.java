package com.midas.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.midas.dao.StandingbookDao;
import com.midas.service.StandingbookService;
import com.midas.vo.StandingBook;

@Service
public class StandingbookServiceImpl implements StandingbookService {

    @Autowired
    private StandingbookDao dao;
    
    @Override
    public PageInfo<Map<String, Object>> queryStandingbookListByPage(Map<String, Object> map, Page<?> page) {
        return dao.queryStandingbookListByPage(map, page);
    }

    @Override
    public int insert(Map<String, Object> map) {
        int result = 0;
        List<Map<String, Object>> list = dao.getStandingbook(map);
        if(list == null || list.isEmpty()) {
            result = dao.insert(map);
        }
        return result;
    }
    
    @Override
    public int update(Map<String, Object> map) {
        return dao.update(map);
    }
    
    public int save(StandingBook vo){
       Map<String, Object> map=new HashMap<String, Object>();
       map.put("sid",vo.getSid());
       map.put("volume_label",vo.getVolume_label());//卷标号
      map.put("data_type",vo.getData_type());//数据类型
      map.put("work_area",vo.getWork_area());//工区
      map.put("construction_unit",vo.getConstruction_unit()) ;//单位
      map.put("construction_year",vo.getConstruction_year());//日期
      map.put("states",vo.getStates()) ;//状态
      map.put("data_quantity",vo.getData_quantity());//数据大小
      map.put("burn_count",vo.getBurn_count());//刻盘数量
      map.put("update_time",vo.getUpdate_time());//刻盘耗时
       map.put("volume_label",vo.getVolume_label());
       map.put("create_time", vo.getCreate_time());
        dao.insertBatch(map);
        return 1;
        
        
    }

    @Override
    public int delete(String sid) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sid", sid);
      
        return dao.delete(map);
    }

    @Override
    public List<Map<String, Object>> getStandingbook(Map<String, Object> map) {
        // TODO Auto-generated method stub
        return dao.getStandingbook(map);
    }
    
}
