package com.cn.pwd.controller.system;

import com.cn.pwd.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/user")
public class UserResource {

    private static final Logger logger =  LoggerFactory.getLogger(UserResource.class);

    @Resource
    private IUserService iUserService;

    @GetMapping
    public String index(){
        return "hello world";
    }



}
