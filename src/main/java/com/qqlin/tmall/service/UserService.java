package com.qqlin.tmall.service;


import com.qqlin.tmall.dao.entity.User;
import com.qqlin.tmall.util.Page4Navigator;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    boolean isExist(String name);

    User getByName(String name);


    User get(String name, String password);

    Page4Navigator<User> list(int start, int size, int navigatePages);

    void add(User user);

}
