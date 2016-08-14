package com.midas.model.user;

import com.midas.model.common.BaseBean;

public class UserRole extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5294292558172485401L;

	private Long userId;
	
	private Long roleId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}
