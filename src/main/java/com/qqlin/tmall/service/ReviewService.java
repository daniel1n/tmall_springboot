package com.qqlin.tmall.service;

import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.entity.Review;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qqlin
 */
@Service
public interface ReviewService {

    /**
     * 新增评论
     *
     * @param review 评论的实体类
     */
    void add(Review review);

    /**
     * 查询产品的所有评论
     *
     * @param product 产品
     * @return 产品的所有评论
     */
    List<Review> list(Product product);

    /**
     * 获得产品的数量
     *
     * @param product 产品
     * @return 返回产品的数量
     */
    int getCount(Product product);

}
