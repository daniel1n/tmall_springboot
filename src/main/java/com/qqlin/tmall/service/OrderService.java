package com.qqlin.tmall.service;

import com.qqlin.tmall.dao.entity.Order;
import com.qqlin.tmall.dao.entity.OrderItem;
import com.qqlin.tmall.dao.entity.User;
import com.qqlin.tmall.util.Page4Navigator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    String waitPay = "waitPay";
    String waitDelivery = "waitDelivery";
    String waitConfirm = "waitConfirm";
    String waitReview = "waitReview";
    String finish = "finish";
    String delete = "delete";

    Page4Navigator<Order> list(int start, int size, int navigatePages);

    void removeOrderFromOrderItem(List<Order> orders);

    void removeOrderFromOrderItem(Order order);

    Order get(int oid);

    void update(Order bean);

    float add(Order order, List<OrderItem> ois);

    void add(Order order);

    List<Order> listByUserWithoutDelete(User user);

    List<Order> listByUserAndNotDeleted(User user);

    void cacl(Order order);
}
