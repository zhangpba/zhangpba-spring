package com.study.spring;

import com.study.spring.aop.AopHelper;
import com.study.spring.helper.BeanHelper;
import com.study.spring.helper.ClassHelper;
import com.study.spring.helper.ControllerHelper;
import com.study.spring.ioc.IocHelper;
import com.study.spring.util.ClassUtil;

/**
 * HelperLoader 类:
 * 我们创建了ClassHelper, BeanHelper, IocHelper, ControllerHelper这四个Helper类,
 * 我们需要一个入口程序来加载他们(实际上是加载静态代码块), 当然就算没有这个入口程序, 这些类也会被加载, 我们这里只是为了让加载更加集中.
 *
 * @date 2020-07-02
 */
public final class HelperLoader {

    public static void init() {
        Class<?>[] classeList = {
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };

        for (Class<?> cls : classeList) {
            ClassUtil.loadClass(cls.getName());
        }
    }
}
