package com.midas.model.user;

import com.midas.model.common.BaseBean;

public class RolePermission extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1135319466179989899L;

	private Long roleId;
	
	private Long permissionId;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}
}
