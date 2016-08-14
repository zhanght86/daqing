package com.midas.model.user;

import com.midas.model.common.BaseBean;

public class Role extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8446802835055137214L;

	private Long roleId;
	
	private String roleCode;
	
	private String roleName;
	
	private String remark;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
