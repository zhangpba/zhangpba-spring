package com.study.spring.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * ReflectionUtil 反射工具类
 *
 * @date 2020-07-01
 */
public final class ReflectionUtil {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 创建实例
     */
    public static Object newInstance(Class<?> cls) {
        Object instance;
        try {
            instance = cls.newInstance();
        } catch (Exception e) {
            logger.error("new instance failure:{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 创建实例 (根据类名)
     */
    public static Object newInstance(String className) {
        Class<?> cls = ClassUtil.loadClass(className);
        return newInstance(cls);
    }


    /**
     * 调用方法
     *
     * @param obj    对象
     * @param method 对象的方法
     * @param args   方法的参数
     * @return
     */
    public static Object invokeMethod(Object obj, Method method, Object... args) {
        Object result;
        try {
            method.setAccessible(true);
            result = method.invoke(obj, args);
        } catch (Exception e) {
            logger.error("调用方法失败 :{}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }


    /**
     * 设置成员变量的值:    给obj的(属性field)赋值为value
     *
     * @param obj   实例对象
     * @param field 属性
     * @param value 属性实例
     */
    public static void setField(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true);// 去除私有权限
            field.set(obj, value);
        } catch (Exception e) {
            logger.error("设置成员变量值失败 :{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
