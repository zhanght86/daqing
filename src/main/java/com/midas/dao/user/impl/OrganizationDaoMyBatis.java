package com.midas.dao.user.impl;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.midas.dao.user.OrganizationDao;
import com.midas.mapper.OrganizationMapper;
import com.midas.vo.SearchResult;
import com.midas.vo.user.OrganizationDto;
import com.midas.vo.user.OrganizationForm;



@Repository("organizationDao")
public class OrganizationDaoMyBatis implements OrganizationDao {
	
	@Autowired
	private OrganizationMapper organizationMapper;

	@Override
	public SearchResult<OrganizationDto> selectOrganization(OrganizationForm form) {
		SearchResult<OrganizationDto> result = new SearchResult<OrganizationDto>();
        Integer count = organizationMapper.selectOrganizationCount(form);
        if(count>0){
            List<OrganizationDto> list = organizationMapper.selectOrganization(form,form.getRowBounds());
            result.setRows(list);
            result.setTotal(count);
        }
        return result;
	}

	@Override
	public OrganizationDto getOrganizationById(String dptCode) {
		return organizationMapper.getOrganizationById(dptCode);
	}

	@Override
	public List<OrganizationDto> getOrganizationByParentId(String parentId) {
		return organizationMapper.getOrganizationByParentId(parentId);
	}

	@Override
	public List<OrganizationDto> getOrganization() {
		return organizationMapper.getOrganization();
	}
}
