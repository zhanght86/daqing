package com.midas.controller;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageInfo;
import com.midas.service.UserService;

public class UserTest {

    private UserService userService;
    
    @Before
    public void before() {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "classpath:conf/spring.xml", "classpath:conf/spring-mybatis.xml" });
        
        userService = context.getBean(UserService.class);
        
    }
    
    @Test
    public void list() {
        PageInfo<Map<String, Object>> pageInfo = userService.list(null,null);
        pageInfo.getList();
        System.out.println(pageInfo);
    }
    
}
