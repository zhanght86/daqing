package com.midas.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.midas.model.user.User;
import com.midas.vo.user.RoleDto;
import com.midas.vo.user.UserDto;
import com.midas.vo.user.UserForm;



public interface UserMapper {

	List<UserDto> selectUsers(UserForm form,RowBounds rowBounds);
	
	Integer selectUsersCount(UserForm form);
	
	List<UserDto> selectUsers(Map<String,Object> param);
	
	String getPasswordForUser(Map<String,Object> param);
	
	UserDto findByName(Map<String,Object> param);
	
	UserDto findById(Map<String,Object> param);
	
	User getUserByUuid(Map<String,Object> param);
	
	List<RoleDto> getUserRoles(Map<String,Object> param);
	
	int saveUser(User user);
	
	void updatePassword(User user);

	int updateUser(User updateUser);

	List<UserDto> selectUsersByUserForm(UserForm form);
}
