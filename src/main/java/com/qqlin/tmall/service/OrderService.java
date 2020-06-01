package com.qqlin.tmall.service;

import com.qqlin.tmall.dao.entity.Order;
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

    public Page4Navigator<Order> list(int start, int size, int navigatePages);

    public void removeOrderFromOrderItem(List<Order> orders);

    void removeOrderFromOrderItem(Order order);

    public Order get(int oid);

    public void update(Order bean);


}
