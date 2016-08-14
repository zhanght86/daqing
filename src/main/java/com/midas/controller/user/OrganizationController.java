package com.midas.controller.user;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.midas.controller.common.BaseController;
import com.midas.service.user.OrganizationService;
import com.midas.vo.SearchResult;
import com.midas.vo.user.OrganizationDto;
import com.midas.vo.user.OrganizationForm;



@Controller
public class OrganizationController extends BaseController{
	
	@Autowired
	private OrganizationService organizationService;

	/**
	 * 进入页面
	 */
	@RequestMapping(value = "/organization/into.do", method = RequestMethod.GET)
    public String initPage(){
        return "/user/organizationList";
    }
	
	/**
	 * 信息列表
	 */
	@ResponseBody
	@RequestMapping(value = "/organization/list.do", method = RequestMethod.GET)
	public SearchResult<OrganizationDto> caseinfoList(OrganizationForm form){
		return organizationService.selectOrganization(form);
	}
	
	/**
	 * 获取当前机构下所有机构
	 */
	@ResponseBody
	@RequestMapping(value = "/organization/selectCurrOrganization.do", method = RequestMethod.GET)
	public List<OrganizationDto> selectCurrOrganization(){
		return organizationService.selectCurrOrganization();
	}
}
