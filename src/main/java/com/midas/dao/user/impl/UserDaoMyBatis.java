package com.midas.dao.user.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.midas.dao.user.UserDao;
import com.midas.mapper.UserMapper;
import com.midas.model.user.User;
import com.midas.vo.SearchResult;
import com.midas.vo.user.RoleDto;
import com.midas.vo.user.UserDto;
import com.midas.vo.user.UserForm;



@Repository("userDao")
public class UserDaoMyBatis implements UserDao {
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public SearchResult<UserDto> selectUsers(UserForm vo) {
		SearchResult<UserDto> result = new SearchResult<UserDto>();
        Integer count = userMapper.selectUsersCount(vo);
        if(count>0){
            List<UserDto> list = userMapper.selectUsers(vo,vo.getRowBounds());
            result.setRows(list);
            result.setTotal(count);
        }
        return result;
	}
	
	@Override
	public List<UserDto> selectUsersByChannel(Map<String,Object> param) {
		return userMapper.selectUsers(param);
	}

	@Override
	public List<UserDto> selectUsersByUserForm(UserForm userForm) {
		return userMapper.selectUsersByUserForm(userForm);
	}

	@Override
	public void loadUserRole(UserDto user) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", user.getUserId());

		List<RoleDto> roles = userMapper.getUserRoles(params);
		user.setRoles(roles);
	}

	@Override
	public String getPasswordForUser(String loginName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginName", loginName);
		return userMapper.getPasswordForUser(params);
	}

	@Override
	public void changePassword(User user) {
		userMapper.updatePassword(user);
	}

	@Override
	public UserDto findByName(String loginName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginName", loginName);
		return userMapper.findByName(params);
	}

	@Override
	public UserDto findById(Long userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		return userMapper.findById(params);	
	}
	
	@Override
	public User getUserByUuid(String userUuid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userUuid", userUuid);
		return userMapper.getUserByUuid(params);	
	}

	@Override
	public int saveUser(User user) {
		return userMapper.saveUser(user);
	}

	@Override
	public int updateUser(User updateUser) {
		return userMapper.updateUser(updateUser);
	}
}
