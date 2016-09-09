package com.midas.service.common;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.midas.model.user.Permission;
import com.midas.model.user.User;
import com.midas.service.user.UserService;
import com.midas.uitls.tools.CommonsUtils;
import com.midas.vo.user.RoleDto;
import com.midas.vo.user.UserDto;



public class StorageSysShiroRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;

	/**
	 * 权限认证
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		// 获取登录时输入的用户名
		String loginName = (String) principalCollection.fromRealm(getName()).iterator().next();
		// 到数据库查是否有此对象
		UserDto user = userService.findByName(loginName, true);
		if (user != null) {
			// 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			List<RoleDto> roleList = user.getRoles();
			if (roleList != null && !roleList.isEmpty()) {
				// 用户的角色集合
				Set<String> rolesName = new HashSet<String>();
				List<String> permissions = new ArrayList<String>();
				for (RoleDto role : roleList) {
					rolesName.add(role.getRoleCode());
					if (role.getPermissions() != null)
						for (Permission permission : role.getPermissions()) {
							permissions.add(permission.getPermission());
						}
				}
				info.setRoles(rolesName);
				info.addStringPermissions(permissions);
			}
			return info;
		}
		return null;
	}

	/**
	 * 登录认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		// UsernamePasswordToken对象用来存放提交的登录信息
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
		// 查出是否有此用户
		String userName = token.getUsername();
        //		String password = userService.getPasswordForUser(userName);
		//String tmpapsswod=StringUtils.join(token.getPassword());
		//String dbpasswd=CommonsUtils.encryptPassword(userName, token.getPassword());
		User user = userService.findByName(userName, false); 
		if (user != null) {
			// 若存在，将此用户存放到登录认证info中
			SimpleAuthenticationInfo authcInfo = new SimpleAuthenticationInfo(userName,user.getLoginPassword(), getName());
			authcInfo.setCredentialsSalt(ByteSource.Util.bytes(CommonsUtils.getPasswordSalt(userName)));
			return authcInfo;
		} else {
			throw new UnknownAccountException("用户不存在 [" + userName + "]");
		}
	}

	public UserService getUserService() {
		return userService;
	}
	
	@Resource
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
