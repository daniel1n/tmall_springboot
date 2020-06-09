package com.qqlin.tmall.dao.repository;

import com.qqlin.tmall.dao.entity.Category;
import com.qqlin.tmall.dao.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author qqlin
 */
public interface PropertyDAO extends JpaRepository<Property, Integer> {
    /**
     * 根据传入的Category和Pageable，查询相应的page
     *
     * @param category 分类
     * @param pageable 分页
     * @return 分页的对象
     */
    Page<Property> findByCategory(Category category, Pageable pageable);

    /**
     * 根据传入的Category，查询分类下的属性值
     *
     * @param category 分类
     * @return 属性值list
     */
    List<Property> findByCategory(Category category);

}
