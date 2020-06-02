package com.qqlin.tmall;

import com.qqlin.tmall.util.PortUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author qingq
 */
@SpringBootApplication
@EnableCaching
@EnableElasticsearchRepositories(basePackages = "com.qqlin.tmall.dao.elasticsearch")
@EnableJpaRepositories(basePackages = {"com.qqlin.tmall.dao.repository", "com.qqlin.tmall.dao.entity"})
public class Application {
    static {
        PortUtil.checkPort(6379, "Redis 服务端", true);
        PortUtil.checkPort(9300, "ElasticSearch 服务器", true);
        PortUtil.checkPort(5601, "Kibana 工具", true);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
