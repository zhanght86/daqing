package com.midas.dao.user.impl;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.midas.dao.user.PermissionDao;
import com.midas.mapper.PermissionMapper;
import com.midas.model.user.Permission;
import com.midas.model.user.RolePermission;
import com.midas.vo.SearchResult;
import com.midas.vo.user.PermissionDto;
import com.midas.vo.user.PermissionForm;



@Repository("permissionDao")
public class PermissionDaoMyBatis implements PermissionDao {
	
	@Autowired
	private PermissionMapper permissionMapper;

	@Override
	public PermissionDto findById(Long permissionId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("permissionId", permissionId);
		
		return permissionMapper.findByPermissionId(params);
	}

	@Override
	public List<PermissionDto> findByPermissionCode(String permissionCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("permissionCode", permissionCode);
		
		return permissionMapper.findByPermissionCode(params);
	}

	@Override
	public List<PermissionDto> findByPermissionName(String permissionName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("permissionName", permissionName);
		
		return permissionMapper.findByPermissionName(params);
	}

	@Override
	public List<PermissionDto> selectAllPermissions() {
		return permissionMapper.selectAllPermissions();
	}

	@Override
	public List<PermissionDto> selectAvailablePermissionsForRole(Long roleId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleId", roleId);
		
		return permissionMapper.selectAvailablePermissionsForRole(params);
	}

	@Override
	public List<PermissionDto> loadRolePermissions(Long roleId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleId", roleId);
		return permissionMapper.getRolePermissions(params);
	}
	
	@Override
	public List<PermissionDto> loadAllMenu() {
		return permissionMapper.loadAllMenu();
	}
	
	@Override
	public List<PermissionDto> loadAllModule() {
		return permissionMapper.loadAllModule();
	}
	
	@Override
	public List<PermissionDto> loadAllFunction() {
		return permissionMapper.loadAllFunction();
	}

	@Override
	public void deleteRolePermissions(Long roleId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleId", roleId);
		
		permissionMapper.deleteRolePermissions(params);
	}
	
	public void insertRolePermission(RolePermission rolePermission) {
		permissionMapper.insertRolePermission(rolePermission);
	}

	@Override
	public void insertPermission(Permission permission) {
		permissionMapper.save(permission);
	}

	@Override
	public void updatePermission(Permission permission) {
		permissionMapper.updatePermission(permission);
	}


	@Override
	public int save(Permission permission) {
		return permissionMapper.save(permission);
	}

	@Override
	public int update(Permission permission) {
		return permissionMapper.update(permission);
	}

	@Override
	public int delete(Permission permission) {
		return permissionMapper.delete(permission);
	}

	@Override
	public Permission getPermissionById(String permissionId) {
		return permissionMapper.getPermissionById(permissionId);
	}

	@Override
	public SearchResult<PermissionDto> selectPermission(PermissionForm form) {
		SearchResult<PermissionDto> result=new SearchResult<PermissionDto>();
		Integer count=permissionMapper.selectPermissionCount(form);
		if(count>0){
			List<PermissionDto> list=permissionMapper.selectPermission(form,form.getRowBounds());
			result.setRows(list);
			result.setTotal(count);
		}
		return result;
	}

}
