package com.qqlin.tmall.service.impl;

import com.qqlin.tmall.dao.entity.Category;
import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.repository.ProductDAO;
import com.qqlin.tmall.service.*;
import com.qqlin.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qqlin
 * @since 2020-6-1
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDAO productDAO;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    ReviewService reviewService;

    @Override
    public void add(Product bean) {
        productDAO.save(bean);
    }

    @Override
    public void delete(int id) {
        productDAO.deleteById(id);
    }

    @Override
    public Product get(int id) {
        return productDAO.findById(id).get();
    }

    @Override
    public void update(Product bean) {
        productDAO.save(bean);
    }

    @Override
    public Page4Navigator<Product> list(int cid, int start, int size, int navigatePages) {
        Category category = categoryService.get(cid);
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Product> pageFromJPA = productDAO.findByCategory(category, pageable);
        return new Page4Navigator<>(pageFromJPA, navigatePages);
    }

    @Override
    public void fill(List<Category> categorys) {
        for (Category category : categorys) {
            fill(category);
        }
    }

    @Override
    public void fill(Category category) {
        List<Product> products = listByCategory(category);
        productImageService.setFirstProductImages(products);
        category.setProducts(products);
    }


    @Override
    public void fillByRow(List<Category> categorys) {
        int productNumberEachRow = 8;
        for (Category category : categorys) {
            List<Product> products = category.getProducts();
            List<List<Product>> productsByRow = new ArrayList<>();
            for (int i = 0; i < products.size(); i += productNumberEachRow) {
                int size = i + productNumberEachRow;
                size = size > products.size() ? products.size() : size;
                List<Product> productsOfEachRow = products.subList(i, size);
                productsByRow.add(productsOfEachRow);
            }
            category.setProductsByRow(productsByRow);
        }
    }

    @Override
    public List<Product> listByCategory(Category category) {
        return productDAO.findByCategoryOrderById(category);
    }

    @Override
    public void setSaleAndReviewNumber(Product product) {
        int saleCount = orderItemService.getSaleCount(product);
        product.setSaleCount(saleCount);

        int reviewCount = reviewService.getCount(product);
        product.setReviewCount(reviewCount);

    }


    @Override
    public void setSaleAndReviewNumber(List<Product> products) {
        for (Product product : products) {
            setSaleAndReviewNumber(product);
        }
    }

    @Override
    public List<Product> search(String keyword, int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        List<Product> products = productDAO.findByNameLike("%" + keyword + "%", pageable);
        return products;
    }
}
