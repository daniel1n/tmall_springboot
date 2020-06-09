package com.qqlin.tmall.service;

import com.qqlin.tmall.dao.entity.Order;
import com.qqlin.tmall.dao.entity.OrderItem;
import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qingq
 */
@Service
public interface OrderItemService {

    /**
     * 订单填充内容
     *
     * @param order 订单
     */
    void fill(Order order);

    /**
     * 为多个订单填充内容
     *
     * @param orders 多个订单
     */
    void fill(List<Order> orders);

    /**
     * 更新订单中的订单项
     *
     * @param orderItem 订单项
     */
    void update(OrderItem orderItem);

    /**
     * 新增订单项
     *
     * @param orderItem 订单项
     */
    void add(OrderItem orderItem);

    /**
     * 获取订单项
     *
     * @param id 订单项的id
     * @return 返回一个订单
     */
    OrderItem get(int id);

    /**
     * 删除订单
     *
     * @param id 订单的id
     */
    void delete(int id);

    /**
     * 获取产品的销售数量
     *
     * @param product 产品
     * @return 返回销售数量
     */
    int getSaleCount(Product product);

    /**
     * 购物车功能
     * 根据产品，查找所有的订单项
     *
     * @param product 产品
     * @return 所有订单项
     */
    List<OrderItem> listByProduct(Product product);

    /**
     * 订单功能
     * 根据订单查找订单项
     *
     * @param order 订单
     * @return 所有的订单项
     */
    List<OrderItem> listByOrder(Order order);

    /**
     * 查找用户的订单项
     *
     * @param user 用户
     * @return 所有的订单项
     */
    List<OrderItem> listByUser(User user);
}
