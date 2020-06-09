package com.qqlin.tmall.service;


import com.qqlin.tmall.dao.entity.User;
import com.qqlin.tmall.util.Page4Navigator;
import org.springframework.stereotype.Service;

/**
 * @author qqlin
 */
@Service
public interface UserService {

    /**
     * 判断用户名是否存在
     * 用户账户注册，或者账户登录
     *
     * @param name 用户名
     * @return 返回布尔值
     */
    boolean isExist(String name);

    /**
     * 根据用户名获得用户信息
     *
     * @param name 用户名
     * @return 返回一个用户
     */
    User getByName(String name);

    /**
     * 根据用户名和密码获得用户信息
     *
     * @param name     用户名
     * @param password 用户密码
     * @return 返回一个用户
     */
    User get(String name, String password);

    /**
     * 获取全部用户，并分页显示
     *
     * @param start         起始页
     * @param size          当页显示的条数
     * @param navigatePages 导航栏显示的页码数
     * @return 返回一个用户集合
     */
    Page4Navigator<User> list(int start, int size, int navigatePages);

    /**
     * 新增用户
     *
     * @param user 需要增加的用户
     */
    void add(User user);

}
