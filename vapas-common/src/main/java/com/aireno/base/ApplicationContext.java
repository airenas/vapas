package com.aireno.base;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 12.11.11
 * Time: 10.02
 * To change this template use File | Settings | File Templates.
 */
public interface ApplicationContext {
    public <T> T getBean(java.lang.String name, java.lang.Class<T> requiredType) throws org.springframework.beans.BeansException;
    public <T> T getBean(java.lang.Class<T> requiredType) throws org.springframework.beans.BeansException;
}
