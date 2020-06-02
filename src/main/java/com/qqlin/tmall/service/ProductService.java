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

    /**
     * 初始化数据到es.
     * 因为数据刚开始都在数据库中，不在es中，
     * 所以刚开始查询，先看看es有没有数据，如果没有，就把数据从数据库同步到es中。
     */
    void initDatabase2ES();
}
