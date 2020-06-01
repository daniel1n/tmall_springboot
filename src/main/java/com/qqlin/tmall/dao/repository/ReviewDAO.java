package com.qqlin.tmall.dao.repository;

import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewDAO extends JpaRepository<Review, Integer> {

    List<Review> findByProductOrderByIdDesc(Product product);

    int countByProduct(Product product);

}
