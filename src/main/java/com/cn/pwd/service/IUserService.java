package com.cn.pwd.service;

import com.cn.pwd.entity.User;

public interface IUserService {

      User insert(User user);

      User  findUserById(String id);

}
