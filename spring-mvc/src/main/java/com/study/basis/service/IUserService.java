package com.study.basis.service;

import com.study.basis.bean.User;

import java.util.List;
import java.util.Map;

public interface IUserService {

    public List<User> getAllUser();

    User getUserInfoById(Integer id);

    boolean updateUser(int id, Map<String, Object> fieldMap);
}
