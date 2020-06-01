package com.qqlin.tmall.service.impl;

import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.entity.Review;
import com.qqlin.tmall.dao.repository.ReviewDAO;
import com.qqlin.tmall.service.ProductService;
import com.qqlin.tmall.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qqlin
 * @since 2020-6-1
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewDAO reviewDAO;
    @Autowired
    ProductService productService;

    @Override
    public void add(Review review) {
        reviewDAO.save(review);
    }

    @Override
    public List<Review> list(Product product) {
        List<Review> result = reviewDAO.findByProductOrderByIdDesc(product);
        return result;
    }

    @Override
    public int getCount(Product product) {
        return reviewDAO.countByProduct(product);
    }

}
