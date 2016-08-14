package com.midas.controller.user;



import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.midas.constant.CommonConstants;
import com.midas.controller.common.BaseController;
import com.midas.model.user.Role;
import com.midas.service.user.PermissionService;
import com.midas.service.user.RoleService;
import com.midas.vo.MessageBean;
import com.midas.vo.SearchResult;
import com.midas.vo.user.PermissionDto;
import com.midas.vo.user.RoleDto;
import com.midas.vo.user.RoleFrom;


@Controller
public class RoleController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PermissionService permissionService;

	/**
	 * 角色管理列表
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(value = "/role/query.do", method = RequestMethod.GET)
    public String initPage(Model model){
		return "/user/roleLists";
    }
	@ResponseBody
	@RequestMapping(value = "/role/list.do", method = RequestMethod.POST)
	public SearchResult<RoleDto> roleList(RoleFrom form){
		return roleService.selectRole(form);
	}
	
	@RequestMapping(value="/role/add.do",method=RequestMethod.POST)
	@ResponseBody
	public String addRole(RoleDto roleForm) {
		String roleCode = roleForm.getRoleCode();
		String roleName = roleForm.getRoleName();
		
		boolean isValidForm = true;
		if (!isValidRoleCode(roleCode) || !isValidRoleName(null, roleName)) {
			isValidForm = false;
		}
		
		boolean saved = false;
		if (isValidForm) {
			Role role = new Role();
			role.setRoleCode(roleForm.getRoleCode());
			role.setRoleName(roleForm.getRoleName());
			role.setRemark(roleForm.getRemark());
			role.setDeleted(roleForm.getDeleted());
			
			int ret = roleService.addRole(role);
			saved = ret > 0 ? true : false;
		}
		return saved ? "true" : "false";
	}
	
	@RequestMapping(value="/role/roleCode/validation.do", method=RequestMethod.POST)
	@ResponseBody
	public String validationRoleCode(@RequestParam(required=true, value="roleCode") String roleCode) {
		boolean isValid = isValidRoleCode(roleCode);
		return isValid ? "true" : "false";
	}
	
	@RequestMapping(value="/role/roleName/validation.do", method=RequestMethod.POST)
	@ResponseBody
	public String validationRoleName(@RequestParam(required=false, value="roleId") Long roleId, @RequestParam(required=true, value="roleName") String roleName) {
		boolean isValid = isValidRoleName(roleId, roleName);
		return isValid ? "true" : "false";
	}
	
	private boolean isValidRoleCode(String roleCode) {
		boolean isValid = false;
		if (StringUtils.isNotBlank(roleCode)) {
			if (roleService.findByRoleCode(roleCode) == null) {
				isValid = true;
			}
		}
		return isValid;
	}
	
	private boolean isValidRoleName(Long roleId, String roleName) {
		boolean isValid = false;
		Role role = null;
		if (roleId != null) {
			role = roleService.findByRoleId(roleId);
		}
		if (role != null && role.getRoleName().equals(roleName)) {
			isValid = true;
		} else {
			if (StringUtils.isNotBlank(roleName)) {
				if (roleService.findByRoleName(roleName) == null) {
					isValid = true;
				}
			}
		}
		return isValid;
	}
	
	/**
	 * 进入修改页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/role/intoEdit.do", method = RequestMethod.GET)
	public String roleEdit(Model model,@RequestParam(required = true, value = "roleId") String roleId,String savetype) {
		Role role = new Role();
		if(!StringUtils.isEmpty(roleId)){
			role = roleService.getRoleById(roleId);
		}
		model.addAttribute("role", role);
		model.addAttribute("savetype", savetype);
		return "/user/roleEdit";
	}
	
	/**
	 * 保存案件信息
	 * @param userForm
	 * @return
	 */
	@RequestMapping(value = "/role/update.do", method = RequestMethod.POST)
	@ResponseBody
	public MessageBean roleinfo(Role role,String savetype) {
		int ret = 0;
		if(savetype.equals(CommonConstants.SAVETYPE_UPDATE)){//修改
			ret= roleService.updateRole(role);
			savetype = CommonConstants.SAVETYPE_UPDATE;
		}else{//新增
			ret=roleService.addRole(role);
			savetype = CommonConstants.SAVETYPE_ADD;
			}
		if(ret>=1){
			return handleSuccess();
        }else {
            return handleFail("操作失败！");
        }
	}
	
	
	@RequestMapping(value = "/role/updateYe.do", method = RequestMethod.GET)
	public String roleEdit(Model model,@RequestParam(required = true, value = "roleId") String roleId,String savetype,RoleDto roleForm) {
		boolean saved = false;
		Role updateRole = new Role();
		try {
			updateRole.setRoleId(roleForm.getRoleId());
		} catch (NumberFormatException e) {
			logger.error("Role id is invalid: " + e.getMessage(), e);
			return "false";
		}
		if (!isValidRoleName(updateRole.getRoleId(), roleForm.getRoleName())) {
			return "false";
		}
		updateRole.setRoleName(roleForm.getRoleName());
		updateRole.setRemark(roleForm.getRemark());
		updateRole.setDeleted(roleForm.getDeleted());
		int ret = roleService.updateRole(updateRole);
		saved = ret > 0 ? true : false;
		return saved ? "true" : "false";
	}
	
	
	@RequestMapping(value="/role/rolePermission.do",method=RequestMethod.GET)
	public String rolePermission(Model model, @RequestParam(required=true, value="roleId") Long roleId) {
		Role role = roleService.findByRoleId(roleId);
		if (role != null) {
			List<PermissionDto> availablePermissions = permissionService.selectAllPermissions();
			List<PermissionDto> rolePermissions = permissionService.loadRolePermissions(roleId);
			
			model.addAttribute("role", role);
			model.addAttribute("availablePermissions", availablePermissions);
			model.addAttribute("rolePermissions", rolePermissions);
		}
		
		return "/user/rolePermission";
	}
	
	@RequestMapping(value="/role/rolePermission.do",method=RequestMethod.POST)
	@ResponseBody
	public String updateRolePermissions(Model model, @RequestParam(required=true, value="roleId") Long roleId, 
			@RequestParam(required=false, value="permissionId") Long [] selectedPermissionIds) {
		permissionService.updateRolePermissions(roleId, selectedPermissionIds);
		return "true";
	}
}
