package com.midas.controller.user;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.midas.constant.CommonConstants;
import com.midas.controller.common.BaseController;
import com.midas.model.user.Permission;
import com.midas.service.user.PermissionService;
import com.midas.vo.MessageBean;
import com.midas.vo.SearchResult;
import com.midas.vo.user.PermissionDto;
import com.midas.vo.user.PermissionForm;



@Controller
public class PermissionController extends BaseController{

	@Autowired
	private PermissionService permissionService;

	/**
	 * 进入页面
	 */
	@RequestMapping(value="/permission/query.do", method=RequestMethod.GET)
	public String permissionList(Model model) throws Exception{
		List<PermissionDto> permission=permissionService.selectAllPermissions();
		model.addAttribute("permission", permission);
		return "/user/permissionList";
	}
	
	/**
	 * 信息列表
	 */
	@ResponseBody
	@RequestMapping(value="/permission/list.do",method=RequestMethod.POST)
	public SearchResult<PermissionDto> permissionList(PermissionForm form){
		return permissionService.selectPermission(form);
	}
	/**
	 * 打开修改页面
	 * @return
	 */
	@RequestMapping(value="/permission/intoEdit.do", method=RequestMethod.GET)
	public String permissionEdit(Model model, @RequestParam(required=true, value="permissionId") String permissionId,String savetype) {
		Permission permission=new Permission();
		if(!StringUtils.isEmpty(permissionId)){
			permission=permissionService.getPermissionById(permissionId);
		}
		List<PermissionDto> per=permissionService.selectAllPermissions();
		model.addAttribute("savetype", savetype);
		model.addAttribute("permission",permission);
		model.addAttribute("per", per);
		return "/user/permissionEdit";
	}
	
	/**
	 * 保存
	 * @param permissionForm
	 * @return
	 */
	@RequestMapping(value="/permission/update.do", method=RequestMethod.POST)
	@ResponseBody
	public MessageBean addPermission(Permission permission,String savetype) {
		int ret=0;
		if(savetype.equals(CommonConstants.SAVETYPE_UPDATE)){
			ret=permissionService.update(permission);
			savetype=CommonConstants.SAVETYPE_UPDATE;
		}else{
			ret=permissionService.save(permission);
			savetype=CommonConstants.SAVETYPE_ADD;
		}
		if(ret>=1){
			return handleSuccess();
		}else{
			 return handleFail("操作失败！");
		}
	}
	
	/**
	 * 删除
	 * @param roleCode
	 * @return
	 */
	@RequestMapping(value="/permission/delete.do", method=RequestMethod.GET)
	@ResponseBody
	public MessageBean delete(String permissionId){
		Permission permission=new Permission();
		permission.setPermissionId(Long.parseLong(permissionId));
		int count=permissionService.delete(permission);
		if(count>=1){
			return handleSuccess();
		}else{
			return handleFail("操作失败！");
		}
	}
	/**
	 * 获取所有
	 * @param roleCode
	 * @return
	 */
	@RequestMapping(value="/permission/getPermission.do", method=RequestMethod.GET)
	@ResponseBody
	public List<PermissionDto> getPermission(){
		return permissionService.selectAllPermissions();
	}

}
