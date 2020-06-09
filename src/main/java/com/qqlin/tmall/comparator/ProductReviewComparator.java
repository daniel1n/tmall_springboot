package com.qqlin.tmall.comparator;


import com.qqlin.tmall.dao.entity.Product;

import java.util.Comparator;

/**
 * ProductReviewComparator 人气比较器
 * 把 评价数量多的放前面
 *
 * @author qqlin
 * @date 2020-5-21
 */
public class ProductReviewComparator implements Comparator<Product> {

    @Override
    public int compare(Product o1, Product o2) {
        return o2.getReviewCount() - o1.getReviewCount();
    }
}
