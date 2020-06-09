package com.qqlin.tmall.dao.repository;

import com.qqlin.tmall.dao.entity.Category;
import com.qqlin.tmall.dao.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author qqlin
 */
public interface ProductDAO extends JpaRepository<Product, Integer> {

    /**
     * 查询分类下的产品，对应的Page
     *
     * @param category 分类
     * @param pageable 分页
     * @return 分页对象
     */
    Page<Product> findByCategory(Category category, Pageable pageable);

    /**
     * 查询分类下的所有产品
     *
     * @param category 分类
     * @return 返回一个所有产品的列表
     */
    List<Product> findByCategoryOrderById(Category category);

    /**
     * 使用keyword从数据库，进行模糊查询，进而实现产品的搜索功能
     * 现在已经被ElasticSearch替代，已不使用
     *
     * @param keyword  关键字
     * @param pageable 分页
     * @return 返回查询到的产品列表
     */
    List<Product> findByNameLike(String keyword, Pageable pageable);
}
