package com.cloudcare.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 系统配置类
 * 用于配置系统管理员相关参数
 */
@Data
@Component
@ConfigurationProperties(prefix = "system")
public class SystemConfig {
    
    /**
     * 管理员配置
     */
    private Admin admin = new Admin();
    
    @Data
    public static class Admin {
        /**
         * 管理员电话
         */
        private String phone;
        
        /**
         * 管理员邮箱
         */
        private String email;
    }
}