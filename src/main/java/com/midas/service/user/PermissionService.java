package com.midas.service.user;



import java.util.List;

import com.midas.model.user.Permission;
import com.midas.vo.SearchResult;
import com.midas.vo.user.PermissionDto;
import com.midas.vo.user.PermissionForm;



public interface PermissionService {

	List<PermissionDto> selectAllPermissions();
	
	PermissionDto findByPermissionCode(String permissionCode);
	
	PermissionDto findByPermissionName(String permissionName);
	
	List<PermissionDto> loadRolePermissions(Long roleId);
	
	PermissionDto findById(Long permissionId);
	
	List<PermissionDto> loadAllMenu();
	
	/**
	 * 获取模块
	 * @return
	 */
	List<PermissionDto> loadAllModule();
	
	List<PermissionDto> loadAllFunction();
	
	void updateRolePermissions(Long roleId, Long [] selectedPermissionIds);
	
	int updatePermission(Permission updatedPermission);
	
	SearchResult<PermissionDto> selectPermission(PermissionForm form);
	
	int save(Permission permission);
	
	int update(Permission permission);
	
	int delete(Permission permission);
	
	Permission getPermissionById(String permissionId);
}
