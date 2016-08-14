package com.midas.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.midas.dao.DownloadDataInfoDao;
import com.midas.enums.DataDownState;
import com.midas.service.DownloadDataInfoService;

@Service
public class DownloadDataInfoServiceImpl implements DownloadDataInfoService {

    @Autowired
    private DownloadDataInfoDao downloadDataDao;

    @Override
    public List<Map<String, Object>> list(Map<String, Object> map) {
        return downloadDataDao.list(map);
    }

    @Override
    public PageInfo<Map<String, Object>> list(Map<String, Object> map, Page<?> page) {
        return downloadDataDao.list(map, page);
    }

    @Override
    public int update(Map<String, Object> map) {
        map.put("udate", new Date());
        return downloadDataDao.update(map);
    }

    @Override
    public int getCountByCondition(Map<String, Object> map) {
        return downloadDataDao.getCountByCondition(map);
    }

    // TODO 初始到下载中
    @Override
    public synchronized Map<String, Object> updateBySelect() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("state", DataDownState.INIT.getKey());
        Map<String, Object> resultMap = downloadDataDao.getByCondtion(map);

        if (null == resultMap || resultMap.isEmpty()) {
            return null;
        }
        // 设置为开始下载
        resultMap.put("state", DataDownState.BEGIN_DOWNLOAD.getKey());
        map.clear();
        map.put("id", resultMap.get("id"));
        map.put("state", DataDownState.BEGIN_DOWNLOAD.getKey());

        downloadDataDao.update(map);
        return resultMap;
    }

//    // TODO 下载完成
//    public synchronized Map<String, Object> updateByDownload(Map<String, Object> map) {
//
//    }
//
//    // TODO 开始刻录
//    public synchronized Map<String, Object> updateByDownload(Map<String, Object> map) {
//
//    }
//
//    // TODO 刻录完成
//    public synchronized Map<String, Object> updateByDownload(Map<String, Object> map) {
//
//    }

    @Override
    public int insert(Map<String, Object> map) {
        // 设置插入为当前时间
        map.put("cdate", new Date());
        map.put("state", DataDownState.INIT.getKey());
        return downloadDataDao.insert(map);
    }

}
