package com.qqlin.tmall.dao.repository;

import com.qqlin.tmall.dao.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDAO extends JpaRepository<Order, Integer> {
}
