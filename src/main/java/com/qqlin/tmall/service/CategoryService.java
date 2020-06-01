package com.qqlin.tmall.service;

import com.qqlin.tmall.dao.entity.Category;
import com.qqlin.tmall.util.Page4Navigator;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qingq
 */
@Service
public interface CategoryService {

    Page4Navigator<Category> list(int start, int size, int navigatePages);

    List<Category> list();

    void add(Category bean);

    void delete(int id);

    Category get(int id);

    void update(Category bean);

    void removeCategoryFromProduct(List<Category> cs);

    void removeCategoryFromProduct(Category category);
}
