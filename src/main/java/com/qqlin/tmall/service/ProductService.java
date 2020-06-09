package com.qqlin.tmall.service;

import com.qqlin.tmall.dao.entity.Category;
import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.util.Page4Navigator;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qingq
 */
@Service
public interface ProductService {

    /**
     * 增加产品
     *
     * @param bean 需要增加的产品实体类
     */
    void add(Product bean);

    /**
     * 删除产品
     *
     * @param id 需要删除的产品id
     */
    void delete(int id);

    /**
     * 获取产品
     *
     * @param id 产品的id
     * @return
     */
    Product get(int id);

    /**
     * 更新产品
     *
     * @param bean 需要更新的实体Product
     */
    void update(Product bean);

    /**
     * 查询分类下的所有产品，并进行分页
     *
     * @param cid           分类id
     * @param start         开始的页码
     * @param size          当页显示的条数
     * @param navigatePages 导航栏显示的页码数量
     * @return 返回所有该分类下的所有产品
     */
    Page4Navigator<Product> list(int cid, int start, int size, int navigatePages);

    /**
     * 为分类填充产品集合
     *
     * @param category 分类
     */
    void fill(Category category);

    /**
     * 为多个分类填充产品集合
     *
     * @param categories 多个分类
     */
    void fill(List<Category> categories);

    /**
     * 为多个分类填充推荐产品集合
     *
     * @param categories 多个分类
     */
    void fillByRow(List<Category> categories);

    /**
     * 查询某个分类下的所有产品
     *
     * @param category 分类
     * @return 返回查询到产品列表
     */
    List<Product> listByCategory(Category category);

    /**
     * 为产品设置销量和评价数量的方法
     *
     * @param product 产品
     */
    void setSaleAndReviewNumber(Product product);

    /**
     * 为多个产品设置销量和评价数量的方法
     *
     * @param products 多个产品
     */
    void setSaleAndReviewNumber(List<Product> products);

    /**
     * 以前查询是模糊查询，是直接到数据库的
     * 现在是通过 ProductESDAO 到 ElasticSearch 中进行查询了
     *
     * @param keyword 查询提供的关键字
     * @param start   分页显示的初始页码
     * @param size    当页显示的条数
     * @return 返回查询到产品
     */
    List<Product> search(String keyword, int start, int size);

    /**
     * 初始化数据到ElasticSearch.
     * 因为数据刚开始都在数据库中，不在ElasticSearch中，
     * 所以刚开始查询，先看看ElasticSearch有没有数据，
     * 如果没有，就把数据从数据库同步到ElasticSearch中。
     */
    void initDatabase2ES();
}
