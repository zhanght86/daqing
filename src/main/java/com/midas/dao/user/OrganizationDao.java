package com.midas.dao.user;


import java.util.List;

import com.midas.vo.SearchResult;
import com.midas.vo.user.OrganizationDto;
import com.midas.vo.user.OrganizationForm;



public interface OrganizationDao {
	
	SearchResult<OrganizationDto> selectOrganization(OrganizationForm form);
	
	OrganizationDto getOrganizationById(String dptCode);
	
	List<OrganizationDto> getOrganizationByParentId(String parentId);
	
	List<OrganizationDto> getOrganization();
}
