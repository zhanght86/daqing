package com.midas.controller.user;


import java.io.IOException;
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
import com.midas.model.user.User;
import com.midas.service.user.OrganizationService;
import com.midas.service.user.RoleService;
import com.midas.service.user.UserService;
import com.midas.uitls.tools.CommonsUtils;
import com.midas.vo.MessageBean;
import com.midas.vo.SearchResult;
import com.midas.vo.user.ChangePassowrdForm;
import com.midas.vo.user.OrganizationDto;
import com.midas.vo.user.RoleDto;
import com.midas.vo.user.UserDto;
import com.midas.vo.user.UserForm;



@Controller
public class UserController extends BaseController{
	
	private Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@RequestMapping(value = "/user/query.do", method = RequestMethod.GET)
    public String initPage(Model model){
		List<OrganizationDto> orgList = organizationService.getOrganization();
		model.addAttribute("orgList", orgList);
        return "/user/userList";
    }
	
	@ResponseBody
	@RequestMapping(value = "/user/list.do", method = RequestMethod.POST)
	public SearchResult<UserDto> convertList(UserForm vo){
		return userService.selectUsers(vo);
	}
	
	@RequestMapping(value = "/user/loginName/validation.do", method = RequestMethod.POST)
	@ResponseBody
	public String validationLoginName(@RequestParam(required = true, value = "loginName") String loginName) {
		boolean isValid = isValidLoginName(loginName);
		return isValid ? "true" : "false";
	}

	@RequestMapping(value = "/user/getUserByLoginName.do", method = RequestMethod.POST)
	@ResponseBody
	public List<UserDto> getUserByLoginName(UserForm userForm) {
		if(userForm!=null && userForm.getLoginName()!=null){
			List<UserDto> userList = userService.selectUsersByUserForm(userForm);
			return userList;
		}
		return null;
	}

	/**
	 * 打开修改页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/user/intoEdit.do", method = RequestMethod.GET)
	public String userEdit(Model model,@RequestParam(required = true, value = "userId") Long userId,String savetype) {
		User user = new User();
		if(userId!=0){
			user = userService.findById(userId);
		}
		List<OrganizationDto> orgList = organizationService.getOrganization();
		model.addAttribute("orgList", orgList);
		model.addAttribute("savetype", savetype);
		model.addAttribute("user", user);
		return "/user/userEdit";
	}

	@RequestMapping(value = "/user/update.do", method = RequestMethod.POST)
	@ResponseBody
	public MessageBean saveUser(UserDto userForm,String savetype) {
		int ret = 0;
		if(savetype.equals(CommonConstants.SAVETYPE_UPDATE)){//修改
			ret = userService.updateUser(userForm);
			savetype=CommonConstants.SAVETYPE_UPDATE;
		}else{//新增
			boolean isValid = isValidLoginName(userForm.getLoginName());
			if (isValid) {
				userForm.setStatus(1);
				try {
					String loginPassword = CommonsUtils.getPropertiesValue("/comm.properties","default_password");
					userForm.setLoginPassword(CommonsUtils.encryptPassword(userForm.getLoginName(), loginPassword));
				} catch (IOException e) {
					logger.error("新增用户没有配置默认密码：",e);
				}
				ret = userService.addUser(userForm);
				savetype=CommonConstants.SAVETYPE_ADD;
			}
		}
		if(1 == ret){
            return handleSuccess();
        }else {
            return handleFail("操作失败！");
        }
	}
	
	/**
	 * 用户-角色设置页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/user/userRole.do", method = RequestMethod.GET)
	public String userRole(Model model,
			@RequestParam(required = true, value = "userId") Long userId) {
		List<RoleDto> availableRoles = roleService
				.selectAvailableRolesForUser(userId);
		List<RoleDto> userRoles = roleService.selectUserRoles(userId);

		model.addAttribute("userId", userId);
		model.addAttribute("availableRoles", availableRoles);
		model.addAttribute("userRoles", userRoles);

		return "/user/userRole";
	}

	@RequestMapping(value = "/user/userRole.do", method = RequestMethod.POST)
	@ResponseBody
	public String updateUserRoles(
			Model model,
			@RequestParam(required = true, value = "userId") Long userId,
			@RequestParam(required = false, value = "roleId") Long[] selectedRoleIds) {

		roleService.updateUserRoles(userId, selectedRoleIds);

		return "true";
	}

	@RequestMapping(value = "/user/updatePassword.do", method = RequestMethod.POST)
	@ResponseBody
	public String changePersonalPassword(ChangePassowrdForm changePassowrdForm) {
		boolean success = false;
		if (StringUtils.isNotBlank(changePassowrdForm.getOldPassword())
				&& StringUtils.isNotBlank(changePassowrdForm.getNewPassword())) {
			success = userService.changePassword(
					changePassowrdForm.getOldPassword(),
					changePassowrdForm.getNewPassword());
		}

		return success ? "true" : "false";
	}

	@RequestMapping(value = "/user/resetPassword.do", method = RequestMethod.POST)
	@ResponseBody
	public String resetPassword(
			@RequestParam(required = true, value = "userId") Long userId) {
		boolean success = userService.resetPassword(userId);

		return success ? "true" : "false";
	}

	/**
	 * 验证用户名称是否重复
	 * @param loginName
	 * @return
	 */
	private boolean isValidLoginName(String loginName) {
		boolean isValid = false;
		if (StringUtils.isNotBlank(loginName)) {
			if (!userService.isExistingUserName(loginName)) {
				isValid = true;
			}
		}
		return isValid;
	}
}
