package com.midas.model.common;


import java.io.Serializable;
import java.util.Date;

public class BaseBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3393804806991502493L;

	private Long createdUser;
	
	private Date createdTime;
	
	private Long updatedUser;
	
	private Date updatedTime;
	
	private Boolean deleted;

	public Long getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(Long createdUser) {
		this.createdUser = createdUser;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Long getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(Long updatedUser) {
		this.updatedUser = updatedUser;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
}
