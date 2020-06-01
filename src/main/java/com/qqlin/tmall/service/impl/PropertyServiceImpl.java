package com.qqlin.tmall.service.impl;

import com.qqlin.tmall.dao.entity.Category;
import com.qqlin.tmall.dao.entity.Property;
import com.qqlin.tmall.dao.repository.PropertyDAO;
import com.qqlin.tmall.service.CategoryService;
import com.qqlin.tmall.service.PropertyService;
import com.qqlin.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qqlin
 * @since 2020-6-1
 */
@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    PropertyDAO propertyDAO;
    @Autowired
    CategoryService categoryService;

    @Override
    public void add(Property bean) {
        propertyDAO.save(bean);
    }

    @Override
    public void delete(int id) {
        propertyDAO.deleteById(id);
    }

    @Override
    public Property get(int id) {
        return propertyDAO.findById(id).get();
    }

    @Override
    public void update(Property bean) {
        propertyDAO.save(bean);
    }

    @Override
    public Page4Navigator<Property> list(int cid, int start, int size, int navigatePages) {
        Category category = categoryService.get(cid);

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);

        Page<Property> pageFromJPA = propertyDAO.findByCategory(category, pageable);

        return new Page4Navigator<>(pageFromJPA, navigatePages);


    }

    @Override
    public List<Property> listByCategory(Category category) {
        return propertyDAO.findByCategory(category);
    }

}

