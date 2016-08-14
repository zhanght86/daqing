package com.midas.vo.user;

import com.midas.vo.SearchForm;

public class UserForm extends SearchForm{

	private String dptCode;
	
	private String loginName;

	public String getDptCode() {
		return dptCode;
	}

	public void setDptCode(String dptCode) {
		this.dptCode = dptCode;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
}
