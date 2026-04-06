package com.qqlin.tmall.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 拦截器判断是否登录，改用了Shiro方式
 *
 * @author qqlin
 * @since 2020-6-1
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HttpSession session = httpServletRequest.getSession();
        String contextPath = session.getServletContext().getContextPath();
        String[] requiredAuthPages = new String[]{
                "buy",
                "alipay",
                "payed",
                "cart",
                "bought",
                "confirmPay",
                "orderConfirmed",

                "forebuyone",
                "forebuy",
                "foreaddCart",
                "forecart",
                "forechangeOrderItem",
                "foredeleteOrderItem",
                "forecreateOrder",
                "forepayed",
                "forebought",
                "foreconfirmPay",
                "foreorderConfirmed",
                "foredeleteOrder",
                "forereview",
                "foredoreview",

                // API paths (new frontend-backend separation)
                "api/fore/buy",
                "api/fore/alipayPage",
                "api/fore/payed",
                "api/fore/cart",
                "api/fore/bought",
                "api/fore/confirmPay",
                "api/fore/orderConfirmed",
                "api/fore/checkLogin",
                "api/fore/userInfo"
        };

        String uri = httpServletRequest.getRequestURI();
        uri = StringUtils.remove(uri, contextPath + "/");
        String page = uri;

        if (beginWith(page, requiredAuthPages)) {
            /**
             * 修改为subject
             */
//            User user = (User) session.getAttribute("user");
            Subject subject = SecurityUtils.getSubject();

            if (!subject.isAuthenticated()) {
//            if (user == null) {
                // For API paths, return 401 with JSON response
                if (page.startsWith("api/")) {
                    httpServletResponse.setStatus(401);
                    httpServletResponse.setContentType("application/json;charset=UTF-8");
                    httpServletResponse.getWriter().write("{\"success\":false,\"message\":\"未登录\"}");
                } else {
                    httpServletResponse.sendRedirect("login");
                }
                return false;
            }
        }
        return true;

    }

    private boolean beginWith(String page, String[] requiredAuthPages) {
        boolean result = false;
        for (String requiredAuthPage : requiredAuthPages) {
            if (StringUtils.startsWith(page, requiredAuthPage)) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
