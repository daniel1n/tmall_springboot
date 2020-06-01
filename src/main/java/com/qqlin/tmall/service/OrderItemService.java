package com.qqlin.tmall.service;

import com.qqlin.tmall.dao.entity.Order;
import com.qqlin.tmall.dao.entity.OrderItem;
import com.qqlin.tmall.dao.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qingq
 */
@Service
public interface OrderItemService {

    void fill(List<Order> orders);

    void update(OrderItem orderItem);

    void fill(Order order);

    void add(OrderItem orderItem);

    OrderItem get(int id);

    void delete(int id);

    int getSaleCount(Product product);

    List<OrderItem> listByProduct(Product product);

    List<OrderItem> listByOrder(Order order);


}
