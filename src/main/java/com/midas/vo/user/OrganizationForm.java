package com.midas.vo.user;

import com.midas.vo.SearchForm;

public class OrganizationForm extends SearchForm{

	private String dptCode;
	
	private String dptName;

	public String getDptCode() {
		return dptCode;
	}

	public void setDptCode(String dptCode) {
		this.dptCode = dptCode;
	}

	public String getDptName() {
		return dptName;
	}

	public void setDptName(String dptName) {
		this.dptName = dptName;
	}
}
