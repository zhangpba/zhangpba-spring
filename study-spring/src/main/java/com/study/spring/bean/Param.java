package com.study.spring.bean;

import java.util.Map;

/**
 * Param类：封装controller方法参数
 *
 * @date 2020-07-02
 */
public class Param {

    private Map<String, Object> paramMap;

    public Param() {
    }

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public boolean isEmpty() {
        return paramMap.isEmpty();
    }
}
