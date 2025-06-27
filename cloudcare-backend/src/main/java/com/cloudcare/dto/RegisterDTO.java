package com.cloudcare.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 注册请求DTO
 *
 * @author cloudcare
 */
@Data
@Schema(description = "注册请求DTO")
public class RegisterDTO {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "用户类型（1：普通，2：医生，3：老人）")
    private int userType;
}