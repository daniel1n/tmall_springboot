package com.qqlin.tmall.service.impl;

import com.qqlin.tmall.dao.entity.OrderItem;
import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.entity.ProductImage;
import com.qqlin.tmall.dao.repository.ProductImageDAO;
import com.qqlin.tmall.service.ProductImageService;
import com.qqlin.tmall.service.ProductService;
import com.qqlin.tmall.util.SpringContextUtil;
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
@CacheConfig(cacheNames = "productImages")
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    private ProductImageDAO productImageDAO;
    @Autowired
    private ProductService productService;

    @Override
    @CacheEvict(allEntries = true)
    public void add(ProductImage bean) {
        productImageDAO.save(bean);

    }

    @Override
    @CacheEvict(allEntries = true)
    public void delete(int id) {
        productImageDAO.delete(id);
    }

    @Override
    @Cacheable(key = "'productImages-one-'+ #p0")
    public ProductImage get(int id) {
        return productImageDAO.findOne(id);
    }

    @Override
    @Cacheable(key = "'productImages-single-pid-'+ #p0.id")
    public List<ProductImage> listSingleProductImages(Product product) {
        return productImageDAO.findByProductAndTypeOrderByIdDesc(product, TYPE_SINGLE);
    }

    @Override
    @Cacheable(key = "'productImages-detail-pid-'+ #p0.id")
    public List<ProductImage> listDetailProductImages(Product product) {
        return productImageDAO.findByProductAndTypeOrderByIdDesc(product, TYPE_DETAIL);
    }

    @Override
    public void setFirstProductImage(Product product) {
//        List<ProductImage> singleImages = listSingleProductImages(product);
        ProductImageService productImageService = SpringContextUtil.getBean(ProductImageService.class);
        List<ProductImage> singleImages = productImageService.listSingleProductImages(product);
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

    @Override
    public void setFirstProductImagesOnOrderItems(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            setFirstProductImage(orderItem.getProduct());
        }
    }

}
