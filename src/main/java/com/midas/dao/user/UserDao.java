package com.midas.dao.user;



import java.util.List;
import java.util.Map;

import com.midas.model.user.User;
import com.midas.vo.SearchResult;
import com.midas.vo.user.UserDto;
import com.midas.vo.user.UserForm;



public interface UserDao {
	
	SearchResult<UserDto> selectUsers(UserForm vo);

	List<UserDto> selectUsersByChannel(Map<String,Object> param);
	
	void loadUserRole(UserDto user);
	
	String getPasswordForUser(String loginName);
	
	void changePassword(User user);

	UserDto findByName(String loginName);
	
	UserDto findById(Long userId);
	
	User getUserByUuid(String userUuid);
	
	int saveUser(User user);
	
	int updateUser(User updateUser);

	List<UserDto> selectUsersByUserForm(UserForm userForm);
}
