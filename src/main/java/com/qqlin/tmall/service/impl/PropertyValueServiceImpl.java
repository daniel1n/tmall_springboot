package com.qqlin.tmall.service.impl;

import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.entity.Property;
import com.qqlin.tmall.dao.entity.PropertyValue;
import com.qqlin.tmall.dao.repository.PropertyValueDAO;
import com.qqlin.tmall.service.PropertyService;
import com.qqlin.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qqlin
 * @since 2020-6-1
 */
@Service
public class PropertyValueServiceImpl implements PropertyValueService {

    @Autowired
    PropertyValueDAO propertyValueDAO;
    @Autowired
    PropertyService propertyService;

    @Override
    public void update(PropertyValue bean) {
        propertyValueDAO.save(bean);
    }

    @Override
    public void init(Product product) {
        List<Property> propertys = propertyService.listByCategory(product.getCategory());
        for (Property property : propertys) {
            PropertyValue propertyValue = getByPropertyAndProduct(product, property);
            if (null == propertyValue) {
                propertyValue = new PropertyValue();
                propertyValue.setProduct(product);
                propertyValue.setProperty(property);
                propertyValueDAO.save(propertyValue);
            }
        }
    }

    @Override
    public PropertyValue getByPropertyAndProduct(Product product, Property property) {
        return propertyValueDAO.getByPropertyAndProduct(property, product);
    }

    @Override
    public List<PropertyValue> list(Product product) {
        return propertyValueDAO.findByProductOrderByIdDesc(product);
    }


}

