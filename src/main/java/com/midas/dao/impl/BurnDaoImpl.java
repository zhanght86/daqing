package com.midas.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.midas.dao.BurnDao;
import com.midas.mapper.BurnMapper;

@Repository
public class BurnDaoImpl implements BurnDao {

	@Autowired
	private BurnMapper mapper;

	@Override
	public int insert(Map<String, Object> map) {
		return mapper.insert(map);
	}

	@Override
	public int update(Map<String, Object> map) {
		return mapper.update(map);
	}

	@Override
	public List<Map<String, Object>> list(Map<String, Object> map) {
		return mapper.list(map);
	}

	@Override
	public PageInfo<Map<String, Object>> list(Map<String, Object> map, Page<Map<String, Object>> page) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		List<Map<String, Object>> list = mapper.list(map);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
		return pageInfo;
	}

	@Override
	public Map<String, Object> getLast(Map<String, Object> map) {
		return mapper.getLast(map);
	}

	@Override
	public Map<String, Object> getBurnByVolLabel(String volLabel) {
		return mapper.getBurnByVolLabel(volLabel);
	}

	@Override
	public boolean updateState(Map<String, Object> map) {
		map.put("update_time", new Date());
		return mapper.updateState(map);
	}

	@Override
	public List<Map<String, Object>> listPosition(String volLabel) {
		return mapper.listPosition(volLabel);
	}

	@Override
	public List<Map<String, Object>> getBurnDetailByCondition(String volLabel, String electronicTag,
			String discPosition) {
		return mapper.getBurnDetailByCondition(volLabel, electronicTag, discPosition);
	}

	@Override
	public int insertDetail(Map<String, Object> map) {
		return mapper.insertDetail(map);
	}

	@Override
	public List<Map<String, Object>> listExportRecord(String volLabel, String state, String task_name) {
		return mapper.listExportRecord(volLabel, state, task_name);
	}

	@Override
	public int insertExportRecord(Map<String, Object> map) {
		map.put("create_time", new Date());
		return mapper.insertExportRecord(map);
	}

	@Override
	public int updateExportRecord(Map<String, Object> map) {
		map.put("update_time", new Date());
		return mapper.updateExportRecord(map);
	}

	@Override
	public List<Map<String, Object>> listExportRecord(String volLabel, String task_name) {
		return mapper.listExportRecordByVol(volLabel, task_name);
	}

	@Override
	public void updateSize(Map<String, Object> map) {
		mapper.updateSize(map);
	}

	@Override
	public void delete(String volLabel) {
		mapper.delete(volLabel);
	}

	@Override
	public void deleteDetail(String volLabel) {
		mapper.deleteDetail(volLabel);
	}

	@Override
	public void deleteExport(String eid) {
		// TODO Auto-generated method stub
		mapper.deleteExport(eid);
	}

	@Override
	public int insertExportFileRecord(Map<String, Object> map) {
		map.put("create_time", new Date());
		return mapper.insertExportFileRecord(map);
	}

	@Override
	public List<Map<String, Object>> listExportTask(String param) {
		// TODO Auto-generated method stub
		return mapper.listExportTask(param);
	}

	@Override
	public int updateExportFile(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.updateExportFile(map);
	}

	@Override
	public List<Map<String, Object>> listExportRecordCheck(String volLabel, String state, String task_name) {
		return mapper.listExportRecord(volLabel, state, task_name);
	}

}
