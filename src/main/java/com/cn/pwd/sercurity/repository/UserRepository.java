package com.cn.pwd.sercurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.domain.entity.User;

/**
 * @author Jonsy
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
