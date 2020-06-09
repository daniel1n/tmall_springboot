package com.qqlin.tmall.web.fore;

import com.qqlin.tmall.comparator.*;
import com.qqlin.tmall.dao.entity.*;
import com.qqlin.tmall.service.*;
import com.qqlin.tmall.util.Result;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author qingq
 */
@RestController
public class ForeRestController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private PropertyValueService propertyValueService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private OrderService orderService;

    /**
     * 提供主页显示
     * 将分类信息填充到分类中
     *
     * @return 已填充的分类
     */
    @GetMapping("/forehome")
    public Object home() {
        List<Category> categories = categoryService.list();
        productService.fill(categories);
        productService.fillByRow(categories);
        categoryService.removeCategoryFromProduct(categories);

        return categories;
    }

    /**
     * 注册用户功能
     * 添加了Shiro的加密支持
     *
     * @param user 用户
     * @return result success
     */
    @PostMapping("/foreregister")
    public Object register(@RequestBody User user) {
        String name = user.getName();
        String password = user.getPassword();
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        boolean exist = userService.isExist(name);

        if (exist) {
            String message = "用户名已经被使用,不能使用";
            return Result.fail(message);
        }

        /**
         * Shiro加密支持
         */
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String algorithmName = "md5";

        String encodedPassword = new SimpleHash(algorithmName, password, salt, times).toString();

        user.setSalt(salt);
        user.setPassword(encodedPassword);
//        user.setPassword(password);

        userService.add(user);

        return Result.success();
    }

    /**
     * 登录模块
     * 登录时，也需要Shiro的方式来进行校验
     *
     * @param userParam 用户
     * @param session   会话
     * @return 返回登录的结果
     */
    @PostMapping("/forelogin")
    public Object login(@RequestBody User userParam, HttpSession session) {
        String name = userParam.getName();
        name = HtmlUtils.htmlEscape(name);

        /*
        User user = userService.get(name, userParam.getPassword());
        if (null == user) {
            String message = "账号密码错误";
            return Result.fail(message);
        } else {
            session.setAttribute("user", user);
            return Result.success();
        }
         */

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, userParam.getPassword());
        try {
            subject.login(token);
            User user = userService.getByName(name);
            session.setAttribute("user", user);
            return Result.success();
        } catch (AuthenticationException e) {
            e.printStackTrace();
            String message = "账号密码错误";
            return Result.fail(message);
        }
    }

    /**
     * 产品显示的详情页面
     *
     * @param pid 产品的id
     * @return 填充产品信息，并返回result.success
     */
    @GetMapping("/foreproduct/{pid}")
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
     * 在产品页面点击立即购买或者加入购物车的时候，
     * 需要判断是否登录，同样改为Shiro 方式
     *
     * @param session 会话
     * @return 返回result.success或者result.fail
     */
    @GetMapping("/forecheckLogin")
    public Object checkLogin(HttpSession session) {
//        User user = (User) session.getAttribute("user");
//        if (null != user) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return Result.success();
        }
        return Result.fail("未登录");
    }

    /**
     * 分类的详情显示页
     *
     * @param cid  分类的id
     * @param sort 排序
     * @return 返回填充了产品，并排序的分类
     */
    @GetMapping("/forecategory/{cid}")
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
        return category;
    }

    /**
     * 搜索页面的功能
     * 使用ElasticSearch实现
     *
     * @param keyword 关键字
     * @return 返回多个产品
     */
    @PostMapping("foresearch")
    public Object search(String keyword) {
        if (null == keyword) {
            keyword = "";
        }
        List<Product> products = productService.search(keyword, 0, 20);
        productImageService.setFirstProductImages(products);
        productService.setSaleAndReviewNumber(products);
        return products;
    }

    /**
     * 立即购买
     *
     * @param pid     产品id
     * @param num     购买的数量
     * @param session 会话
     * @return 返回已购买或者添加到购物车
     */
    @GetMapping("/forebuyone")
    public Object buyOne(int pid, int num, HttpSession session) {
        return buyOneAndAddCart(pid, num, session);
    }

    private int buyOneAndAddCart(int pid, int num, HttpSession session) {
        Product product = productService.get(pid);
        int orderItemId = 0;

        User user = (User) session.getAttribute("user");
        boolean found = false;

        List<OrderItem> orderItems = orderItemService.listByUser(user);
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getProduct().getId() == product.getId()) {
                orderItem.setNumber(orderItem.getNumber() + num);
                orderItemService.update(orderItem);
                found = true;
                orderItemId = orderItem.getId();
                break;
            }
        }

        if (!found) {
            OrderItem orderItem = new OrderItem();
            orderItem.setUser(user);
            orderItem.setProduct(product);
            orderItem.setNumber(num);
            orderItemService.add(orderItem);
            orderItemId = orderItem.getId();
        }

        return orderItemId;
    }

    /**
     * 结算页面
     *
     * @param oiid    订单项的id
     * @param session 会话
     * @return 返回result.success
     */
    @GetMapping("forebuy")
    public Object buy(String[] oiid, HttpSession session) {
        List<OrderItem> orderItems = new ArrayList<>();
        float total = 0;

        for (String strid : oiid) {
            int id = Integer.parseInt(strid);
            OrderItem oi = orderItemService.get(id);
            total += oi.getProduct().getPromotePrice() * oi.getNumber();
            orderItems.add(oi);
        }

        productImageService.setFirstProductImagesOnOrderItems(orderItems);

        session.setAttribute("ois", orderItems);

        Map<String, Object> map = new HashMap<>();
        map.put("orderItems", orderItems);
        map.put("total", total);
        return Result.success(map);
    }

    /**
     * 加入购物车
     *
     * @param pid     产品的id
     * @param num     产品的数量
     * @param session 会话
     * @return 返回添加成功
     */
    @GetMapping("foreaddCart")
    public Object addCart(int pid, int num, HttpSession session) {
        buyOneAndAddCart(pid, num, session);
        return Result.success();
    }

    /**
     * 购物车页面
     *
     * @param session 会话
     * @return 返回购物车的订单项
     */
    @GetMapping("forecart")
    public Object cart(HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<OrderItem> ois = orderItemService.listByUser(user);
        productImageService.setFirstProductImagesOnOrderItems(ois);
        return ois;
    }

    /**
     * 调整购物车中的订单数量
     *
     * @param session 会话
     * @param pid     产品id
     * @param num     产品数量
     * @return 返回result.success
     */
    @GetMapping("forechangeOrderItem")
    public Object changeOrderItem(HttpSession session, int pid, int num) {
        User user = (User) session.getAttribute("user");
        if (null == user) {
            return Result.fail("未登录");
        }

        List<OrderItem> ois = orderItemService.listByUser(user);
        for (OrderItem oi : ois) {
            if (oi.getProduct().getId() == pid) {
                oi.setNumber(num);
                orderItemService.update(oi);
                break;
            }
        }
        return Result.success();
    }

    /**
     * 删除购物车的订单项
     *
     * @param session 会话
     * @param oiid    订单项的id
     * @return 返回删除成功
     */
    @GetMapping("foredeleteOrderItem")
    public Object deleteOrderItem(HttpSession session, int oiid) {
        User user = (User) session.getAttribute("user");
        if (null == user) {
            return Result.fail("未登录");
        }
        orderItemService.delete(oiid);
        return Result.success();
    }

    /**
     * 生成订单
     *
     * @param order   订单
     * @param session 会话
     * @return 订单生成成功
     */
    @PostMapping("forecreateOrder")
    public Object createOrder(@RequestBody Order order, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (null == user) {
            return Result.fail("未登录");
        }

        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(10000);
        order.setOrderCode(orderCode);
        order.setCreateDate(new Date());
        order.setUser(user);
        order.setStatus(OrderService.WAIT_PAY);
        List<OrderItem> ois = (List<OrderItem>) session.getAttribute("ois");

        float total = orderService.add(order, ois);

        Map<String, Object> map = new HashMap<>();
        map.put("oid", order.getId());
        map.put("total", total);

        return Result.success(map);
    }

    /**
     * 支付页面
     *
     * @param oid 订单项的id
     * @return 返回已支付的订单
     */
    @GetMapping("forepayed")
    public Object payed(int oid) {
        Order order = orderService.get(oid);
        order.setStatus(OrderService.WAIT_DELIVERY);
        order.setPayDate(new Date());
        orderService.update(order);
        return order;
    }

    /**
     * 我的订单页面
     *
     * @param session 会话
     * @return 返回我的订单
     */
    @GetMapping("forebought")
    public Object bought(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (null == user) {
            return Result.fail("未登录");
        }
        List<Order> os = orderService.listByUserWithoutDelete(user);
        orderService.removeOrderFromOrderItem(os);
        return os;

    }

    /**
     * 确认收货的页面
     *
     * @param oid 订单的id
     * @return 返回订单对象
     */
    @GetMapping("foreconfirmPay")
    public Object confirmPay(int oid) {
        Order order = orderService.get(oid);
        orderItemService.fill(order);
        orderService.calculation(order);
        orderService.removeOrderFromOrderItem(order);
        return order;
    }

    /**
     * 确认收货后的页面
     *
     * @param oid 订单的id
     * @return 返回操作成功
     */
    @GetMapping("foreorderConfirmed")
    public Object orderConfirmed(int oid) {
        Order order = orderService.get(oid);
        order.setStatus(OrderService.WAIT_REVIEW);
        order.setConfirmDate(new Date());
        orderService.update(order);
        return Result.success();
    }

    /**
     * 删除订单
     *
     * @param oid 订单的id
     * @return 操作成功
     */
    @PutMapping("/foredeleteOrder")
    public Object deleteOrder(int oid) {
        Order order = orderService.get(oid);
        order.setStatus(OrderService.DELETE);
        orderService.update(order);
        return Result.success();
    }

    /**
     * 填写评论
     *
     * @param oid 订单的id
     * @return 填写成功
     */
    @GetMapping("forereview")
    public Object review(int oid) {
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
     * 订单提交
     *
     * @param session 会话
     * @param oid     订单id
     * @param pid     产品id
     * @param content 评论正文
     * @return 返回提交成功
     */
    @PostMapping("foredoreview")
    public Object doreview(HttpSession session, int oid, int pid, String content) {
        Order order = orderService.get(oid);
        order.setStatus(OrderService.FINISH);
        orderService.update(order);

        Product product = productService.get(pid);
        content = HtmlUtils.htmlEscape(content);

        User user = (User) session.getAttribute("user");
        Review review = new Review();
        review.setContent(content);
        review.setProduct(product);
        review.setCreateDate(new Date());
        review.setUser(user);
        reviewService.add(review);
        return Result.success();
    }
}


