package com.midas.vo.user;


import java.util.List;

import com.midas.model.user.Permission;




public class PermissionDto extends Permission{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7897334789035968634L;

	private List<PermissionDto> menuItems;
	
	private String parentName;

	public List<PermissionDto> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<PermissionDto> menuItems) {
		this.menuItems = menuItems;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
}
