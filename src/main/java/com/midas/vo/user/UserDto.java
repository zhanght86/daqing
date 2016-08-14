package com.midas.vo.user;



import java.util.List;

import com.midas.model.user.User;



public class UserDto extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4515264509396870438L;
	
	private String roleCode;
	
	private String roleName;
	
	private String orgName;	
	
	private List<RoleDto> roles;

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

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public List<RoleDto> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleDto> roles) {
		this.roles = roles;
	}
}
