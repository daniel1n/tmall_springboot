package com.qqlin.tmall.dao.repository;


import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.entity.Property;
import com.qqlin.tmall.dao.entity.PropertyValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author qqlin
 */
public interface PropertyValueDAO extends JpaRepository<PropertyValue, Integer> {

    /**
     * 查询产品对应的属性值，并按id的倒序排列
     *
     * @param product 产品
     * @return 属性集合
     */
    List<PropertyValue> findByProductOrderByIdDesc(Product product);

    /**
     * 根据产品和属性，获取产品属性值
     *
     * @param property 属性
     * @param product  产品
     * @return 属性值
     */
    PropertyValue getByPropertyAndProduct(Property property, Product product);

}
