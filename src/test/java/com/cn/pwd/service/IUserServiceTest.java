package com.cn.pwd.service;

import com.cn.pwd.ApplicationTest;
import com.cn.pwd.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class IUserServiceTest extends ApplicationTest {

    @Autowired
    IUserService iUserService;

    @Test
    public void insert() {
        User user = new User();
        user.setId("90321");
        user.setName("peter");
        user.setAdress("南京市雨花台区");
        user.setEmail("619774333@qq.com");
        user.setOrgId("runhepoer");
        user.setPhone("15927862169");
        user.setPassWord("123456");
        iUserService.insert(user);
    }

    @Test
    public void findUserById() {

        User userById = iUserService.findUserById("90321");


    }
}