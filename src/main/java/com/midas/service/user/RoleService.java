package com.midas.service.user;

import java.util.List;

import com.midas.model.user.Role;
import com.midas.vo.SearchResult;
import com.midas.vo.user.RoleDto;
import com.midas.vo.user.RoleFrom;


public interface RoleService {
	SearchResult<RoleDto> selectRole(RoleFrom form);
	List<RoleDto> selectRoles();
	
	List<RoleDto> selectUserRoles(Long userId);
	
	List<RoleDto> selectAvailableRolesForUser(Long userId);
	
	int addRole(Role role);
	
	int updateRole(Role updateRole);
	
	Role getRoleById(String roleId);
	RoleDto findByRoleCode(String roleCode);
	
	RoleDto findByRoleName(String roleName);
	
	RoleDto findByRoleId(Long roleId);
	
	void updateUserRoles(Long userId, Long [] roleIds);
}
