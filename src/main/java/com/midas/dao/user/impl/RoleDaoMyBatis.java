package com.midas.dao.user.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.midas.dao.user.RoleDao;
import com.midas.mapper.RoleMapper;
import com.midas.model.user.Role;
import com.midas.model.user.UserRole;
import com.midas.vo.SearchResult;
import com.midas.vo.user.RoleDto;
import com.midas.vo.user.RoleFrom;



@Repository
public class RoleDaoMyBatis implements RoleDao {
	
	@Autowired
	private RoleMapper roleMapper;

	@Override
	public List<RoleDto> selectRoles() {
		return roleMapper.selectRoles();
	}

	@Override
	public List<RoleDto> selectUserRoles(Long userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		return roleMapper.selectUserRoles(params);
	}

	@Override
	public List<RoleDto> selectAvailableRolesForUser(Long userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		return roleMapper.selectAvailableRolesForUser(params);
	}

	@Override
	public List<RoleDto> findByRoleCode(String roleCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleCode", roleCode);
		return roleMapper.findByRoleCode(params);
	}

	@Override
	public List<RoleDto> findByRoleName(String roleName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleName", roleName);
		return roleMapper.findByRoleName(params);
	}

	@Override
	public RoleDto findByRoleId(Long roleId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleId", roleId);
		return roleMapper.findByRoleId(params);
	}

	@Override
	public int saveRole(Role role) {
		return roleMapper.saveRole(role);
	}

	@Override
	public void deleteUserRoles(Long userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		roleMapper.deleteUserRoles(params);
		
	}

	@Override
	public void insertUserRole(UserRole userRole) {
		roleMapper.saveUserRole(userRole);
	}

	@Override
	public int updateRole(Role updateRole) {
		return roleMapper.updateRole(updateRole);
	}

	@Override
	public SearchResult<RoleDto> selectRole(RoleFrom form) {
		SearchResult<RoleDto> result = new SearchResult<RoleDto>();
        Integer count = roleMapper.selectRoleCount(form);
        if(count>0){
            List<RoleDto> list = roleMapper.selectRole(form,form.getRowBounds());
            result.setRows(list);
            result.setTotal(count);
        }
        return result;
	}

	@Override
	public Role getRoleById(String roleId) {
		return roleMapper.getRoleById(roleId);
	}
}
