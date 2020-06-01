package com.qqlin.tmall.dao.repository;

import com.qqlin.tmall.dao.entity.Category;
import com.qqlin.tmall.dao.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyDAO extends JpaRepository<Property, Integer> {
    Page<Property> findByCategory(Category category, Pageable pageable);

    List<Property> findByCategory(Category category);

}
