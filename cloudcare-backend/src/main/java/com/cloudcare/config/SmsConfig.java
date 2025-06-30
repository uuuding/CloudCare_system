package com.cloudcare.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 短信配置类
 * 用于配置短信宝相关参数
 */
@Data
@Component
@ConfigurationProperties(prefix = "sms")
public class SmsConfig {
    
    /**
     * 短信宝用户名
     */
    private String username;
    
    /**
     * 短信宝密码（MD5加密后的值）
     */
    private String password;
    
    /**
     * 短信宝API地址
     */
    private String apiUrl = "http://api.smsbao.com/sms";
    
    /**
     * 查询余额API地址
     */
    private String queryUrl = "http://api.smsbao.com/query";
    
    /**
     * 是否启用短信服务
     */
    private boolean enabled = true;
    
    /**
     * 短信签名
     */
    private String signature = "【云护CloudCare平台】";
}