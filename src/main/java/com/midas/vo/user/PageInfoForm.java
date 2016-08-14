package com.midas.vo.user;

import com.midas.constant.CommonConstants;

public class PageInfoForm {

	private Integer pageNum;
	
	private Integer pageSize;

	public Integer getPageNum() {
		return pageNum == null ? 1 : pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize == null ? CommonConstants.DEFAULT_PAGE_SIZE : pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	
}
