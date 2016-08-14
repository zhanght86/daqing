package com.midas.dao.user;


import java.util.List;

import com.midas.model.user.Permission;
import com.midas.model.user.RolePermission;
import com.midas.vo.SearchResult;
import com.midas.vo.user.PermissionDto;
import com.midas.vo.user.PermissionForm;


public interface PermissionDao {
	
	PermissionDto findById(Long permissionId);
	
	List<PermissionDto> findByPermissionCode(String permissionCode);//y
	
	List<PermissionDto> findByPermissionName(String permissionName);
	
	List<PermissionDto> selectAllPermissions();
	
	List<PermissionDto> selectAvailablePermissionsForRole(Long roleId);
	
	List<PermissionDto> loadRolePermissions(Long roleId);
	
	List<PermissionDto> loadAllMenu();
	
	/**
	 * 获取模块
	 * @return
	 */
	List<PermissionDto> loadAllModule();
	
	void deleteRolePermissions(Long roleId);
	
	void insertRolePermission(RolePermission rolePermission);
	
	void insertPermission(Permission permission);
	
	void updatePermission(Permission permission);
	
	SearchResult<PermissionDto> selectPermission(PermissionForm form);
	
	int save(Permission permission);
	
	int update(Permission permission);
	
	int delete(Permission permission);
	
	Permission getPermissionById(String permissionId);

	List<PermissionDto> loadAllFunction();
}
