package com.study.spring.helper;

import com.study.spring.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Bean容器助手类
 * 加载时就会创建一个Bean容器 BEAN_MAP,
 * 然后获取到应用中所有bean的Class对象, 再通过反射创建bean实例, 储存到 BEAN_MAP 中.
 *
 * @date 2020-07-01
 */
public final class BeanHelper {

    /**
     * spring容器，拥有所有的bean实例
     */
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>();

    static {
        // 获取所有应用中的bean
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        // 将bean实例化，放入bean容器
        for (Class<?> beanClass : beanClassSet) {
            Object object = ReflectionUtil.newInstance(beanClass);
            BEAN_MAP.put(beanClass, object);
        }
    }


    /**
     * 获取bean容器
     */
    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    /**
     * 获取bean实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<?> cls) {
        if (!BEAN_MAP.containsKey(cls)) {
            throw new RuntimeException("can not get bean by class:" + cls);
        }
        return (T) BEAN_MAP.get(cls);
    }

    /**
     * 设置bean实例
     */
    public static void setBean(Class<?> cls, Object obj) {
        BEAN_MAP.put(cls, obj);
    }

}
