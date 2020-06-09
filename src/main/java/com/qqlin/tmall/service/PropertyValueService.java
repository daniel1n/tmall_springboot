package com.qqlin.tmall.service;

import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.entity.Property;
import com.qqlin.tmall.dao.entity.PropertyValue;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qqlin
 */
@Service
public interface PropertyValueService {

    /**
     * 更新属性值
     *
     * @param bean 属性值的实体类
     */
    void update(PropertyValue bean);

    /**
     * 初始化产品的属性
     *
     * @param product 产品
     */
    void init(Product product);

    /**
     * 和根据产品和属性获取PropertyValue对象
     *
     * @param product  产品
     * @param property 属性
     * @return 产品的属性值
     */
    PropertyValue getByPropertyAndProduct(Product product, Property property);

    /**
     * 查询产品的所有属性值
     *
     * @param product 产品
     * @return 产品的属性值集合
     */
    List<PropertyValue> list(Product product);

}
