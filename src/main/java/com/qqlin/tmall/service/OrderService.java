package com.qqlin.tmall.service;

import com.qqlin.tmall.dao.entity.Order;
import com.qqlin.tmall.dao.entity.OrderItem;
import com.qqlin.tmall.dao.entity.User;
import com.qqlin.tmall.util.Page4Navigator;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qqlin
 */
@Service
public interface OrderService {

    String WAIT_PAY = "waitPay";
    String WAIT_DELIVERY = "waitDelivery";
    String WAIT_CONFIRM = "waitConfirm";
    String WAIT_REVIEW = "waitReview";
    String FINISH = "finish";
    String DELETE = "delete";

    /**
     * 查询订单，并分页
     *
     * @param start         起始页
     * @param size          当页显示的条数
     * @param navigatePages 分页
     * @return 返回订单的集合
     */
    Page4Navigator<Order> list(int start, int size, int navigatePages);

    /**
     * 从订单项中删除订单
     *
     * @param orders 多个订单
     */
    void removeOrderFromOrderItem(List<Order> orders);

    /**
     * 从订单项中删除订单
     *
     * @param order 订单
     */
    void removeOrderFromOrderItem(Order order);

    /**
     * 根据订单项，查询订单
     *
     * @param oid 订单项的id
     * @return 返回订单
     */
    Order get(int oid);

    /**
     * 更新订单
     *
     * @param bean 订单实体类
     */
    void update(Order bean);

    /**
     * 根据订单和订单项，计算出该订单的支付金额，显示在订单列表中
     *
     * @param order      订单
     * @param orderItems 订单项
     * @return 总的支付金额
     */
    float add(Order order, List<OrderItem> orderItems);

    /**
     * 订单新增
     *
     * @param order 订单实体类
     */
    void add(Order order);

    /**
     * 将没被删除的订单，填充订单项
     *
     * @param user 用户
     * @return 返回已填充订单项的订单
     */
    List<Order> listByUserWithoutDelete(User user);

    /**
     * 订单删除：只是把订单状态，标注为delete
     * 查询出所有没被删除的订单
     *
     * @param user 用户
     * @return 返回所有没删除的订单
     */
    List<Order> listByUserAndNotDeleted(User user);

    /**
     * 用于计算订单总金额
     *
     * @param order 订单
     */
    void calculation(Order order);
}
