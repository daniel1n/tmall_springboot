package com.qqlin.tmall.dao.repository;

import com.qqlin.tmall.dao.entity.Order;
import com.qqlin.tmall.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author qingq
 */
public interface OrderDAO extends JpaRepository<Order, Integer> {
    public List<Order> findByUserAndStatusNotOrderByIdDesc(User user, String statis);
}
