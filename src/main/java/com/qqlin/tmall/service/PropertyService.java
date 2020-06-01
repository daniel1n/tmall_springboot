package com.qqlin.tmall.service;

import com.qqlin.tmall.dao.entity.Category;
import com.qqlin.tmall.dao.entity.Property;
import com.qqlin.tmall.util.Page4Navigator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PropertyService {

    void add(Property bean);

    void delete(int id);

    Property get(int id);

    void update(Property bean);

    Page4Navigator<Property> list(int cid, int start, int size, int navigatePages);

    List<Property> listByCategory(Category category);

}
