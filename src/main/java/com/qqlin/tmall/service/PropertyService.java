package com.qqlin.tmall.service;

import com.qqlin.tmall.dao.entity.Category;
import com.qqlin.tmall.dao.entity.Property;
import com.qqlin.tmall.util.Page4Navigator;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qqlin
 */
@Service
public interface PropertyService {

    /**
     * 增加属性
     *
     * @param bean 增加的实体Property
     */
    void add(Property bean);

    /**
     * 删除属性
     *
     * @param id 属性对应的id
     */
    void delete(int id);

    /**
     * 获取属性
     *
     * @param id 属性的id
     * @return 返回Property
     */
    Property get(int id);

    /**
     * 更新属性
     *
     * @param bean 需要更新的实体Property
     */
    void update(Property bean);

    /**
     * 查询所有的属性值，进行分页
     *
     * @param cid           分类的id
     * @param start         分页的起始页
     * @param size          单页显示的条数
     * @param navigatePages 分页导航栏显示的分页个数
     * @return 返回已分页的属性列表
     */
    Page4Navigator<Property> list(int cid, int start, int size, int navigatePages);

    /**
     * 查询分类对应的属性值
     *
     * @param category 需要查询的分类
     * @return 返回属性列表
     */
    List<Property> listByCategory(Category category);

}
