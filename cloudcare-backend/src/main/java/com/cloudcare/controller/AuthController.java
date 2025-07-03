package com.cloudcare.controller;

import com.cloudcare.common.Result;
import com.cloudcare.common.annotation.Log;
import com.cloudcare.common.enums.BusinessType;
import com.cloudcare.entity.User;
import com.cloudcare.service.UserService;
import com.cloudcare.dto.RegisterDTO;
import com.cloudcare.dto.LoginDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 *
 * @author cloudcare
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户认证相关接口")
public class AuthController {

    private final UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Log(title = "USER", businessType = BusinessType.OTHER, isSaveRequestData = false, isSaveResponseData = true)
    @Operation(summary = "用户登录", description = "用户登录接口")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        log.info("接收到登录请求: 用户名={}, 密码长度={}", username, password != null ? password.length() : 0);

        // 调用服务层登录方法
        String token = userService.login(username, password);
        if (token == null) {
            log.warn("登录失败: 用户名={}", username);
            return Result.error("用户名或密码错误");
        }

        // 获取用户信息
        User user = userService.getUserByUsername(username);
        log.info("登录成功: 用户名={}, 用户ID={}", username, user.getUserId());

        // 返回结果
        Map<String, Object> result = new HashMap<>(8);
        result.put("token", token);
        result.put("userId", user.getUserId());
        result.put("username", user.getUsername());
        result.put("realName", user.getRealName());
        result.put("avatar", user.getAvatar());
        result.put("userType", user.getUserType());
        result.put("status", user.getStatus());
        result.put("phone", user.getPhone());
        result.put("email", user.getEmail());

        return Result.success(result, "登录成功");
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Log(title = "USER", businessType = BusinessType.INSERT, isSaveRequestData = false, isSaveResponseData = true)
    @Operation(summary = "用户注册", description = "用户注册接口")
    public Result<Boolean> register(@RequestBody RegisterDTO registerDTO) {

        // 创建用户对象
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword());
        user.setRealName(registerDTO.getRealName());
        user.setPhone(registerDTO.getPhone());
        user.setUserType(registerDTO.getUserType());
        
        // 注册用户
        boolean result = userService.register(user);
        if (!result) {
            return Result.error("注册失败，用户名已存在");
        }
        
        return Result.success(true, "注册成功");
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    @Log(title = "USER", businessType = BusinessType.OTHER, isSaveResponseData = true)
    @Operation(summary = "退出登录", description = "用户退出登录接口")
    public Result<Boolean> logout() {
        return Result.success(true, "退出成功");
    }
}