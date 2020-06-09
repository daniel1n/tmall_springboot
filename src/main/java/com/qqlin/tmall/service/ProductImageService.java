package com.qqlin.tmall.service;

import com.qqlin.tmall.dao.entity.OrderItem;
import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.entity.ProductImage;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qqlin
 */
@Service
public interface ProductImageService {

    String TYPE_SINGLE = "single";
    String TYPE_DETAIL = "detail";

    /**
     * 增加图片
     *
     * @param bean 图片实体
     */
    void add(ProductImage bean);

    /**
     * 删除图片
     *
     * @param id 图片的id
     */
    void delete(int id);

    /**
     * 获得图片
     *
     * @param id 图片的id
     * @return 返回图片实体
     */
    ProductImage get(int id);

    /**
     * 查询singeImage图片
     *
     * @param product 产品
     * @return 返回所有查询的singeImage集合
     */
    List<ProductImage> listSingleProductImages(Product product);

    /**
     * 查询detailImage图片
     *
     * @param product 产品
     * @return 返回所有查询的detailImages
     */
    List<ProductImage> listDetailProductImages(Product product);

    /**
     * 为产品设置显示图片
     *
     * @param product 产品
     */
    void setFirstProductImage(Product product);

    /**
     * 为多个产品设置多个图片
     *
     * @param products 多个产品
     */
    void setFirstProductImages(List<Product> products);

    /**
     * 为购物车中的产品设图片
     *
     * @param orderItems 购物车
     */
    void setFirstProductImagesOnOrderItems(List<OrderItem> orderItems);
}
