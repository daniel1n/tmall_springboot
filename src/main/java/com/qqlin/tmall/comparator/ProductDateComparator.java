package com.qqlin.tmall.comparator;


import com.qqlin.tmall.dao.entity.Product;

import java.util.Comparator;

/**
 * @author qqlin
 * @date 2020-5-21
 */
public class ProductDateComparator implements Comparator<Product> {

    @Override
    public int compare(Product o1, Product o2) {
        return o2.getCreateDate().compareTo(o1.getCreateDate());
    }
}
