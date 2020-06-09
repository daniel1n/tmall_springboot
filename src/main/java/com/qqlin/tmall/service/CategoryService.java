package com.qqlin.tmall.service;

import com.qqlin.tmall.dao.entity.Category;
import com.qqlin.tmall.util.Page4Navigator;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qingq
 */
@Service
public interface CategoryService {

    /**
     * 查询分类Category中的所有值
     *
     * @return 返回一个包含各种Category的list
     */
    List<Category> list();

    /**
     * 对分类Category进行分页查询
     *
     * @param start         从第几页开始
     * @param size          当页显示几条数据
     * @param navigatePages 表示导航栏中显示的个数
     * @return 返回一个根据既定约束，显示的list
     */
    Page4Navigator<Category> list(int start, int size, int navigatePages);

    /**
     * 增加分类Category
     *
     * @param bean 需要增加的Category
     */
    void add(Category bean);

    /**
     * 根据id删除分类
     *
     * @param id 需要删除的id
     */
    void delete(int id);

    /**
     * 根据id获取到该分类
     *
     * @param id 需要获取的分类id
     * @return 返回一个分类Category
     */
    Category get(int id);

    /**
     * 将传入的Category,进行更新
     *
     * @param bean 需要更新的Category
     */
    void update(Category bean);


    /**
     * 这个方法的用处是删除Product对象上的 分类。
     * 为什么要删除呢？ 因为在对分类做序列还转换为 json 的时候，
     * 会遍历里面的 products, 然后遍历出来的产品上，又会有分类，接着就开始子子孙孙无穷溃矣地遍历了
     * <p>
     * 而在这里去掉，就没事了。 只要在前端业务上，没有通过产品获取分类的业务，去掉也没有关系
     *
     * @param category 需要删除的分类Category
     */
    void removeCategoryFromProduct(Category category);

    /**
     * 删除Product对象上的 分类
     *
     * @param categories 需要删除的分类Category的list
     */
    void removeCategoryFromProduct(List<Category> categories);


}
