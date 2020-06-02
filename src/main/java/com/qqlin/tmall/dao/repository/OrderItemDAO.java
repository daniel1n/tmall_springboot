package com.qqlin.tmall.dao.repository;

import com.qqlin.tmall.dao.entity.Order;
import com.qqlin.tmall.dao.entity.OrderItem;
import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemDAO extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrderOrderByIdDesc(Order order);

    List<OrderItem> findByProduct(Product product);

    List<OrderItem> findByUserAndOrderIsNull(User user);
}
