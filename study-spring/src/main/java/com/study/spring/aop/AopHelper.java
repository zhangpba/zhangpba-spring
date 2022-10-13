package com.study.spring.aop;

import com.study.spring.annotation.Aspect;
import com.study.spring.annotation.Service;
import com.study.spring.helper.BeanHelper;
import com.study.spring.helper.ClassHelper;
import com.study.spring.proxy.AspectProxy;
import com.study.spring.proxy.Proxy;
import com.study.spring.proxy.ProxyFactory;
import com.study.spring.proxy.TransactionProxy;
import com.study.spring.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * AopHelper 助手类:初始化整个AOP框架
 * <p>
 * 框架中所有Bean的实例都是从Bean容器中获取, 然后再执行该实例的方法, 基于此,
 * 初始化AOP框架实际上就是用代理对象覆盖掉Bean容器中的目标对象,
 * 这样根据目标类的Class对象从Bean容器中获取到的就是代理对象, 从而达到了对目标对象增强的目的.
 *
 * @date 2020-07-03
 */
public final class AopHelper {
    private static final Logger logger = LoggerFactory.getLogger(AopHelper.class);

    static {

        try {
            // 切面-目标类集合的映射: EfficientAspect-UserController
            Map<Class<?>, Set<Class<?>>> aspectMap = createAspectMap();

            // 目标-切面对象列表的映射: UserController-EfficientAspect
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(aspectMap);

            // 把切面对象织入目标类中，创建代理对象
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                Class<?> targetClass = targetEntry.getKey();
                List<Proxy> proxyList = targetEntry.getValue();

                // 创建代理对象
                Object proxy = ProxyFactory.createProxy(targetClass, proxyList);

                // 覆盖bean容器中的目标类对应的实例，下次从Bean容器获取的就是代理对象了
                BeanHelper.setBean(targetClass, proxy);
            }
        } catch (Exception e) {
            logger.error("aop failure ", e);
        }

    }

    /**
     * 获取切面类-目标类集合的映射
     */
    private static Map<Class<?>, Set<Class<?>>> createAspectMap() throws Exception {
        Map<Class<?>, Set<Class<?>>> aspectMap = new HashMap<>();
        addAspectProxy(aspectMap);
        // 事务
        addTransactionProxy(aspectMap);
        return aspectMap;
    }

    /**
     * 获取普通切面类-目标类集合的映射
     */
    public static void addAspectProxy(Map<Class<?>, Set<Class<?>>> aspectMap) throws Exception {
        // 所有实现AspectProxy抽象类的切面
        Set<Class<?>> aspectClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        for (Class<?> aspectClass : aspectClassSet) {
            if (aspectClass.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = aspectClass.getAnnotation(Aspect.class);
                // 与该切面对应的目标类集合
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                /**
                 * map中的数据：
                 * EfficientAspect-UserController
                 */
                aspectMap.put(aspectClass, targetClassSet);
            }
        }
    }

    /**
     * 获取事务切面类-目标集合的映射
     */
    private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> aspectMap) {
        Set<Class<?>> serviceClassSet = ClassHelper.getClassSetByAnnotation(Service.class);
        aspectMap.put(TransactionProxy.class, serviceClassSet);
    }

    /**
     * 根据@Aspect定义的包名和类名 获取对应的目标类集合
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception {
        Set<Class<?>> targetClassSet = new HashSet<>();
        // 包名
        String pkg = aspect.pkg();
        // 类名
        String cls = aspect.cls();
        // 如果包与类名均不为空,则指定类名
        if (!pkg.equals("") && !cls.equals("")) {
            targetClassSet.add(Class.forName(pkg + "." + cls));
        } else if (!pkg.equals("")) {
            // 如果包名不为空，类名为空，则添加该包名下的所有类
            targetClassSet.addAll(ClassUtil.getClassSet(pkg));
        }
        return targetClassSet;

    }


    /**
     * 将 切面类-目标类集合的映射关系  转化为 目标类-切面对象映射关系
     */
    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> aspectMap) throws Exception {

        Map<Class<?>, List<Proxy>> targetMap = new HashMap<>();
        for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : aspectMap.entrySet()) {
            // 切面类
            Class<?> aspectClass = proxyEntry.getKey();
            // 目标类集合
            Set<Class<?>> targetClassSet = proxyEntry.getValue();
            // 创建目标类-切面对象列表的映射关系
            for (Class<?> targetClass : targetClassSet) {
                //切面对象
                Proxy aspect = (Proxy) aspectClass.newInstance();
                if (targetMap.containsKey(targetClass)) {
                    targetMap.get(targetClass).add(aspect);
                } else {
                    // 切面对象列表
                    List<Proxy> aspectList = new ArrayList<>();
                    aspectList.add(aspect);
                    targetMap.put(targetClass, aspectList);
                }

            }
        }

        return targetMap;
    }
}
