package com.qqlin.tmall.service.impl;

import com.qqlin.tmall.dao.entity.Order;
import com.qqlin.tmall.dao.entity.OrderItem;
import com.qqlin.tmall.dao.entity.User;
import com.qqlin.tmall.dao.repository.OrderDAO;
import com.qqlin.tmall.service.OrderItemService;
import com.qqlin.tmall.service.OrderService;
import com.qqlin.tmall.util.Page4Navigator;
import com.qqlin.tmall.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author qqlin
 * @since 2020-6-1
 */
@Service
@CacheConfig(cacheNames = "orders")
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDAO orderDAO;
    @Autowired
    OrderItemService orderItemService;

    @Override
    @Cacheable(key = "'orders-page-'+#p0+ '-' + #p1")
    public Page4Navigator<Order> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
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
    @Cacheable(key = "'orders-one-'+ #p0")
    public Order get(int oid) {
        return orderDAO.findOne(oid);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void update(Order bean) {
        orderDAO.save(bean);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
    public float add(Order order, List<OrderItem> ois) {
        float total = 0;
        add(order);

        /**
         * 故意抛出异常代码用来模拟当增加订单后出现异常，观察事务管理是否预期发生。
         * （需要把false修改为true才能观察到）
         */
        if (false) {
            throw new RuntimeException();
        }

        for (OrderItem oi : ois) {
            oi.setOrder(order);
            orderItemService.update(oi);
            total += oi.getProduct().getPromotePrice() * oi.getNumber();
        }
        return total;
    }

    @Override
    @CacheEvict(allEntries = true)
    public void add(Order order) {
        orderDAO.save(order);
    }

    @Override
    @Cacheable(key = "'orders-uid-'+ #p0.id")
    public List<Order> listByUserAndNotDeleted(User user) {
        return orderDAO.findByUserAndStatusNotOrderByIdDesc(user, OrderService.delete);
    }

    @Override
    public List<Order> listByUserWithoutDelete(User user) {
        OrderService orderService = SpringContextUtil.getBean(OrderService.class);
        List<Order> orders = orderService.listByUserAndNotDeleted(user);
        orderItemService.fill(orders);
        return orders;
    }

    @Override
    public void cacl(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        float total = 0;
        for (OrderItem orderItem : orderItems) {
            total += orderItem.getProduct().getPromotePrice() * orderItem.getNumber();
        }
        order.setTotal(total);
    }

}

