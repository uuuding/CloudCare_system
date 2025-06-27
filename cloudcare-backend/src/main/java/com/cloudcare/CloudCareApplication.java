package com.cloudcare;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CloudCare云护智慧医养大数据公共服务平台启动类
 *
 * @author cloudcare
 */
@SpringBootApplication
@MapperScan("com.cloudcare.mapper")
public class CloudCareApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudCareApplication.class, args);
    }

}