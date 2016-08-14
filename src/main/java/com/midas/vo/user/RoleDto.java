package com.midas.vo.user;



import java.util.List;

import com.midas.model.user.Role;



public class RoleDto extends Role{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8187381085967025083L;

	private List<PermissionDto> permissions;

	public List<PermissionDto> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<PermissionDto> permissions) {
		this.permissions = permissions;
	}
}
