package com.cn.pwd.impl;


import com.cn.pwd.entity.User;
import com.cn.pwd.repository.UserRepository;
import com.cn.pwd.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User insert(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findUserById(String id) {
        return userRepository.findOne(id);
    }
}
