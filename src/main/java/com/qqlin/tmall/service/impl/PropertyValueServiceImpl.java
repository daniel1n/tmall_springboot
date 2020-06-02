package com.qqlin.tmall.service.impl;

import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.entity.Property;
import com.qqlin.tmall.dao.entity.PropertyValue;
import com.qqlin.tmall.dao.repository.PropertyValueDAO;
import com.qqlin.tmall.service.PropertyService;
import com.qqlin.tmall.service.PropertyValueService;
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
@CacheConfig(cacheNames = "propertyValues")
public class PropertyValueServiceImpl implements PropertyValueService {

    @Autowired
    PropertyValueDAO propertyValueDAO;
    @Autowired
    PropertyService propertyService;


    @Override
    @CacheEvict(allEntries = true)
    public void update(PropertyValue bean) {
        propertyValueDAO.save(bean);
    }

    @Override
    public void init(Product product) {
        PropertyValueService propertyValueService = SpringContextUtil.getBean(PropertyValueService.class);

        List<Property> properties = propertyService.listByCategory(product.getCategory());
        for (Property property : properties) {
            PropertyValue propertyValue = propertyValueService.getByPropertyAndProduct(product, property);
            if (null == propertyValue) {
                propertyValue = new PropertyValue();
                propertyValue.setProduct(product);
                propertyValue.setProperty(property);
                propertyValueDAO.save(propertyValue);
            }
        }
    }

    @Override
    @Cacheable(key = "'propertyValues-one-pid-'+#p0.id+ '-ptid-' + #p1.id")
    public PropertyValue getByPropertyAndProduct(Product product, Property property) {
        return propertyValueDAO.getByPropertyAndProduct(property, product);
    }

    @Override
    @Cacheable(key = "'propertyValues-pid-'+ #p0.id")
    public List<PropertyValue> list(Product product) {
        return propertyValueDAO.findByProductOrderByIdDesc(product);
    }


}

