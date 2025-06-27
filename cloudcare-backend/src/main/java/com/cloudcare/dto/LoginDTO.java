package com.cloudcare.dto;

import lombok.Data;

/**
 * 登录数据传输对象
 *
 * @author cloudcare
 */
@Data
public class LoginDTO {
    private String username;
    private String password;
}