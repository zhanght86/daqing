package com.midas.service.user.impl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.midas.dao.user.OrganizationDao;
import com.midas.service.CommonService;
import com.midas.service.user.OrganizationService;
import com.midas.vo.SearchResult;
import com.midas.vo.user.OrganizationDto;
import com.midas.vo.user.OrganizationForm;



@Service("organizationService")
public class OrganizationServiceImpl implements OrganizationService {

	@Autowired
	private OrganizationDao organizationDao;
	@Autowired
	private CommonService commonService;

	@Override
	public SearchResult<OrganizationDto> selectOrganization(OrganizationForm form) {
		return organizationDao.selectOrganization(form);
	}

	@Override
	public List<OrganizationDto> selectCurrOrganization() {
		//取当前机构
		String currOrgNo =null ;//commonService.getCurrOrgNo();
		OrganizationDto org = organizationDao.getOrganizationById(currOrgNo);
		//取当前机构下子机构
		List<OrganizationDto> childrenList = organizationDao.getOrganizationByParentId(org.getDptCode());
		if(!childrenList.isEmpty()){
			org.setChildren(childrenList);
			//org.setState("closed");
		}
		for(OrganizationDto o:childrenList){
			List<OrganizationDto> threeLevelList = organizationDao.getOrganizationByParentId(o.getDptCode());
			if(!threeLevelList.isEmpty()){
				o.setChildren(threeLevelList);
				//o.setState("closed");
			}
		}
		List<OrganizationDto> orgList = new ArrayList<OrganizationDto>();
		orgList.add(org);
		return orgList;
	}

	@Override
	public List<OrganizationDto> getOrganization() {
		return organizationDao.getOrganization();
	}
}
