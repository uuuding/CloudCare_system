package com.cloudcare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudcare.entity.User;

import java.util.List;

/**
 * 用户服务接口
 *
 * @author cloudcare
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回token，失败返回null
     */
    String login(String username, String password);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 注册用户
     *
     * @param user 用户信息
     * @return 注册成功返回true，失败返回false
     */
    boolean register(User user);

    /**
     * 修改密码
     *
     * @param userId      用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改成功返回true，失败返回false
     */
    boolean updatePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 重置密码
     *
     * @param userId 用户ID
     * @return 重置后的密码
     */
    String resetPassword(Long userId);

    /**
     * 更新用户状态
     *
     * @param userId 用户ID
     * @param status 状态（0：禁用，1：正常）
     * @return 更新成功返回true，失败返回false
     */
    boolean updateStatus(Long userId, Integer status);

    /**
     * 分页查询用户列表
     *
     * @param page     分页参数
     * @param username 用户名
     * @param phone    手机号
     * @param status   状态
     * @param userType 用户类型
     * @return 用户分页列表
     */
    Page<User> getUserPage(Page<User> page, String username, String phone, Integer status, Integer userType);

    /**
     * 获取当前登录用户信息
     *
     * @return 当前登录用户信息
     */
    User getCurrentUser();

    /**
     * 更新用户基本信息
     *
     * @param user 用户信息
     * @return 更新成功返回true，失败返回false
     */
    boolean updateUserInfo(User user);

    /**
     * 更新用户头像
     *
     * @param userId 用户ID
     * @param avatar 头像URL
     * @return 更新成功返回true，失败返回false
     */
    boolean updateAvatar(Long userId, String avatar);

    /**
     * 根据用户类型获取用户列表
     *
     * @param userType 用户类型（1：管理员，2：医生，3：老人）
     * @return 用户列表
     */
    List<User> getUsersByType(Integer userType);

}