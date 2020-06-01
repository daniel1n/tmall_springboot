package com.qqlin.tmall.service;

import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.entity.Property;
import com.qqlin.tmall.dao.entity.PropertyValue;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PropertyValueService {

    void update(PropertyValue bean);

    void init(Product product);

    PropertyValue getByPropertyAndProduct(Product product, Property property);

    List<PropertyValue> list(Product product);

}
