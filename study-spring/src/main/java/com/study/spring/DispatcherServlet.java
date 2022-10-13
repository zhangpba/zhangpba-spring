package com.study.spring;

import com.alibaba.fastjson.JSON;
import com.study.spring.bean.Data;
import com.study.spring.bean.Handler;
import com.study.spring.bean.Param;
import com.study.spring.bean.View;
import com.study.spring.helper.BeanHelper;
import com.study.spring.helper.ConfigHelper;
import com.study.spring.helper.ControllerHelper;
import com.study.spring.helper.RequestHelper;
import com.study.spring.util.ReflectionUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 前端控制器
 * <p>
 * 当DispatcherServlet实例化时, 首先执行 init() 方法,
 * 这时会调用 HelperLoader.init() 方法来加载相关的helper类, 并注册处理相应资源的Servlet.
 * <p>
 * 对于每一次客户端请求都会执行 service() 方法, 这时会首先将请求方法和请求路径封装为Request对象,
 * 然后从映射处理器 (REQUEST_MAP) 中获取到处理器. 然后从客户端请求中获取到Param参数对象,
 * 执行处理器方法. 最后判断处理器方法的返回值,
 * 若为view类型, 则跳转到jsp页面, 若为data类型, 则返回json数据.
 *
 * @date 2020-07-02
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("程序初始化入口，程序加载中...");

        // 1 初始化相关的helper
        HelperLoader.init();

        // 2 获取ServletContext对象
        ServletContext servletContext = config.getServletContext();

        // 3 注册处理jsp和静态资源servlet
        registerServlet(servletContext);
    }

    /**
     * DefaultServlet和JspServlet都是由Web容器创建
     * org.apache.catalina.servlets.DefaultServlet
     * org.apache.jasper.servlet.JspServlet
     */
    private void registerServlet(ServletContext servletContext) {
        // 动态注册处理JSP的Servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");

        // 动态注册处理静态资源的默认Servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping("/favicon.ico"); // 网站头像
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
    }

    /**
     * 对于每一次客户端请求都会执行 service() 方法
     *
     * @param request  请求信息
     * @param response 响应信息
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestMethod = request.getMethod().toUpperCase();
        String requestPath = request.getPathInfo();

        // 这里根据Tomcat的配置路径有两种情况, 一种是 "/userList", 另一种是 "/context地址/userList".
        String[] splits = requestPath.split("/");
        if (splits.length > 2) {
            requestPath = "/" + splits[2];
        }

        // 根据请求获取处理器（这里类似于SpringMVC中的 映射处理器）
        Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
        if (handler != null) {
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass);

            // 初始化参数
            Param param = RequestHelper.createParam(request);

            // 调用与请求对应的方法（这里类似于SpringMVC中的 处理器适配器）
            Object result;
            try {
                Method actionMethod = handler.getControllerMethod();
                if (param == null || param.isEmpty()) {
                    result = ReflectionUtil.invokeMethod(controllerBean, actionMethod);
                } else {
                    result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
                }
            } catch (Exception e) {
                result = null;
                e.printStackTrace();
            }

            // 跳转页面或者返回json数据(类似于SpringMVC中 视图解析器)
            if (result instanceof View) {
                handleViewResult((View) result, request, response);
            } else if (result instanceof Data) {
                handleDataResult((Data) result, response);
            }
        }

    }

    /**
     * 跳转页面
     */
    private void handleViewResult(View view, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = view.getPath();
        if (!path.isEmpty()) {
            if (path.startsWith("/")) {// 重定向
                response.sendRedirect(request.getContextPath() + path);
            } else {// 请求转发
                Map<String, Object> model = view.getModel();
                for (Map.Entry<String, Object> entry : model.entrySet()) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                }

                request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);
            }
        }
    }

    /**
     * 返回JSON数据
     */
    private void handleDataResult(Data data, HttpServletResponse response) throws IOException {
        Object model = data.getModel();
        if (model != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            String json = JSON.toJSON(model).toString();
            writer.write(json);
            writer.flush();
            writer.close();
        }
    }
}
