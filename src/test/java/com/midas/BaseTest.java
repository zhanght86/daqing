package com.midas;

import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BaseTest {

    protected ApplicationContext context = null;
    
    @Before
    public void before() {
         context = new ClassPathXmlApplicationContext(
                new String[] { "classpath:conf/spring.xml", "classpath:conf/spring-mybatis.xml" });
    }
    
    
    
}
