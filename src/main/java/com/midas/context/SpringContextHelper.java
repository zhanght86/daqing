package com.midas.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHelper extends ApplicationObjectSupport {

    private static ApplicationContext context;

    @Override
    protected void initApplicationContext(ApplicationContext context) throws BeansException {
        super.initApplicationContext(context);
        SpringContextHelper.context = context;
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        return context.getBean(beanName, clazz);
    }

}
