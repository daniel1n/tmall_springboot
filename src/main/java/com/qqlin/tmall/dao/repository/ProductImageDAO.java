package com.qqlin.tmall.dao.repository;

import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author qqlin
 */
public interface ProductImageDAO extends JpaRepository<ProductImage, Integer> {

    /**
     * 根据产品以及产品图片对应的类型，获得产品图片
     *
     * @param product 产品
     * @param type    产品图片的类型
     * @return 返回所查询的图片集合
     */
    List<ProductImage> findByProductAndTypeOrderByIdDesc(Product product, String type);

}
