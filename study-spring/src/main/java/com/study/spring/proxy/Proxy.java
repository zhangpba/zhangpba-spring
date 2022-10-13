package com.study.spring.proxy;

/**
 * Proxy接口:最上层的代理接口
 *
 * @date 2020-07-03
 */
public interface Proxy {

    /**
     * 执行链式代理
     * 所谓链式代理, 就是说, 可将多个代理通过一条链子串起来, 一个个地去执行,
     * 执行顺序取决于添加到链上的先后顺序
     *
     * @param proxyChain
     * @return
     * @throws Throwable
     */
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
