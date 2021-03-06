package com.qqlin.tmall.comparator;


import com.qqlin.tmall.dao.entity.Product;

import java.util.Comparator;

/**
 * ProductAllComparator 综合比较器
 * 把 销量x评价 高的放前面
 *
 * @author qqlin
 * @date 2020-5-21
 */
public class ProductAllComparator implements Comparator<Product> {

    @Override
    public int compare(Product o1, Product o2) {
        return o2.getSaleCount() * o2.getReviewCount() - o1.getSaleCount() * o2.getReviewCount();
    }
}
