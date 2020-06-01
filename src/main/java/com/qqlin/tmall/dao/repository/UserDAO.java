package com.qqlin.tmall.dao.repository;

import com.qqlin.tmall.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, Integer> {

    User findByName(String name);

    User getByNameAndPassword(String name, String password);

}
