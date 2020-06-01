package com.qqlin.tmall.service.impl;

import com.qqlin.tmall.dao.entity.Order;
import com.qqlin.tmall.dao.entity.OrderItem;
import com.qqlin.tmall.dao.repository.OrderDAO;
import com.qqlin.tmall.service.OrderService;
import com.qqlin.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qqlin
 * @since 2020-6-1
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDAO orderDAO;

    @Override
    public Page4Navigator<Order> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page pageFromJPA = orderDAO.findAll(pageable);
        return new Page4Navigator<>(pageFromJPA, navigatePages);
    }

    @Override
    public void removeOrderFromOrderItem(List<Order> orders) {
        for (Order order : orders) {
            removeOrderFromOrderItem(order);
        }
    }

    @Override
    public void removeOrderFromOrderItem(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(null);
        }
    }

    @Override
    public Order get(int oid) {
        return orderDAO.findById(oid).get();
    }

    @Override
    public void update(Order bean) {
        orderDAO.save(bean);
    }


}

