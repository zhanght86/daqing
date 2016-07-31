package com.midas.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.midas.constant.ErrorConstant;
import com.midas.constant.SysConstant;
import com.midas.dao.DataDao;
import com.midas.enums.BurnState;
import com.midas.enums.DataSource;
import com.midas.enums.DataType;
import com.midas.exception.ServiceException;
import com.midas.service.BurnBusiness;
import com.midas.service.CommonService;
import com.midas.service.DataService;
import com.midas.service.StandingbookService;
import com.midas.uitls.date.DateStyle;
import com.midas.uitls.date.DateUtil;

/**
 * 三维数据
 * 
 * @author arron
 *
 */
@Service("dDataService")
public class DDataServiceImpl extends DataBase implements DataService {

    private Logger logger = LoggerFactory.getLogger(DDataServiceImpl.class);

    @Autowired
    @Qualifier("dDataDao")
    private DataDao dataDao;
    
	@Autowired
	private BurnBusiness burnBusiness;
	@Autowired
	private CommonService commonService;

    @Autowired
    private StandingbookService standingbookService;
    
    @Override
    public PageInfo<Map<String, Object>> list(Map<String, Object> map, Page<?> page) {
        return dataDao.list(map, page);
    }

    @Override
    public int insert(Map<String, Object> map, String valLabel) {

        // Map<String, Object> resultMap = dataDao.getByUniquePk(map);
        // if (null != resultMap && !resultMap.isEmpty()) {
        // throw new ServiceException(ErrorConstant.CODE2000,
        // "重复的数据：" + map.get("work_area") + ", " +
        // map.get("construction_year"));
        // }
        try {
            
            Map<String, Object> standingMap = new HashMap<String, Object>();
            standingMap.put("volume_label", valLabel);
            standingMap.put("data_type", DataType.THREE_DATA.getType());
            standingMap.put("work_area", map.get("project_name"));
            standingMap.put("construction_unit", map.get("filing_unit"));
            standingMap.put("construction_year", map.get("filing_date"));
            standingMap.put("create_time", new Date());
            standingMap.put("type", "1");
            standingbookService.insert(standingMap);
            
            String cdate = DateUtil.getLastDay(DateStyle.YYYYMMDD);
            map.put("volume_label", valLabel);
            map.put("create_date", cdate);
            map.put("state", BurnState.INPUT_SUCCESS.getKey());
            logger.info("insnert RawData param: {}", map);

            return dataDao.insert(map);
        } catch (Exception e) {
            throw new ServiceException(ErrorConstant.CODE2000, "添加数据异常", e);
        }
    }

    @Override
    public int insertBetch(List<Map<String, Object>> list, String valLabel) {
        int i = 0;
        for (Map<String, Object> map : list) {
            int result = insert(map, valLabel);
            i = i + result;
        }
        logger.info("总记录条数为： {}， 添加成功条数为： {}", list != null ? list.size() : "0", i);
        return i;
    }

    @Override
    public int delete(String sid) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sid", sid);
        return dataDao.delete(map);
    }

    @Override
    public List<Map<String, Object>> findByVolLabel(String volLabel) {
        return dataDao.findByVolLabel(volLabel);
    }

    @Override
    public List<String> getFilepath(String volLabel,String dataSource) {
        List<Map<String, Object>> list = dataDao.findByVolLabel(volLabel);
        String sourceWorkdir = null;
        if (null == list || list.isEmpty()) {
            throw new ServiceException(ErrorConstant.CODE2000, "没有需要刻录的数据内容");
        }        
     
        if (DataSource.HARD_DISK.getKey().equals(dataSource)) {
            // 移动硬盘
            sourceWorkdir = burnBusiness.getLocalPath(volLabel);
        } else if (DataSource.NETWORK_SHARING.getKey().equals(dataSource)) {
            // 网络共享
            Map<String, Object> map = commonService.getSystemParameters(SysConstant.SHARED_NETWORK_ADDRESS);
            sourceWorkdir = ObjectUtils.toString(map.get("sp_value4"));
        }
        
        List<String> resultList = new ArrayList<String>();
        for (Map<String, Object> map : list) {
            // resultList.add(ObjectUtils.toString(map.get("project_name"))+"/"+ObjectUtils.toString(map.get("tape_number")));
        	//List<String> fileAll = getAllFiles(ObjectUtils.toString(map.get("project_name")), ObjectUtils.toString(map.get("record_content")), 1);
        	List<String> fileAll = getAllFilesByFolder( sourceWorkdir, ObjectUtils.toString(map.get("project_name")),1);
            resultList.addAll(fileAll);
        }
        logger.info("文件列表未： {}", resultList);
        return resultList;
    }

    @Override
    public Map<String, Object> findById(String id) {
        return dataDao.findById(id);
    }

    @Override
    public void update(Map<String, Object> map) {
        dataDao.update(map);
    }
    
    @Override
    public void deleteByVolLabel(String volLabel) {
        dataDao.deleteByVolLabel(volLabel);
    }

}
