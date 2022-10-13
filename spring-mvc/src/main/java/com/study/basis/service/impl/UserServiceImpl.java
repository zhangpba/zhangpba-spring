package com.study.basis.service.impl;

import com.study.basis.bean.User;
import com.study.basis.service.IUserService;
import com.study.spring.annotation.Service;
import com.study.spring.annotation.Transactional;
import com.study.spring.helper.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements IUserService {

    /**
     * 获取所有用户-测试IOC
     */
    @Override
    public List<User> getAllUser() {
//        List<User> userList = new ArrayList<>();
//        userList.add(new User(1, "Tom", 22));
//        userList.add(new User(2, "Alic", 12));
//        userList.add(new User(3, "Bob", 32));
//        return userList;

//        try {
//            Thread.sleep(1000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        String sql = "SELECT user_id as id,username name,age FROM user";
        return DatabaseHelper.queryEntityList(User.class, sql);
    }

    /**
     * 根据id获取用户信息
     */
    @Override
    public User getUserInfoById(Integer id) {
        String sql = "select user_id as id,username name,age from user where user_id = ?";
        return DatabaseHelper.queryEntity(User.class, sql, id);
    }

    /**
     * 修改用户信息
     */
    @Transactional
    @Override
    public boolean updateUser(int id, Map<String, Object> fieldMap) {
        return DatabaseHelper.updateEntity(User.class, id, fieldMap);
    }
}
