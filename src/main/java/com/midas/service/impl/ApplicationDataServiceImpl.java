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
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.midas.constant.ErrorConstant;
import com.midas.dao.DaoSupport;
import com.midas.dao.DataDao;
import com.midas.enums.BurnState;
import com.midas.enums.DataType;
import com.midas.exception.ServiceException;
import com.midas.service.ApplicationDataService;
import com.midas.service.DataService;
import com.midas.service.StandingbookService;
import com.midas.uitls.date.DateStyle;
import com.midas.uitls.date.DateUtil;
import com.midas.uitls.tools.StringTools;

/**
 * 原始数据
 * 
 * @author arron
 *
 */
@Service("ApplicationDataService")
public class ApplicationDataServiceImpl extends DataBase implements ApplicationDataService {

    private Logger logger = LoggerFactory.getLogger(ApplicationDataServiceImpl.class);
    
    @Autowired
    private DaoSupport dao;

	@Override
	public PageInfo<Map<String, Object>> list(Map<String, Object> map, Page<?> page) {
		
		try {
			PageHelper.startPage(page.getPageNum(), page.getPageSize());
			List<Map<String, Object>> list = (List<Map<String, Object>>)dao.findForList("com.midas.mapper.AppDataMapper.list", map);
			PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
			return pageInfo;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return null;
	}

	@Override
	public int insertApplication(Map<String, Object> map) throws Exception{
	
			dao.save("com.midas.mapper.AppDataMapper.insertApplicationData", map);
	
			return 0;
	}
	
	@Override
	public int updateApplication(Map<String, Object> map) throws Exception{
	
		dao.update("com.midas.mapper.AppDataMapper.updateApplicationData", map);
	
			return 0;
	}

	@Override
	public PageInfo<Map<String, Object>> queryApplication(Map<String, Object> map,Page<?> page) throws Exception{

			
			try {
				PageHelper.startPage(page.getPageNum(), page.getPageSize());
				List<Map<String, Object>> list = (List<Map<String, Object>>)dao.findForList("com.midas.mapper.AppDataMapper.queryApplication", map);
				PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
				return pageInfo;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			return null;
		
	}




  

}
