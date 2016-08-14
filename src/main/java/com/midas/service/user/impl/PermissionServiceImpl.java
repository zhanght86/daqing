package com.midas.service.user.impl;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.midas.constant.CommonConstants;
import com.midas.dao.user.PermissionDao;
import com.midas.model.user.Permission;
import com.midas.model.user.Role;
import com.midas.model.user.RolePermission;
import com.midas.service.user.PermissionService;
import com.midas.service.user.RoleService;
import com.midas.service.user.UserService;
import com.midas.vo.SearchResult;
import com.midas.vo.user.PermissionDto;
import com.midas.vo.user.PermissionForm;



@Service("permissionService")
public class PermissionServiceImpl implements PermissionService{

	@Autowired
	private PermissionDao permissionDao;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	
	@Override
	public List<PermissionDto> selectAllPermissions() {
		return permissionDao.selectAllPermissions();
	}
	
	public PermissionDto findByPermissionCode(String permissionCode) {
		List<PermissionDto> permissions = permissionDao.findByPermissionCode(permissionCode);
		return permissions.size() > 0 ? permissions.get(0) : null;
	}
	
	public PermissionDto findByPermissionName(String permissionName) {
		List<PermissionDto> permissions = permissionDao.findByPermissionName(permissionName);
		return permissions.size() > 0 ? permissions.get(0) : null;
	}
	
	public List<PermissionDto> loadRolePermissions(Long roleId) {
		return permissionDao.loadRolePermissions(roleId);
	}
	
	public PermissionDto findById(Long permissionId) {
		return permissionDao.findById(permissionId);
	}
	
	public void updateRolePermissions(Long roleId, Long [] selectedPermissionIds) {
		Role role = roleService.findByRoleId(roleId);
		if (role != null) {
			permissionDao.deleteRolePermissions(roleId);
			if (selectedPermissionIds != null && selectedPermissionIds.length > 0) {
				for (Long permissionId : selectedPermissionIds) {
					RolePermission rolePermission = new RolePermission();
					rolePermission.setRoleId(roleId);
					rolePermission.setPermissionId(permissionId);
					permissionDao.insertRolePermission(rolePermission);
				}
			}
		}
	}
	
	public int updatePermission(Permission updatedPermission) {
        permissionDao.updatePermission(updatedPermission);
		return 1;
	}

	@Override
	public List<PermissionDto> loadAllMenu() {
		List<PermissionDto> list = permissionDao.loadAllMenu();
		for(PermissionDto p:list){
			if(p.getParentId()!=0){
				p.setPermissionName(CommonConstants.ALLSPACE+p.getPermissionName());
			}
		}
		return list;
	}

	@Override
	public List<PermissionDto> loadAllModule() {
		return permissionDao.loadAllModule();
	}

	@Override
	public SearchResult<PermissionDto> selectPermission(PermissionForm form) {
		return permissionDao.selectPermission(form);
	}

	@Override
	public int save(Permission permission) {
		return permissionDao.save(permission);
	}

	@Override
	public int update(Permission permission) {
		return permissionDao.update(permission);
	}

	@Override
	public int delete(Permission permission) {
		return permissionDao.delete(permission);
	}

	@Override
	public Permission getPermissionById(String permissionId) {
		return permissionDao.getPermissionById(permissionId);
	}

	@Override
	public List<PermissionDto> loadAllFunction() {
		
		return permissionDao.loadAllFunction();
	}

}
