package com.zsk.template;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
* @Description: spring 容器, 必须放在跟@SpringBootApplication修饰的类同一个包下
* @Param:
* @return:
*/
@Component
@Lazy(false)
public class SpringContext implements ApplicationContextAware
{
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }

    public static Object getBean(String name)
    {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz)
    {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz)
    {
        return getApplicationContext().getBean(name, clazz);
    }
}