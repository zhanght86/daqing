package com.midas.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.midas.model.user.Permission;
import com.midas.model.user.RolePermission;
import com.midas.vo.user.PermissionDto;
import com.midas.vo.user.PermissionForm;


public interface PermissionMapper{
	
	PermissionDto findByPermissionId(Map<String,Object> param);//y
	
	List<PermissionDto> findByPermissionCode(Map<String,Object> param);//y
	
	List<PermissionDto> findByPermissionName(Map<String,Object> param);
	
	List<PermissionDto> selectAllPermissions();

	List<PermissionDto> selectAvailablePermissionsForRole(Map<String,Object> param);
	
	List<PermissionDto> getRolePermissions(Map<String,Object> param);
	
	List<PermissionDto> loadAllMenu();
	
	/**
	 * 获取模块
	 * @return
	 */
	List<PermissionDto> loadAllModule();
	
	List<PermissionDto> loadAllFunction();
	
	void deleteRolePermissions(Map<String,Object> param);
	
	void insertRolePermission(RolePermission rolePermission);
	
	void updatePermission(Permission permission);
	
	List<PermissionDto> selectPermission(PermissionForm form,RowBounds rowBounds);
	
	Integer selectPermissionCount(PermissionForm form);
	
	int save(Permission permission);
	
	int update(Permission permission);
	
	int delete(Permission permission);
	
	Permission getPermissionById(@Param("permissionId")String permissionId);
}
