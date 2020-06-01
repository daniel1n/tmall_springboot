package com.qqlin.tmall.service.impl;


import com.qqlin.tmall.dao.entity.Category;
import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.repository.CategoryDAO;
import com.qqlin.tmall.service.CategoryService;
import com.qqlin.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qqlin
 * @since 2020-6-1
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryDAO categoryDAO;


    @Override
    public Page4Navigator<Category> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page pageFromJPA = categoryDAO.findAll(pageable);

        return new Page4Navigator<>(pageFromJPA, navigatePages);
    }

    @Override
    public List<Category> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return categoryDAO.findAll(sort);
    }

    @Override
    public void add(Category bean) {
        categoryDAO.save(bean);
    }

    @Override
    public void delete(int id) {
        categoryDAO.deleteById(id);
    }

    @Override
    public Category get(int id) {
        Category c = categoryDAO.findById(id).get();
        return c;
    }

    @Override
    public void update(Category bean) {
        categoryDAO.save(bean);
    }


    @Override
    public void removeCategoryFromProduct(List<Category> cs) {
        for (Category category : cs) {
            removeCategoryFromProduct(category);
        }
    }

    @Override
    public void removeCategoryFromProduct(Category category) {
        List<Product> products = category.getProducts();
        if (null != products) {
            for (Product product : products) {
                product.setCategory(null);
            }
        }

        List<List<Product>> productsByRow = category.getProductsByRow();
        if (null != productsByRow) {
            for (List<Product> ps : productsByRow) {
                for (Product p : ps) {
                    p.setCategory(null);
                }
            }
        }
    }
}

