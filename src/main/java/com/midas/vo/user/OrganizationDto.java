package com.midas.vo.user;

import java.util.List;

import com.midas.model.user.Organization;


public class OrganizationDto extends Organization{

	private String parentName;
	
	private List<OrganizationDto> children;
	
	private String state;//树节点是否折叠，closed折叠，为空全部展开

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public List<OrganizationDto> getChildren() {
		return children;
	}

	public void setChildren(List<OrganizationDto> children) {
		this.children = children;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
