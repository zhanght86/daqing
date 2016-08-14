package com.midas.service.user.impl;



import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.midas.constant.SysConstant;
import com.midas.dao.user.PermissionDao;
import com.midas.dao.user.UserDao;
import com.midas.model.user.User;
import com.midas.service.user.UserService;
import com.midas.uitls.tools.CommonsUtils;
import com.midas.vo.SearchResult;
import com.midas.vo.user.PermissionDto;
import com.midas.vo.user.RoleDto;
import com.midas.vo.user.UserDto;
import com.midas.vo.user.UserForm;



@Service("userService")
public class UserServiceImpl implements UserService {

	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private PermissionDao permissionDao;

	@Override
	public SearchResult<UserDto> selectUsers(UserForm vo) {
		return userDao.selectUsers(vo);
	}
	
	@Override
	public List<UserDto> selectUsersByChannel(Long channelsId) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("channelsId", channelsId);
		return userDao.selectUsersByChannel(param);
	}
	
	@Override
	public List<UserDto> selectUsersByUserForm(UserForm userForm) {
		List<UserDto> users = userDao.selectUsersByUserForm(userForm);
		return users;
	}

	@Override
	public UserDto getLoginUserInfo() {
		Subject currentUser = SecurityUtils.getSubject();
		UserDto verifyuser = null;
		if(currentUser!=null&&currentUser.getPrincipal()!=null){
			verifyuser =findByName(currentUser.getPrincipal().toString(),false);
		}

		if(verifyuser == null){
			try {
				throw new Exception();
			} catch (Exception e) {
				logger.error("用户session过期！");
				e.printStackTrace();
			}
		}
		return verifyuser;
	}

	@Override
	public int addUser(User user) {
		return userDao.saveUser(user);
	}

	@Override
	public int updateUser(User updateUser) {
		return userDao.updateUser(updateUser);
	}

	@Override
	public boolean resetPassword(Long userId) {
		User user = findById(userId);
		if (user != null) {
			try {
				String loginPassword = CommonsUtils.getPropertiesValue("default_password");
				user.setLoginPassword(CommonsUtils.encryptPassword(user.getLoginName(), loginPassword));
				userDao.changePassword(user);
			} catch (IOException e) {
				logger.error("用户没有配置默认密码：",e);
			}
			return true;
		}

		return false;
	}

	@Override
	public boolean isExistingUserName(String loginName) {
		User user = findByName(loginName, false);
		return user != null;
	}

	@Override
	public UserDto findByName(String loginName, boolean loadPermission) {
		UserDto user = userDao.findByName(loginName);
		if (user != null) {
			if (loadPermission) {
				userDao.loadUserRole(user);
				if (user.getRoles() != null) {
					for (RoleDto role : user.getRoles()) {
						List<PermissionDto> permissions = permissionDao
								.loadRolePermissions(role.getRoleId());
						role.setPermissions(permissions);
					}
				}
			}
		}
		return user;
	}
	
	@Override
	public UserDto findById(Long userId) {
		return userDao.findById(userId);
	}
	
	@Override
	public User getUserByUuid(String userUuid) {
		return userDao.getUserByUuid(userUuid);
	}

	@Override
	public boolean changePassword(String oldPassword, String newPassword) {
		User user = getLoginUserInfo();
		String encryptOldPwd = CommonsUtils.encryptPassword(
				user.getLoginName(), oldPassword);
		if (encryptOldPwd.equals(user.getLoginPassword())) {
			String encryptNewPwd = CommonsUtils.encryptPassword(
					user.getLoginName(), newPassword);
			user.setLoginPassword(encryptNewPwd);
			userDao.changePassword(user);
			return true;
		}
		return false;
	}

	@Override
	public String getPasswordForUser(String loginName) {
		if (loginName != null) {
			return userDao.getPasswordForUser(loginName);
		}
		return null;
	}
}
