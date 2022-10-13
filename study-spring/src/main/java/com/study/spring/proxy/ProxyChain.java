package com.study.spring.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * ProxyChain类:
 * proxyList 存储的是代理列表(也就是增强列表),
 * 当执行doProxyChain() 方法时会按照顺序执行增强,最后再执行目标方法.
 *
 * @date 2020-07-03
 */
public class ProxyChain {

    private final Class<?> targetClass; // 目标类
    private final Object targetObject;  // 目标对象
    private final Method targetMethod;  // 目标方法
    private final MethodProxy methodProxy;  // 方法代理
    private final Object[] methodParams;    // 方法参数

    private List<Proxy> proxyList = new ArrayList<>();// 代理列表
    private int proxyIndex = 0; // 代理索引

    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyList = proxyList;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    /**
     * 递归执行
     *
     * @return
     * @throws Throwable
     */
    public Object doProxyChain() throws Throwable {
        Object methodResult;
        if (proxyIndex < proxyList.size()) {
            // 执行增强方法
            methodResult = proxyList.get(proxyIndex++).doProxy(this);
        } else {
            methodResult = methodProxy.invokeSuper(targetObject, methodParams);
        }
        return methodResult;
    }
}
