package com.qqlin.tmall.service.impl;

import com.qqlin.tmall.dao.entity.Category;
import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.repository.CategoryDAO;
import com.qqlin.tmall.service.CategoryService;
import com.qqlin.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
@CacheConfig(cacheNames = "categories")
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryDAO categoryDAO;


    @Override
    @Cacheable(key = "'categories-page-' + #p0 + '-' + #p1")
    public Page4Navigator list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Category> pageFromJpa = categoryDAO.findAll(pageable);

        return new Page4Navigator<>(pageFromJpa, navigatePages);
    }

    @Override
    @Cacheable(key = "'categories-all'")
    public List<Category> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return categoryDAO.findAll(sort);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void add(Category bean) {
        categoryDAO.save(bean);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delete(int id) {
        categoryDAO.delete(id);
    }

    @Override
    @Cacheable(key = "'categiries-one-' + #p0")
    public Category get(int id) {
        Category c = categoryDAO.findOne(id);
        return c;
    }

    @Override
    @CacheEvict(allEntries = true)
    public void update(Category bean) {
        categoryDAO.save(bean);
    }


    /**
     * 这个方法的用处是删除Product对象上的 分类。
     * 为什么要删除呢？ 因为在对分类做序列还转换为 json 的时候，
     * 会遍历里面的 products, 然后遍历出来的产品上，又会有分类，接着就开始子子孙孙无穷溃矣地遍历了
     * <p>
     * 而在这里去掉，就没事了。 只要在前端业务上，没有通过产品获取分类的业务，去掉也没有关系
     *
     * @param cs
     */
    @Override
    public void removeCategoryFromProduct(List<Category> cs) {
        for (Category category : cs) {
            removeCategoryFromProduct(category);
        }
    }

    @Override
    public void removeCategoryFromProduct(Category category) {
        List<Product> products = category.getProducts();
        if (null != products) {
            for (Product product : products) {
                product.setCategory(null);
            }
        }

        List<List<Product>> productsByRow = category.getProductsByRow();
        if (null != productsByRow) {
            for (List<Product> ps : productsByRow) {
                for (Product p : ps) {
                    p.setCategory(null);
                }
            }
        }
    }
}

