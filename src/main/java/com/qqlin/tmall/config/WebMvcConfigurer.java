package com.qqlin.tmall.config;

import com.qqlin.tmall.interceptor.LoginInterceptor;
import com.qqlin.tmall.interceptor.OrderInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 拦截器设置
 *
 * @author qqlin
 * @since 2020-6-1
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Bean
    public LoginInterceptor getLoginInterceptor() {
        return new LoginInterceptor();
    }

    @Bean
    public OrderInterceptor getOtherInterceptor() {
        return new OrderInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getLoginInterceptor())
                .addPathPatterns("/**");
        registry.addInterceptor(getOtherInterceptor())
                .addPathPatterns("/**");
    }
}
