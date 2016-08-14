package com.midas.service.user.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.midas.dao.user.RoleDao;
import com.midas.model.user.Role;
import com.midas.model.user.User;
import com.midas.model.user.UserRole;
import com.midas.service.user.RoleService;
import com.midas.service.user.UserService;
import com.midas.vo.SearchResult;
import com.midas.vo.user.RoleDto;
import com.midas.vo.user.RoleFrom;



@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleDao roleDao;
	@Autowired
	private UserService userService;
	
	public List<RoleDto> selectRoles() {
		List<RoleDto> roles = roleDao.selectRoles();
		return roles;
	}
	
	public List<RoleDto> selectUserRoles(Long userId) {
		if (userId != null) {
			return roleDao.selectUserRoles(userId);
		}
		return null;
	}
	
	public List<RoleDto> selectAvailableRolesForUser(Long userId) {
		if (userId != null) {
			return roleDao.selectAvailableRolesForUser(userId);
		}
		return null;
	}
	
	public int addRole(Role role) {
		return roleDao.saveRole(role);
	}
	
	public int updateRole(Role updateRole) {
        return roleDao.updateRole(updateRole);
	}
	
	public RoleDto findByRoleCode(String roleCode) {
		List<RoleDto> roles = roleDao.findByRoleCode(roleCode);
		return roles.size() > 0 ? roles.get(0) : null;
	}
	
	public RoleDto findByRoleName(String roleName) {
		List<RoleDto> roles = roleDao.findByRoleName(roleName);
		return roles.size() > 0 ? roles.get(0) : null;
	}
	
	public RoleDto findByRoleId(Long roleId) {
		return roleDao.findByRoleId(roleId);
	}
	
	public void updateUserRoles(Long userId, Long [] roleIds) {
		User user = userService.findById(userId);
		if (user != null) {
			roleDao.deleteUserRoles(userId);
			if (roleIds != null && roleIds.length > 0) {
				for (Long roleId : roleIds) {
					UserRole userRole = new UserRole();
					userRole.setUserId(userId);
					userRole.setRoleId(roleId);
					roleDao.insertUserRole(userRole);
				}
			}
		}
	}

	@Override
	public SearchResult<RoleDto> selectRole(RoleFrom form) {
		return roleDao.selectRole(form);
	}

	@Override
	public Role getRoleById(String roleId) {
		return roleDao.getRoleById(roleId);
	}
}
