package com.study.spring.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * ProxyFactory类:代理工厂类
 * 通过这个类来梳理上面的代理逻辑.
 * 当调用 ProxyFactory.createProxy(final Class<?> targetClass, final List proxyList) 方法来创建一个代理对象后,
 * 每次执行代理方法时都会调用 intercept() 方法, 从而创建一个 ProxyChain 对象,
 * 并调用该对象的 doProxyChain() 方法. 调用doProxyChain()方法时会首先递归的执行增强, 最后再执行目标方法.
 *
 * @date 2020-07-03
 */
public class ProxyFactory {

    /**
     * 输入一个目标类和一组Proxy接口实现，输出一个代理对象
     */
    @SuppressWarnings("unchecked") // @SuppressWarnings("unchecked")是告诉编译器忽略警告信息
    public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList) {
        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            /**
             * 代理方法：每次调用目标方法时都会先创建一个ProxyChain对象，
             * 然后调用该对象的doProxyChain方法
             */
            @Override
            public Object intercept(Object targetObject, Method targetMethod, Object[] methodParam, MethodProxy methodProxy) throws Throwable {
                return new ProxyChain(targetClass, targetObject, targetMethod, methodProxy, methodParam, proxyList).doProxyChain();
            }
        });
    }
}
