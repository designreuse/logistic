package com.frame.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring上下文工具类,用于动态初始化bean
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> cls){
        return applicationContext.getBean(cls);
    }

    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }
}