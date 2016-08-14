package com.midas.service.user;


import java.util.List;

import com.midas.model.user.User;
import com.midas.vo.SearchResult;
import com.midas.vo.user.UserDto;
import com.midas.vo.user.UserForm;



public interface UserService{

	SearchResult<UserDto> selectUsers(UserForm vo);
	
	List<UserDto> selectUsersByChannel(Long channelsId);

	UserDto getLoginUserInfo();
	
	int addUser(User user);

    int updateUser(User updateUser);
    
	boolean resetPassword(Long userId);
	
	boolean isExistingUserName(String loginName);
	
	UserDto findByName(String loginName, boolean loadPermission);
	
	UserDto findById(Long userId);
	
	User getUserByUuid(String userUuid);
	
	boolean changePassword(String oldPassword, String newPassword);

	String getPasswordForUser(String loginName);

	List<UserDto> selectUsersByUserForm(UserForm userForm);
}
