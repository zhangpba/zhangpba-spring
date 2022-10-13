package com.study.spring.bean;

/**
 * Data类：封装Controller方法的JSON返回结果
 *
 * @date 2020-07-02
 */
public class Data {

    /**
     * 模型数据
     */
    private Object model;

    public Data(Object model) {
        this.model = model;
    }

    public Object getModel() {
        return model;
    }
}
