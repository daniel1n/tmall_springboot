package com.qqlin.tmall.service.impl;

import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.entity.ProductImage;
import com.qqlin.tmall.dao.repository.ProductImageDAO;
import com.qqlin.tmall.service.ProductImageService;
import com.qqlin.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qqlin
 * @since 2020-6-1
 */
@Service
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    ProductImageDAO productImageDAO;
    @Autowired
    ProductService productService;


    @Override
    public void add(ProductImage bean) {
        productImageDAO.save(bean);

    }

    @Override
    public void delete(int id) {
        productImageDAO.deleteById(id);
    }

    @Override
    public ProductImage get(int id) {
        return productImageDAO.findById(id).get();
    }

    @Override
    public List<ProductImage> listSingleProductImages(Product product) {
        return productImageDAO.findByProductAndTypeOrderByIdDesc(product, type_single);
    }

    @Override
    public List<ProductImage> listDetailProductImages(Product product) {
        return productImageDAO.findByProductAndTypeOrderByIdDesc(product, type_detail);
    }

    @Override
    public void setFirstProductImage(Product product) {
        List<ProductImage> singleImages = listSingleProductImages(product);
        if (!singleImages.isEmpty()) {
            product.setFirstProductImage(singleImages.get(0));
        } else {
            product.setFirstProductImage(new ProductImage());
        }

    }

    @Override
    public void setFirstProductImages(List<Product> products) {
        for (Product product : products) {
            setFirstProductImage(product);
        }
    }


}
