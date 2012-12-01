package com.aireno.base;

import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.11
 * Time: 10.05
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationContextProvider {
    static ApplicationContext context = null;
    public static void init(){
        ApplicationContextImpl context = new ApplicationContextImpl();
        context.init("META-INF/beans.xml");
        ApplicationContextProvider.context = context;
    }

    public static ApplicationContext getProvider() {
        if (context == null)
            throw new RuntimeException("ApplicationContext not initialized");
        return context;
    }
}

class ApplicationContextImpl implements ApplicationContext {
    ClassPathXmlApplicationContext context = null;
    public void init(String path){
        context = new ClassPathXmlApplicationContext(path);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        if (context == null)
            throw new RuntimeException("ApplicationContext not initialized");
        return context.getBean(name, requiredType);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        if (context == null)
            throw new RuntimeException("ApplicationContext not initialized");
        return context.getBean(requiredType);
    }
}
