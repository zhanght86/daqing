package com.midas.service.user;



import java.util.List;

import com.midas.vo.SearchResult;
import com.midas.vo.user.OrganizationDto;
import com.midas.vo.user.OrganizationForm;



public interface OrganizationService{

	/**
	 * 获取所有机构
	 * @param form
	 * @return
	 */
	SearchResult<OrganizationDto> selectOrganization(OrganizationForm form);
	
	/**
	 * 获取当前机构下所有机构
	 */
	List<OrganizationDto> selectCurrOrganization();
	
	List<OrganizationDto> getOrganization();
}
