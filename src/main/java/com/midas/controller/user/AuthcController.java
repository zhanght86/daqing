package com.midas.controller.user;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.midas.model.user.User;
import com.midas.service.user.PermissionService;
import com.midas.service.user.UserService;
import com.midas.vo.user.PermissionDto;
import com.midas.vo.user.UserDto;

@Controller
public class AuthcController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthcController.class);

	@Autowired
	private PermissionService permissionService;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/login.do",method=RequestMethod.GET)
	public String loginForm(Model model, RedirectAttributes redirectAttributes) {
		model.addAttribute("userForm", new UserDto());
		String errorCode = (String) redirectAttributes.getFlashAttributes().get("errorCode");
		if (errorCode != null) {
			model.addAttribute("errorCode", errorCode);
		}
		return "login";
	}
	
	@RequestMapping(value="/login.do",method=RequestMethod.POST)
	public String login(User userForm, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		try {
			if (StringUtils.isBlank(userForm.getLoginName()) || StringUtils.isBlank(userForm.getLoginPassword())) {
				return "login";
			}
			Subject currentUser = SecurityUtils.getSubject();
			if (!currentUser.isAuthenticated()) {
				UsernamePasswordToken token = new UsernamePasswordToken(userForm.getLoginName(), userForm.getLoginPassword());
				token.setRememberMe(false);
				currentUser.login(token);
				List<PermissionDto> allMenugroupList = permissionService.loadAllMenu();
				List<PermissionDto> allFunctionList = permissionService.loadAllFunction();
				List<PermissionDto> userMenugroupList = getAuthUserMenuGroups(allMenugroupList);
				SecurityUtils.getSubject().getSession().setAttribute("storageSysMenus", userMenugroupList);
				SecurityUtils.getSubject().getSession().setAttribute("storageSysFunction", allFunctionList);
				SecurityUtils.getSubject().getSession().setTimeout(3600000);//session失效为1小时
			}
			return "main";
		} catch (AuthenticationException e) {
			redirectAttributes.addFlashAttribute("errorCode", "001");
			logger.error("Can not login!", e);
			return "main";
		}
	}
	
	private List<PermissionDto> getAuthUserMenuGroups(List<PermissionDto> allMenugroupList) {
		List<PermissionDto> userMenuGroups = new ArrayList<PermissionDto>();
		List<PermissionDto> menuItems = null;
		Long permissionId = allMenugroupList.get(0).getPermissionId();
		for (PermissionDto permissionGroup : allMenugroupList) {
			if(permissionGroup.getParentId()==permissionId){
				menuItems.add(permissionGroup);
			}else{
				userMenuGroups.add(permissionGroup);
				menuItems = new ArrayList<PermissionDto>();
				userMenuGroups.get(userMenuGroups.size()-1).setMenuItems(menuItems);
				permissionId = permissionGroup.getPermissionId();
			}
		}
		return userMenuGroups;
	}
	
	@RequestMapping(value="/logout.do", method=RequestMethod.GET)
	public String logout() {
		SecurityUtils.getSubject().logout();
		return "redirect:/login.do";
	}
	
	@RequestMapping("/403.do")
	public String unauthorizedRole() {
		return "403";
	}
}
