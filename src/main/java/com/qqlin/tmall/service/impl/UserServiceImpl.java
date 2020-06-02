package com.qqlin.tmall.service.impl;

import com.qqlin.tmall.dao.entity.User;
import com.qqlin.tmall.dao.repository.UserDAO;
import com.qqlin.tmall.service.UserService;
import com.qqlin.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
@CacheConfig(cacheNames = "users")
public class UserServiceImpl implements UserService {

    @Autowired
    UserDAO userDAO;


    @Override
    public boolean isExist(String name) {
        User user = getByName(name);
        return null != user;
    }


    @Override
    @Cacheable(key = "'users-one-name-'+ #p0")
    public User getByName(String name) {
        return userDAO.findByName(name);
    }


    @Override
    @Cacheable(key = "'users-one-name-'+ #p0 +'-password-'+ #p1")
    public User get(String name, String password) {
        return userDAO.getByNameAndPassword(name, password);
    }

    @Override
    @Cacheable(key = "'users-page-'+#p0+ '-' + #p1")
    public Page4Navigator<User> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page pageFromJPA = userDAO.findAll(pageable);
        return new Page4Navigator<>(pageFromJPA, navigatePages);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void add(User user) {
        userDAO.save(user);
    }

}
