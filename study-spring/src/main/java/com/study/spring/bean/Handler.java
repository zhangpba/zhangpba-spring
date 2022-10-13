package com.study.spring.bean;

import java.lang.reflect.Method;

/**
 * Handler类为一个处理器, 封装了Controller的Class对象和Method方法.
 *
 * @date 2020-07-02
 */
public class Handler {
    /**
     * controller类
     */
    private Class<?> controllerClass;

    /**
     * controller方法
     */
    private Method controllerMethod;

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getControllerMethod() {
        return controllerMethod;
    }

    public Handler(Class<?> controllerClass, Method controllerMethod) {
        this.controllerClass = controllerClass;
        this.controllerMethod = controllerMethod;
    }
}
