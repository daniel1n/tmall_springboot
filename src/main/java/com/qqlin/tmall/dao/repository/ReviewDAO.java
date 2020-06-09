package com.qqlin.tmall.dao.repository;

import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author qqlin
 */
public interface ReviewDAO extends JpaRepository<Review, Integer> {

    /**
     * 查找产品的评论，并以倒序排序
     *
     * @param product 产品
     * @return 返回所有的评论
     */
    List<Review> findByProductOrderByIdDesc(Product product);

    /**
     * 查询产品的数量
     *
     * @param product 产品
     * @return 返回产品的数量
     */
    int countByProduct(Product product);

}
