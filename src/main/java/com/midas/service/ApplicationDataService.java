package com.midas.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

public interface ApplicationDataService {

	/**
	 * 通过条件查询原始数据清单
	 * 
	 * @param map
	 *            查询条件
	 * @return 分页数据 PageInfo<Map<String, Object>>
	 */
	public PageInfo<Map<String, Object>> list(Map<String, Object> map, Page<?> page);

	public int insertApplication(Map<String, Object> map) throws Exception;



	int updateApplication(Map<String, Object> map) throws Exception;

	PageInfo<Map<String, Object>> queryApplication(Map<String, Object> map, Page<?> page) throws Exception;
}
