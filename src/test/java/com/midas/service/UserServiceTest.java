package com.midas.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.midas.BaseTest;

public class UserServiceTest extends BaseTest {

    private UserService userService;

    @Before
    public void ust() {
        this.userService = context.getBean(UserService.class);
    }

    /**
     * 
     */
    // @Test
    public void getCountByCondition() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("user_name", "ab");

        int i = userService.getCountByCondition(map);
        System.out.println(">>>" + i);
    }

    // @Test
    public void add() {
        // #{},#{},#{},#{},#{},#{},#{},#{},#{},#{},#{}
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_name", "abab1234");
        map.put("password", "ab");
        map.put("nick_name", "ab");
        map.put("status", "1");
        map.put("remark", null);

        int i = userService.addUser(map, 0);
        System.out.println(i);
    }

    @Test
    public void login() {
        boolean bool = userService.login("abab1234", "ab才");
        System.out.println("登录结果:"+bool);
    }

}
