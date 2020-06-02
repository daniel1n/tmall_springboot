package com.qqlin.tmall.service;

import com.qqlin.tmall.dao.entity.OrderItem;
import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.entity.ProductImage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductImageService {

    String type_single = "single";
    String type_detail = "detail";


    void add(ProductImage bean);

    void delete(int id);

    ProductImage get(int id);

    List<ProductImage> listSingleProductImages(Product product);

    List<ProductImage> listDetailProductImages(Product product);

    void setFirstProductImage(Product product);

    void setFirstProductImages(List<Product> products);

    void setFirstProductImagesOnOrderItems(List<OrderItem> orderItems);
}
