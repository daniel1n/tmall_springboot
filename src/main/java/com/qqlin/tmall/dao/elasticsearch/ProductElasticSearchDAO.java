package com.qqlin.tmall.dao.elasticsearch;

import com.qqlin.tmall.dao.entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 用于链接 ElasticSearch 的DAO
 *
 * @author qqlin
 * @since 2020-6-2
 */
public interface ProductElasticSearchDAO extends ElasticsearchRepository<Product, Integer> {

}
