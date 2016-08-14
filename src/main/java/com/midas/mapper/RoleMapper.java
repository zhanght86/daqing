package com.midas.mapper;



import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.midas.model.user.Role;
import com.midas.model.user.UserRole;
import com.midas.vo.user.RoleDto;
import com.midas.vo.user.RoleFrom;


public interface RoleMapper{


	List<RoleDto> selectRole(RoleFrom form,RowBounds rowBounds);
	
	Integer selectRoleCount(RoleFrom form);
	
	List<RoleDto> selectRoles();
	
	List<RoleDto> selectUserRoles(Map<String,Object> param);
	
	List<RoleDto> selectAvailableRolesForUser(Map<String,Object> param);
	
	List<RoleDto> findByRoleCode(Map<String,Object> param);
	
	List<RoleDto> findByRoleName(Map<String,Object> param);
	
	RoleDto findByRoleId(Map<String,Object> param);
	
	
	
	void deleteUserRoles(Map<String,Object> param);
	
	Role getRoleById(@Param("roleId")String roleId);
	void saveUserRole(UserRole userRole);
	
	int updateRole(Role updateRole);
	int saveRole(Role role);
}
