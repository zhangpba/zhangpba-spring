package com.study.spring.helper;

import com.study.spring.annotation.Controller;
import com.study.spring.annotation.Service;
import com.study.spring.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * ClassHelper 助手类:
 * 在自身被加载的时候通过 ConfigHelper 助手类获取应用的基础包名,
 * 然后通过 ClassUtil 工具类来获取基础包名下所有类, 存储到 CLASS_SET 集合中. 除此之外,
 * 其他的方法在后面的代码中会经常被使用到.
 *
 * @date 2020-07-01
 */
public final class ClassHelper {

    private static final Logger logger = LoggerFactory.getLogger(ClassHelper.class);

    /**
     * 定义集合类（存放基础包下的所有类）
     */
    private static final Set<Class<?>> CLASS_SET;

    static {
        // 获取基础包名
        String basePackage = ConfigHelper.getAppBasePackage();
        // 获取基础包下的所有类
        CLASS_SET = ClassUtil.getClassSet(basePackage);

        for (Class<?> cls : CLASS_SET) {
            logger.info("spring 加载到基础包下的类：" + cls.getName());
        }
    }


    /**
     * 获取基础包名下的所有类
     */
    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    /**
     * 获取基础包名下的所有 Service 类
     */
    public static Set<Class<?>> getServiceClassSet() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(Service.class)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取基础包名下的所有 Controller 类
     */
    public static Set<Class<?>> getControllerClassSet() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(Controller.class)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取基础包名下的所有 Bean 类（包括：Controller、Service）
     */
    public static Set<Class<?>> getBeanClassSet() {
        Set<Class<?>> beanClassSet = new HashSet<Class<?>>();
        beanClassSet.addAll(getServiceClassSet());
        beanClassSet.addAll(getControllerClassSet());
        return beanClassSet;
    }

    /**
     * 获取基础包名下某父类或者接口的所有实现类
     */
    public static Set<Class<?>> getClassSetBySuper(Class<?> supClass) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET) {
            // isAssignableFrom()指 supClass和cls是否相同 或者supClass是否是cls的父类接口
            if (supClass.isAssignableFrom(cls) && !supClass.equals(cls)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取基础包名下带某注解的所有类
     */
    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(annotationClass)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }
}
