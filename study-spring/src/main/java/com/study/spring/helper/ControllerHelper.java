package com.study.spring.helper;

import com.study.spring.annotation.RequestMapping;
import com.study.spring.bean.Handler;
import com.study.spring.bean.Request;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 映射器处理器
 * <p>
 * 1 首先通过 ClassHelper 工具类获取到应用中所有Controller的Class对象,
 * 2 然后遍历Controller及其所有方法, 将所有带 @RequestMapping 注解的方法封装为处理器,
 * 3 将 @RequestMapping 注解里的请求路径和请求方法封装成请求对象, 然后存入 REQUEST_MAP 中.
 * REQUEST_MAP 就相当于Spring MVC里的映射处理器, 接收到请求后返回对应的处理器.
 *
 * @date 2020-07-02
 */
public final class ControllerHelper {


    /**
     * "请求-处理器"的映射
     */
    private static final Map<Request, Handler> REQUEST_MAP = new HashMap<>();

    static {
        // 遍历所有的Controller类
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (!controllerClassSet.isEmpty()) {
            for (Class<?> controllerClass : controllerClassSet) {
                // 暴力反射获取所有的方法
                Method[] methods = controllerClass.getDeclaredMethods();
                if (methods.length > 0) {
                    for (Method method : methods) {
                        // 判断是否带RequestMapping注解
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                            // 请求路径
                            String requestPath = requestMapping.value();
                            // 请求方法
                            String requestMethod = requestMapping.method().name();

                            // 封装请求和处理器
                            Request request = new Request(requestMethod, requestPath);
                            Handler handler = new Handler(controllerClass, method);
                            REQUEST_MAP.put(request, handler);
                        }
                    }
                }
            }
        }
    }


    /**
     * 获取Handler
     *
     * @param requestMethod 请求方案
     * @param requestPath   请求路径
     * @return
     */
    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return REQUEST_MAP.get(request);
    }
}
