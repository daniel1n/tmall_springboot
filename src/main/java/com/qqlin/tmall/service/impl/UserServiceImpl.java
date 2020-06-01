package com.qqlin.tmall.service.impl;

import com.qqlin.tmall.dao.entity.User;
import com.qqlin.tmall.dao.repository.UserDAO;
import com.qqlin.tmall.service.UserService;
import com.qqlin.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author qqlin
 * @since 2020-6-1
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDAO userDAO;


    @Override
    public boolean isExist(String name) {
        User user = getByName(name);
        return null != user;
    }


    @Override
    public User getByName(String name) {
        return userDAO.findByName(name);
    }


    @Override
    public User get(String name, String password) {
        return userDAO.getByNameAndPassword(name, password);
    }

    @Override
    public Page4Navigator<User> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page pageFromJPA = userDAO.findAll(pageable);
        return new Page4Navigator<>(pageFromJPA, navigatePages);
    }

    @Override
    public void add(User user) {
        userDAO.save(user);
    }

}
