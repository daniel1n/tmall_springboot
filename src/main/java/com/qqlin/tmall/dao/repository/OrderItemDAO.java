package com.qqlin.tmall.dao.repository;

import com.qqlin.tmall.dao.entity.Order;
import com.qqlin.tmall.dao.entity.OrderItem;
import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author qqlin
 */
public interface OrderItemDAO extends JpaRepository<OrderItem, Integer> {

    /**
     * 根据订单查找订单项，并按照倒序排序
     *
     * @param order 订单
     * @return 返回订单项的集合
     */
    List<OrderItem> findByOrderOrderByIdDesc(Order order);

    /**
     * 根据产品查找订单项
     *
     * @param product 产品
     * @return 订单项的集合
     */
    List<OrderItem> findByProduct(Product product);

    /**
     * 根据用户查找订单项，且用户的订单为空
     *
     * @param user 用户
     * @return 返回订单项列表
     */
    List<OrderItem> findByUserAndOrderIsNull(User user);
}
