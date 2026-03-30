package com.qqlin.tmall.web.admin;

import com.qqlin.tmall.dao.entity.*;
import com.qqlin.tmall.service.*;
import com.qqlin.tmall.util.Page4Navigator;
import com.qqlin.tmall.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Admin page data REST API controller
 * Provides JSON data for Vue.js admin frontend
 *
 * @author qqlin
 */
@RestController
@RequestMapping("/api/admin")
public class AdminPageRestController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private PropertyValueService propertyValueService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private UserService userService;

    /**
     * Admin dashboard data
     */
    @GetMapping("/dashboard")
    public Object dashboard() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageTitle", "管理后台");
        return Result.success(map);
    }

    /**
     * Category list page data
     */
    @GetMapping("/categoryList")
    public Object categoryList(@RequestParam(value = "start", defaultValue = "0") int start,
                               @RequestParam(value = "size", defaultValue = "5") int size) {
        start = start < 0 ? 0 : start;
        Page4Navigator<Category> page = categoryService.list(start, size, 5);
        return Result.success(page);
    }

    /**
     * Category edit page data
     */
    @GetMapping("/categoryEdit")
    public Object categoryEdit(@RequestParam(value = "id", defaultValue = "0") int id) {
        Category category = null;
        if (id > 0) {
            category = categoryService.get(id);
        } else {
            category = new Category();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("category", category);
        map.put("pageTitle", id > 0 ? "编辑分类" : "新增分类");
        return Result.success(map);
    }

    /**
     * Product list page data
     */
    @GetMapping("/productList")
    public Object productList(@RequestParam(value = "cid", defaultValue = "0") int cid,
                              @RequestParam(value = "start", defaultValue = "0") int start,
                              @RequestParam(value = "size", defaultValue = "5") int size) {
        start = start < 0 ? 0 : start;
        Page4Navigator<Product> page = productService.list(cid, start, size, 5);
        productImageService.setFirstProductImages(page.getContent());
        return Result.success(page);
    }

    /**
     * Product edit page data
     */
    @GetMapping("/productEdit")
    public Object productEdit(@RequestParam(value = "id", defaultValue = "0") int id) {
        Product product = null;
        if (id > 0) {
            product = productService.get(id);
        } else {
            product = new Product();
            product.setId(0);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("product", product);
        map.put("pageTitle", id > 0 ? "编辑产品" : "新增产品");
        return Result.success(map);
    }

    /**
     * Product image list page data
     */
    @GetMapping("/productImageList")
    public Object productImageList(@RequestParam("pid") int pid,
                                   @RequestParam("type") String type) {
        Product product = productService.get(pid);
        if (ProductImageService.TYPE_SINGLE.equals(type)) {
            return Result.success(productImageService.listSingleProductImages(product));
        } else if (ProductImageService.TYPE_DETAIL.equals(type)) {
            return Result.success(productImageService.listDetailProductImages(product));
        }
        return Result.success(null);
    }

    /**
     * Property list page data
     */
    @GetMapping("/propertyList")
    public Object propertyList(@RequestParam("cid") int cid,
                               @RequestParam(value = "start", defaultValue = "0") int start,
                               @RequestParam(value = "size", defaultValue = "5") int size) {
        start = start < 0 ? 0 : start;
        Page4Navigator<Property> page = propertyService.list(cid, start, size, 5);
        return Result.success(page);
    }

    /**
     * Property edit page data
     */
    @GetMapping("/propertyEdit")
    public Object propertyEdit(@RequestParam(value = "id", defaultValue = "0") int id,
                               @RequestParam(value = "cid", defaultValue = "0") int cid) {
        Property property = null;
        if (id > 0) {
            property = propertyService.get(id);
        } else {
            property = new Property();
            property.setCategory(categoryService.get(cid));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("property", property);
        map.put("pageTitle", id > 0 ? "编辑属性" : "新增属性");
        return Result.success(map);
    }

    /**
     * Property value edit page data
     */
    @GetMapping("/propertyValueEdit")
    public Object propertyValueEdit(@RequestParam("pid") int pid) throws Exception {
        Product product = productService.get(pid);
        propertyValueService.init(product);
        return Result.success(propertyValueService.list(product));
    }

    /**
     * Order list page data
     */
    @GetMapping("/orderList")
    public Object orderList(@RequestParam(value = "start", defaultValue = "0") int start,
                            @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start < 0 ? 0 : start;
        Page4Navigator<Order> page = orderService.list(start, size, 5);
        orderItemService.fill(page.getContent());
        orderService.removeOrderFromOrderItem(page.getContent());
        return Result.success(page);
    }

    /**
     * User list page data
     */
    @GetMapping("/userList")
    public Object userList(@RequestParam(value = "start", defaultValue = "0") int start,
                           @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start < 0 ? 0 : start;
        Page4Navigator<User> page = userService.list(start, size, 5);
        return Result.success(page);
    }
}
