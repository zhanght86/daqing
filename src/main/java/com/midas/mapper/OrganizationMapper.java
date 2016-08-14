package com.midas.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.midas.vo.user.OrganizationDto;
import com.midas.vo.user.OrganizationForm;



public interface OrganizationMapper {

	List<OrganizationDto> selectOrganization(OrganizationForm form,RowBounds rowBounds);
	
	Integer selectOrganizationCount(OrganizationForm form);
	
	OrganizationDto getOrganizationById(@Param("dptCode")String dptCode);
	
	List<OrganizationDto> getOrganizationByParentId(@Param("parentId")String parentId);
	
	List<OrganizationDto> getOrganization();
}
