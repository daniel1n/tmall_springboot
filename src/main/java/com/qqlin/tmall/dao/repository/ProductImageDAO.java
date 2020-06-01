package com.qqlin.tmall.dao.repository;

import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageDAO extends JpaRepository<ProductImage, Integer> {
    public List<ProductImage> findByProductAndTypeOrderByIdDesc(Product product, String type);

}
