package com.qqlin.tmall.dao.repository;

import com.qqlin.tmall.dao.entity.Order;
import com.qqlin.tmall.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author qingq
 */
public interface OrderDAO extends JpaRepository<Order, Integer> {

    /**
     * 根据用户和订单状态，查询订单
     *
     * @param user   用户
     * @param status 订单状态
     * @return 返回订单集合
     */
    List<Order> findByUserAndStatusNotOrderByIdDesc(User user, String status);
}
