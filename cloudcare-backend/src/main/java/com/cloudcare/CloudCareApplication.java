package com.cloudcare;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

/**
 * CloudCare云护智慧医养大数据公共服务平台启动类
 *
 * @author cloudcare
 */
@SpringBootApplication
@MapperScan("com.cloudcare.mapper")
@EnableNeo4jRepositories("com.cloudcare.repository")
public class CloudCareApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudCareApplication.class, args);
    }

}