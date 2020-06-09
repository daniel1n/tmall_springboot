package com.qqlin.tmall.dao.repository;

import com.qqlin.tmall.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author qqlin
 */
public interface UserDAO extends JpaRepository<User, Integer> {

    /**
     * 根据用户名，查询用户
     *
     * @param name 用户名
     * @return 用户信息
     */
    User findByName(String name);

    /**
     * 根据用户名和用户密码，获得用户
     *
     * @param name     用户名
     * @param password 用户密码
     * @return 用户
     */
    User getByNameAndPassword(String name, String password);

}
