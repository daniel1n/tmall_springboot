package com.qqlin.tmall.web.fore;

import com.qqlin.tmall.comparator.*;
import com.qqlin.tmall.dao.entity.*;
import com.qqlin.tmall.service.*;
import com.qqlin.tmall.util.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Frontend page data REST API controller
 * Provides JSON data for Vue.js frontend
 *
 * @author qqlin
 */
@RestController
@RequestMapping("/api/fore")
public class ForePageRestController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private PropertyValueService propertyValueService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;

    /**
     * Home page data
     * Returns categories with products filled
     */
    @GetMapping("/home")
    public Object home() {
        List<Category> categories = categoryService.list();
        productService.fill(categories);
        productService.fillByRow(categories);
        categoryService.removeCategoryFromProduct(categories);
        return Result.success(categories);
    }

    /**
     * Register page data
     */
    @GetMapping("/registerPage")
    public Object registerPage() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageTitle", "用户注册");
        return Result.success(map);
    }

    /**
     * Login page data
     */
    @GetMapping("/loginPage")
    public Object loginPage() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageTitle", "用户登录");
        return Result.success(map);
    }

    /**
     * Product detail page data
     */
    @GetMapping("/product/{pid}")
    public Object product(@PathVariable("pid") int pid) {
        Product product = productService.get(pid);

        List<ProductImage> productSingleImages = productImageService.listSingleProductImages(product);
        List<ProductImage> productDetailImages = productImageService.listDetailProductImages(product);
        product.setProductSingleImages(productSingleImages);
        product.setProductDetailImages(productDetailImages);

        List<PropertyValue> propertyValues = propertyValueService.list(product);
        List<Review> reviews = reviewService.list(product);
        productService.setSaleAndReviewNumber(product);
        productImageService.setFirstProductImage(product);

        Map<String, Object> map = new HashMap<>();
        map.put("product", product);
        map.put("propertyValues", propertyValues);
        map.put("reviews", reviews);
        return Result.success(map);
    }

    /**
     * Category page data with optional sorting
     */
    @GetMapping("/category/{cid}")
    public Object category(@PathVariable("cid") int cid, String sort) {
        Category category = categoryService.get(cid);
        productService.fill(category);
        productService.setSaleAndReviewNumber(category.getProducts());
        categoryService.removeCategoryFromProduct(category);

        if (null != sort) {
            switch (sort) {
                case "review":
                    Collections.sort(category.getProducts(), new ProductReviewComparator());
                    break;
                case "date":
                    Collections.sort(category.getProducts(), new ProductDateComparator());
                    break;
                case "saleCount":
                    Collections.sort(category.getProducts(), new ProductSaleCountComparator());
                    break;
                case "price":
                    Collections.sort(category.getProducts(), new ProductPriceComparator());
                    break;
                case "all":
                    Collections.sort(category.getProducts(), new ProductAllComparator());
                    break;
                default:
            }
        }
        return Result.success(category);
    }

    /**
     * Cart page data
     */
    @GetMapping("/cart")
    public Object cart(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (null == user) {
            return Result.fail("未登录");
        }
        List<OrderItem> ois = orderItemService.listByUser(user);
        productImageService.setFirstProductImagesOnOrderItems(ois);
        return Result.success(ois);
    }

    /**
     * Buy page (checkout) data
     */
    @GetMapping("/buy")
    public Object buy(@RequestParam("oiid") String[] oiid, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (null == user) {
            return Result.fail("未登录");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        float total = 0;

        for (String strid : oiid) {
            int id = Integer.parseInt(strid);
            OrderItem oi = orderItemService.get(id);
            total += oi.getProduct().getPromotePrice() * oi.getNumber();
            orderItems.add(oi);
        }

        productImageService.setFirstProductImagesOnOrderItems(orderItems);

        Map<String, Object> map = new HashMap<>();
        map.put("orderItems", orderItems);
        map.put("total", total);
        return Result.success(map);
    }

    /**
     * Alipay page data
     */
    @GetMapping("/alipayPage")
    public Object alipayPage(int oid, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (null == user) {
            return Result.fail("未登录");
        }
        Order order = orderService.get(oid);
        orderItemService.fill(order);
        orderService.removeOrderFromOrderItem(order);
        return Result.success(order);
    }

    /**
     * My orders page data
     */
    @GetMapping("/bought")
    public Object bought(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (null == user) {
            return Result.fail("未登录");
        }
        List<Order> os = orderService.listByUserWithoutDelete(user);
        orderService.removeOrderFromOrderItem(os);
        return Result.success(os);
    }

    /**
     * Search page data
     */
    @GetMapping("/search")
    public Object search(String keyword) {
        if (null == keyword) {
            keyword = "";
        }
        List<Product> products = productService.search(keyword, 0, 20);
        productImageService.setFirstProductImages(products);
        productService.setSaleAndReviewNumber(products);
        return Result.success(products);
    }

    /**
     * Confirm pay page data
     */
    @GetMapping("/confirmPay")
    public Object confirmPayPage(int oid, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (null == user) {
            return Result.fail("未登录");
        }
        Order order = orderService.get(oid);
        orderItemService.fill(order);
        orderService.calculation(order);
        orderService.removeOrderFromOrderItem(order);
        return Result.success(order);
    }

    /**
     * Order confirmed page data
     */
    @GetMapping("/orderConfirmed")
    public Object orderConfirmedPage(int oid, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (null == user) {
            return Result.fail("未登录");
        }
        Order order = orderService.get(oid);
        order.setStatus(OrderService.WAIT_REVIEW);
        order.setConfirmDate(new Date());
        orderService.update(order);
        return Result.success(order);
    }

    /**
     * Review page data
     */
    @GetMapping("/review")
    public Object reviewPage(int oid, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (null == user) {
            return Result.fail("未登录");
        }
        Order order = orderService.get(oid);
        orderItemService.fill(order);
        orderService.removeOrderFromOrderItem(order);
        Product product = order.getOrderItems().get(0).getProduct();
        List<Review> reviews = reviewService.list(product);
        productService.setSaleAndReviewNumber(product);

        Map<String, Object> map = new HashMap<>();
        map.put("p", product);
        map.put("o", order);
        map.put("reviews", reviews);
        return Result.success(map);
    }

    /**
     * Check if user is logged in
     */
    @GetMapping("/checkLogin")
    public Object checkLogin(HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            User user = (User) session.getAttribute("user");
            return Result.success(user);
        }
        return Result.fail("未登录");
    }

    /**
     * Get current user info
     */
    @GetMapping("/userInfo")
    public Object userInfo(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (null == user) {
            return Result.fail("未登录");
        }
        return Result.success(user);
    }
}
