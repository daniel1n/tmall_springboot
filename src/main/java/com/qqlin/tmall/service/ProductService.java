package com.qqlin.tmall.service;

import com.qqlin.tmall.dao.entity.Category;
import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.util.Page4Navigator;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qingq
 */
@Service
public interface ProductService {

    void add(Product bean);

    void delete(int id);

    Product get(int id);

    void update(Product bean);

    Page4Navigator<Product> list(int cid, int start, int size, int navigatePages);

    void fill(List<Category> categorys);

    void fill(Category category);


    void fillByRow(List<Category> categorys);

    List<Product> listByCategory(Category category);

    void setSaleAndReviewNumber(Product product);

    void setSaleAndReviewNumber(List<Product> products);

    List<Product> search(String keyword, int start, int size);
}
