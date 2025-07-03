package com.cloudcare.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudcare.common.Result;
import com.cloudcare.common.annotation.Log;
import com.cloudcare.common.enums.BusinessType;
import com.cloudcare.entity.User;
import com.cloudcare.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 *
 * @author cloudcare
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户管理相关接口")
public class UserController {

    private final UserService userService;

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取当前登录用户信息", description = "获取当前登录用户的详细信息")
    public Result<User> getCurrentUserInfo() {
        User user = userService.getCurrentUser();
        return Result.success(user);
    }

    /**
     * 更新用户基本信息
     */
    @PutMapping("/info")
    @Log(title = "USER", businessType = BusinessType.UPDATE)
    @Operation(summary = "更新用户基本信息", description = "更新当前登录用户的基本信息")
    public Result<Boolean> updateUserInfo(@RequestBody User user) {
        boolean result = userService.updateUserInfo(user);
        return Result.success(result);
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    @Log(title = "USER", businessType = BusinessType.UPDATE, isSaveRequestData = false)
    @Operation(summary = "修改密码", description = "修改当前登录用户的密码")
    public Result<Boolean> updatePassword(
            @Parameter(description = "旧密码") @RequestParam String oldPassword,
            @Parameter(description = "新密码") @RequestParam String newPassword) {
        User user = userService.getCurrentUser();
        boolean result = userService.updatePassword(user.getUserId(), oldPassword, newPassword);
        return Result.success(result);
    }

    /**
     * 分页查询用户列表
     */
    @GetMapping("/page")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "分页查询用户列表", description = "分页查询用户列表信息")
    public Result<Page<User>> getUserPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "手机号") @RequestParam(required = false) String phone,
            @Parameter(description = "状态（0：禁用，1：正常）") @RequestParam(required = false) Integer status,
            @Parameter(description = "用户类型（1：管理员，2：医生，3：老人）") @RequestParam(required = false) Integer userType) {
        Page<User> page = new Page<>(pageNum, pageSize);
        Page<User> userPage = userService.getUserPage(page, username, phone, status, userType);
        return Result.success(userPage);
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "获取用户详情", description = "根据用户ID获取用户详情信息")
    public Result<User> getUserInfo(@Parameter(description = "用户ID") @PathVariable Long userId) {
        User user = userService.getById(userId);
        return Result.success(user);
    }

    /**
     * 新增用户
     */
    @PostMapping
    @Log(title = "USER", businessType = BusinessType.INSERT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "新增用户", description = "新增用户信息")
    public Result<Boolean> addUser(@RequestBody User user) {
        boolean result = userService.register(user);
        return Result.success(result);
    }

    /**
     * 更新用户
     */
    @PutMapping
    @Log(title = "USER", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "更新用户", description = "更新用户信息")
    public Result<Boolean> updateUser(@RequestBody User user) {
        boolean result = userService.updateById(user);
        return Result.success(result);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{userId}")
    @Log(title = "USER", businessType = BusinessType.DELETE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "删除用户", description = "根据用户ID删除用户信息")
    public Result<Boolean> deleteUser(@Parameter(description = "用户ID") @PathVariable Long userId) {
        boolean result = userService.removeById(userId);
        return Result.success(result);
    }

    /**
     * 重置密码
     */
    @PutMapping("/reset-password/{userId}")
    @Log(title = "USER", businessType = BusinessType.UPDATE, isSaveResponseData = false)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "重置密码", description = "重置用户密码为默认密码")
    public Result<String> resetPassword(@Parameter(description = "用户ID") @PathVariable Long userId) {
        String password = userService.resetPassword(userId);
        return Result.success(password, "重置密码成功");
    }

    /**
     * 更新用户状态
     */
    @PutMapping("/status/{userId}/{status}")
    @Log(title = "USER", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "更新用户状态", description = "更新用户状态（0：禁用，1：正常）")
    public Result<Boolean> updateUserStatus(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "状态（0：禁用，1：正常）") @PathVariable Integer status) {
        boolean result = userService.updateStatus(userId, status);
        return Result.success(result);
    }

    /**
     * 获取医生列表
     */
    @GetMapping("/doctors")
    @Operation(summary = "获取医生列表", description = "获取所有医生用户列表")
    public Result<List<User>> getDoctorList() {
        List<User> doctors = userService.getUsersByType(2);
        return Result.success(doctors);
    }

    /**
     * 获取老人列表
     */
    @GetMapping("/elders")
    @Operation(summary = "获取老人列表", description = "获取所有老人用户列表")
    public Result<List<User>> getElderList() {
        List<User> elders = userService.getUsersByType(3);
        return Result.success(elders);
    }

    /**
     * 获取服务人员列表
     */
    @GetMapping("/staff")
    @Operation(summary = "获取服务人员列表", description = "获取所有服务人员用户列表")
    public Result<List<User>> getStaffList() {
        List<User> staff = userService.getUsersByType(4);
        return Result.success(staff);
    }

    /**
     * 更新用户头像
     */
    @PutMapping("/avatar")
    @Operation(summary = "更新用户头像", description = "更新当前用户的头像")
    public Result<Boolean> updateAvatar(@RequestBody Map<String, String> request) {
        String avatar = request.get("avatar");
        if (avatar == null || avatar.trim().isEmpty()) {
            return Result.error("头像URL不能为空");
        }

        // 获取当前登录用户ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.getUserByUsername(username);

        if (currentUser == null) {
            return Result.error("用户不存在");
        }

        boolean result = userService.updateAvatar(currentUser.getUserId(), avatar);
        return Result.success(result, "头像更新成功");
    }
}