package com.midas.dao.user;



import java.util.List;

import com.midas.model.user.Role;
import com.midas.model.user.UserRole;
import com.midas.vo.SearchResult;
import com.midas.vo.user.RoleDto;
import com.midas.vo.user.RoleFrom;


public interface RoleDao {
	SearchResult<RoleDto> selectRole(RoleFrom form);
	
	List<RoleDto> selectRoles();
	
	List<RoleDto> selectUserRoles(Long userId);
	
	List<RoleDto> selectAvailableRolesForUser(Long userId);
	
	List<RoleDto> findByRoleCode(String roleCode);
	
	List<RoleDto> findByRoleName(String roleName);
	Role getRoleById(String roleId);
	RoleDto findByRoleId(Long roleId);
	
	int saveRole(Role role);
	
	void deleteUserRoles(Long userId);
	
	void insertUserRole(UserRole userRole);
	
	int updateRole(Role updateRole);
}
