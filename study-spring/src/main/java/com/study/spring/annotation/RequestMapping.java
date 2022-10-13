package com.study.spring.annotation;

import com.study.spring.constant.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 处理器方法注解
 *
 * @date 2020-07-01
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

    /**
     * 请求路径
     *
     * @return
     */
    String value() default "";

    /**
     * 请求方法
     *
     * @return
     */
    RequestMethod method() default RequestMethod.GET;

}
