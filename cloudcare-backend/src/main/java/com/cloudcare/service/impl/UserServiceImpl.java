package com.cloudcare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudcare.entity.User;
import com.cloudcare.mapper.UserMapper;
import com.cloudcare.service.UserService;
import com.cloudcare.utils.JwtUtil;
import com.cloudcare.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户服务实现类
 *
 * @author cloudcare
 */
@Slf4j
@Service
@Lazy
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Value("${cloudcare.default-password:123456}")
    private String defaultPassword;

    @Override
    public String login(String username, String password) {
        // 进行身份验证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        
        // 验证成功后，更新登录信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = getUserByUsername(username);
        if (user != null) {
            // 更新登录时间和IP
            user.setLoginTime(LocalDateTime.now());
            user.setLoginIp(SecurityUtil.getIpAddress());
            updateById(user);
            
            // 生成JWT令牌
            return jwtUtil.generateToken(user);
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean register(User user) {
        // 检查用户名是否已存在
        User existUser = getUserByUsername(user.getUsername());
        if (existUser != null) {
            return false;
        }
        
        // 设置默认值
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(1); // 正常状态
        user.setCreateTime(LocalDateTime.now());
        user.setCreateBy("system");
        user.setDeleted(0); // 未删除
        
        return save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        if (user == null) {
            return false;
        }
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(LocalDateTime.now());
        user.setUpdateBy(SecurityUtil.getCurrentUsername());
        
        return updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String resetPassword(Long userId) {
        User user = getById(userId);
        if (user == null) {
            return null;
        }
        
        // 重置为默认密码
        user.setPassword(passwordEncoder.encode(defaultPassword));
        user.setUpdateTime(LocalDateTime.now());
        user.setUpdateBy(SecurityUtil.getCurrentUsername());
        
        if (updateById(user)) {
            return defaultPassword;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long userId, Integer status) {
        User user = getById(userId);
        if (user == null) {
            return false;
        }
        
        user.setStatus(status);
        user.setUpdateTime(LocalDateTime.now());
        user.setUpdateBy(SecurityUtil.getCurrentUsername());
        
        return updateById(user);
    }

    @Override
    public Page<User> getUserPage(Page<User> page, String username, String phone, Integer status, Integer userType) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(username), User::getUsername, username)
                .like(StringUtils.isNotBlank(phone), User::getPhone, phone)
                .eq(status != null, User::getStatus, status)
                .eq(userType != null, User::getUserType, userType)
                .eq(User::getDeleted, 0)
                .orderByDesc(User::getCreateTime);
        
        return page(page, wrapper);
    }

    @Override
    public User getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        if (StringUtils.isBlank(username)) {
            return null;
        }
        return getUserByUsername(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserInfo(User user) {
        User existUser = getById(user.getUserId());
        if (existUser == null) {
            return false;
        }
        
        // 只允许更新部分字段
        existUser.setRealName(user.getRealName());
        existUser.setPhone(user.getPhone());
        existUser.setEmail(user.getEmail());
        existUser.setGender(user.getGender());
        existUser.setRemark(user.getRemark());
        existUser.setUpdateTime(LocalDateTime.now());
        existUser.setUpdateBy(SecurityUtil.getCurrentUsername());
        
        return updateById(existUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAvatar(Long userId, String avatar) {
        User user = getById(userId);
        if (user == null) {
            return false;
        }
        
        user.setAvatar(avatar);
        user.setUpdateTime(LocalDateTime.now());
        user.setUpdateBy(SecurityUtil.getCurrentUsername());
        
        return updateById(user);
    }
}