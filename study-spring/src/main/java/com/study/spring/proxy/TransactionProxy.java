package com.study.spring.proxy;

import com.study.spring.annotation.Transactional;
import com.study.spring.helper.DatabaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * TransactionProxy 事务切面类:
 * 同样实现了Proxy接口, 其 doProxy() 方法就是先判断代理方法上有没有 @Transactional 注解,
 * 如果有就加上事务管理, 没有就直接执行.
 *
 * @date 2020-07-03
 */
public class TransactionProxy implements Proxy {

    private static final Logger logger = LoggerFactory.getLogger(TransactionProxy.class);

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {

        Object result;
        Method method = proxyChain.getTargetMethod();
        // 加@Transactional注解的方法要做事务处理
        if (method.isAnnotationPresent(Transactional.class)) {
            try {
                DatabaseHelper.beginTransation();
                logger.debug("开启事务");
                result = proxyChain.doProxyChain();
                DatabaseHelper.commitTransation();
                logger.info("提交事务");
            } catch (Exception e) {
                DatabaseHelper.rollbackTransation();
                logger.info("事务回滚");
                throw e;
            }
        } else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}
