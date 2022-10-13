package com.study.spring.constant;

/**
 * 常量接口
 *
 * @date 2020-07-01
 */
public interface ConfigConstant {
    // 配置文件名称
    String CONFIG_FILE = "application.properties";

    // 数据源
    String JDBC_DRIVER = "study.spring.jdbc.driver";
    String JDBC_URL = "study.spring.jdbc.url";
    String JDBC_USERNAME = "study.spring.jdbc.username";
    String JDBC_PASSWORD = "study.spring.jdbc.password";

    // java源码地址
    String APP_BASE_PACKAGE = "study.spring.app.base-package";

    // jsp页面路径
    String APP_JSP_PATH = "study.spring.app.jsp-path";

    // 静态资源路径
    String APP_ASSET_PATH = "study.spring.app.asset-path";
}
