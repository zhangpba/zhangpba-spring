package com.study.spring.ioc;

import com.study.spring.annotation.Autowried;
import com.study.spring.helper.BeanHelper;
import com.study.spring.helper.ClassHelper;
import com.study.spring.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * https://blog.csdn.net/litianxiang_kaola/article/details/86647022
 * 实现ioc功能，注入依赖
 *
 * @date 2020-07-01
 */
public final class IocHelper {

    /**
     * 遍历bean容器所有的bean属性，为所有带@Autowried注解的属性注入实例
     */
    static {
        // 遍历bean容器中的所有bean
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();

        if (!beanMap.isEmpty()) {
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                // bean的class类
                Class<?> beanClass = beanEntry.getKey();
                // bean实例
                Object beanInstance = beanEntry.getValue();
                // 暴力反射获取属性
                Field[] beanFields = beanClass.getDeclaredFields();
                // 遍历bean属性
                if (beanFields.length > 0) {
                    for (Field beanField : beanFields) {
                        // 判断属性是否带Autowired注解
                        if (beanField.isAnnotationPresent(Autowried.class)) {
                            // 属性类型
                            Class<?> beanFiledClass = beanField.getType();
                            // 如果beanFiledClass是接口，就获取对应所有实现类的
                            beanFiledClass = findImplementClass(beanFiledClass);
                            // 获取Class对应的实例
                            Object beanFiledInstance = beanMap.get(beanFiledClass);
                            if (beanFiledInstance != null) {
                                ReflectionUtil.setField(beanInstance, beanField, beanFiledInstance);
                            }
                        }
                    }
                }

            }
        }
    }

    /**
     * 获取接口对应的实现类
     */
    public static Class<?> findImplementClass(Class<?> interfaceClass) {
        Class<?> implementClass = interfaceClass;
        // 接口对应的实现类
        Set<Class<?>> classSetBySuper = ClassHelper.getClassSetBySuper(implementClass);
        if (!classSetBySuper.isEmpty()) {
            // 获取第一个实例
            implementClass = classSetBySuper.iterator().next();
        }
        return implementClass;
    }
}
