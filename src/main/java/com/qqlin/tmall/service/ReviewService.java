package com.qqlin.tmall.service;

import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.entity.Review;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {

    void add(Review review);

    List<Review> list(Product product);

    int getCount(Product product);

}
