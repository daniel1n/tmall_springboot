package com.qqlin.tmall.service.impl;

import com.qqlin.tmall.dao.elasticsearch.ProductElasticSearchDAO;
import com.qqlin.tmall.dao.entity.Category;
import com.qqlin.tmall.dao.entity.Product;
import com.qqlin.tmall.dao.repository.ProductDAO;
import com.qqlin.tmall.service.*;
import com.qqlin.tmall.util.Page4Navigator;
import com.qqlin.tmall.util.SpringContextUtil;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qqlin
 * @since 2020-6-1
 */
@Service
@CacheConfig(cacheNames = "products")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ProductElasticSearchDAO productElasticSearchDAO;

    @Override
    @CacheEvict(allEntries = true)
    public void add(Product bean) {
        productDAO.save(bean);
        productElasticSearchDAO.save(bean);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delete(int id) {
        productDAO.delete(id);
        productElasticSearchDAO.delete(id);
    }

    @Override
    @Cacheable(key = "'products-one-'+ #p0")
    public Product get(int id) {
        return productDAO.findOne(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void update(Product bean) {
        productDAO.save(bean);
        productElasticSearchDAO.save(bean);
    }

    @Override
    @Cacheable(key = "'products-cid-'+#p0+'-page-'+#p1 + '-' + #p2 ")
    public Page4Navigator<Product> list(int cid, int start, int size, int navigatePages) {
        Category category = categoryService.get(cid);
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Product> pageFromJPA = productDAO.findByCategory(category, pageable);
        return new Page4Navigator<>(pageFromJPA, navigatePages);
    }

    @Override
    public void fill(List<Category> categories) {
        for (Category category : categories) {
            fill(category);
        }
    }

    @Override
    public void fill(Category category) {
//        List<Product> products = listByCategory(category);
        ProductService productService = SpringContextUtil.getBean(ProductService.class);
        List<Product> products = productService.listByCategory(category);
        productImageService.setFirstProductImages(products);
        category.setProducts(products);
    }


    /**
     * 假设一个分类恰好对应40种产品，那么这40种产品本来是放在一个集合List里。
     * 可是，在页面上显示的时候，需要每8种产品，放在一列 为了显示的方便，
     * 我把这40种产品，按照每8种产品方在一个集合里的方式，拆分成了5个小的集合，
     * 这5个小的集合里的每个元素是8个产品。
     * 这样到了页面上，显示起来就很方便了。
     * 否则页面上的处理就会复杂不少。
     *
     * @param categories 多个分类
     */
    @Override
    public void fillByRow(List<Category> categories) {
        int productNumberEachRow = 8;
        for (Category category : categories) {
            List<Product> products = category.getProducts();
            List<List<Product>> productsByRow = new ArrayList<>();
            for (int i = 0; i < products.size(); i += productNumberEachRow) {
                int size = i + productNumberEachRow;
                size = size > products.size() ? products.size() : size;
                List<Product> productsOfEachRow = products.subList(i, size);
                productsByRow.add(productsOfEachRow);
            }
            category.setProductsByRow(productsByRow);
        }
    }

    @Override
    @Cacheable(key = "'products-cid-'+ #p0.id")
    public List<Product> listByCategory(Category category) {
        return productDAO.findByCategoryOrderById(category);
    }

    @Override
    public void setSaleAndReviewNumber(Product product) {
        int saleCount = orderItemService.getSaleCount(product);
        product.setSaleCount(saleCount);

        int reviewCount = reviewService.getCount(product);
        product.setReviewCount(reviewCount);

    }


    @Override
    public void setSaleAndReviewNumber(List<Product> products) {
        for (Product product : products) {
            setSaleAndReviewNumber(product);
        }
    }

    /*@Override
    public List<Product> search(String keyword, int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        List<Product> products = productDAO.findByNameLike("%" + keyword + "%", pageable);
        return products;
    }*/

    @Override
    public void initDatabase2ES() {
        Pageable pageable = new PageRequest(0, 5);
        Page<Product> page = productElasticSearchDAO.findAll(pageable);
        if (page.getContent().isEmpty()) {
            List<Product> products = productDAO.findAll();
            productElasticSearchDAO.save(products);
        }
    }

    @Override
    public List<Product> search(String keyword, int start, int size) {
        initDatabase2ES();
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
                // 关键字查询
                .add(QueryBuilders.matchPhrasePrefixQuery("name", keyword),
                        ScoreFunctionBuilders.weightFactorFunction(100))
                // 设置权重分 求和模式
                .scoreMode("sum")
                // 设置权重分最低分
                .setMinScore(10);
        // 设置分页
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(functionScoreQueryBuilder).build();

        Page<Product> page = productElasticSearchDAO.search(searchQuery);
        return page.getContent();
    }
}
