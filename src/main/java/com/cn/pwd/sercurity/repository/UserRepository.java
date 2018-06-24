package com.cn.pwd.sercurity.repository;

import com.cn.pwd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Jonsy
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
