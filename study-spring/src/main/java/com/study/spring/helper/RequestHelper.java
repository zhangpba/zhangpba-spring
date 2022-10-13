package com.study.spring.helper;

import com.study.spring.bean.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * RequestHelper 助手类:
 * 前端控制器接收到HTTP请求后, 从HTTP中获取请求参数, 然后封装到Param对象中
 *
 * @date 2020-07-02
 */
public final class RequestHelper {
    /**
     * 获取请求参数
     */
    public static Param createParam(HttpServletRequest request) {

        Map<String, Object> paramMap = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        // 没有参数
        if (!paramNames.hasMoreElements()) {
            return null;
        }
        // GET和POST参数都能获取到
        while (paramNames.hasMoreElements()) {
            String fieldName = paramNames.nextElement();
            String fieldValue = request.getParameter(fieldName);
            paramMap.put(fieldName, fieldValue);
        }

        return new Param(paramMap);
    }
}
