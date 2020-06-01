package com.qqlin.tmall.service.impl;

import com.qqlin.tmall.dao.entity.Order;
import com.qqlin.tmall.dao.entity.OrderItem;
import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.repository.OrderItemDAO;
import com.qqlin.tmall.service.OrderItemService;
import com.qqlin.tmall.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qqlin
 * @since 2020-6-1
 */
@Service
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    OrderItemDAO orderItemDAO;
    @Autowired
    ProductImageService productImageService;

    @Override
    public void fill(List<Order> orders) {
        for (Order order : orders) {
            fill(order);
        }
    }

    @Override
    public void update(OrderItem orderItem) {
        orderItemDAO.save(orderItem);
    }


    @Override
    public void fill(Order order) {
        List<OrderItem> orderItems = listByOrder(order);
        float total = 0;
        int totalNumber = 0;
        for (OrderItem oi : orderItems) {
            total += oi.getNumber() * oi.getProduct().getPromotePrice();
            totalNumber += oi.getNumber();
            productImageService.setFirstProductImage(oi.getProduct());
        }
        order.setTotal(total);
        order.setOrderItems(orderItems);
        order.setTotalNumber(totalNumber);
        order.setOrderItems(orderItems);
    }

    @Override
    public void add(OrderItem orderItem) {
        orderItemDAO.save(orderItem);
    }

    @Override
    public OrderItem get(int id) {
        return orderItemDAO.findById(id).get();
    }

    @Override
    public void delete(int id) {
        orderItemDAO.deleteById(id);
    }


    @Override
    public int getSaleCount(Product product) {
        List<OrderItem> ois = listByProduct(product);
        int result = 0;
        for (OrderItem oi : ois) {
            if (null != oi.getOrder()) {
                if (null != oi.getOrder() && null != oi.getOrder().getPayDate())
                    result += oi.getNumber();
            }
        }
        return result;
    }


    @Override
    public List<OrderItem> listByProduct(Product product) {
        return orderItemDAO.findByProduct(product);
    }

    @Override
    public List<OrderItem> listByOrder(Order order) {
        return orderItemDAO.findByOrderOrderByIdDesc(order);
    }


}

