package com.qqlin.tmall.dao.repository;


import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.entity.Property;
import com.qqlin.tmall.dao.entity.PropertyValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyValueDAO extends JpaRepository<PropertyValue, Integer> {

    List<PropertyValue> findByProductOrderByIdDesc(Product product);

    PropertyValue getByPropertyAndProduct(Property property, Product product);


}
