package com.study.basis.controller;

import com.study.basis.bean.User;
import com.study.basis.service.IUserService;
import com.study.spring.annotation.Autowried;
import com.study.spring.annotation.Controller;
import com.study.spring.annotation.RequestMapping;
import com.study.spring.bean.Data;
import com.study.spring.bean.Param;
import com.study.spring.bean.View;
import com.study.spring.constant.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @date 2020-07-01
 */
@Controller
public class UserController {

    @Autowried
    private IUserService userService;

    /**
     * 用户列表-测试IOC
     * <p>
     * 在浏览器中输入：http://127.0.0.1/userList，显示列表信息
     */
    @RequestMapping(value = "/userList", method = RequestMethod.GET)
    public View getUserList() {
        List<User> userList = userService.getAllUser();
        return new View("index.jsp").addModel("userList", userList);
    }

    /**
     * 用户详情
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public Data getUserInfo(Param param) {
        String id = (String) param.getParamMap().get("id");
        User user = userService.getUserInfoById(Integer.parseInt(id));
        return new Data(user);
    }

    @RequestMapping(value = "/userEdit", method = RequestMethod.GET)
    public Data editUser(Param param) {
        String id = (String) param.getParamMap().get("id");
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("age", 911);
        userService.updateUser(Integer.parseInt(id), fieldMap);
        return new Data("Success");
    }
}
