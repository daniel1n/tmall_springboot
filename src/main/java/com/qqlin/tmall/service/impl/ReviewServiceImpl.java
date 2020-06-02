package com.qqlin.tmall.service.impl;

import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.entity.Review;
import com.qqlin.tmall.dao.repository.ReviewDAO;
import com.qqlin.tmall.service.ProductService;
import com.qqlin.tmall.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qqlin
 * @since 2020-6-1
 */
@Service
@CacheConfig(cacheNames = "reviews")
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewDAO reviewDAO;
    @Autowired
    ProductService productService;

    @Override
    @CacheEvict(allEntries = true)
    public void add(Review review) {
        reviewDAO.save(review);
    }

    @Override
    @Cacheable(key = "'reviews-pid-'+ #p0.id")
    public List<Review> list(Product product) {
        List<Review> result = reviewDAO.findByProductOrderByIdDesc(product);
        return result;
    }

    @Override
    @Cacheable(key = "'reviews-count-pid-'+ #p0.id")
    public int getCount(Product product) {
        return reviewDAO.countByProduct(product);
    }

}
