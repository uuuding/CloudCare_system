package com.cloudcare.security;

import com.cloudcare.entity.User;
import com.cloudcare.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义用户详情服务
 *
 * @author cloudcare
 */
@Slf4j
@Service
@Lazy
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名获取用户信息
        User user = userService.getUserByUsername(username);
        if (user == null) {
            log.error("用户[{}]不存在", username);
            throw new UsernameNotFoundException("用户不存在");
        }

        // 创建权限列表
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // 根据用户类型添加角色
        switch (user.getUserType()) {
            case 1:
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                break;
            case 2:
                authorities.add(new SimpleGrantedAuthority("ROLE_DOCTOR"));
                break;
            case 3:
                authorities.add(new SimpleGrantedAuthority("ROLE_ELDER"));
                break;
            default:
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                break;
        }

        // 创建UserDetails对象
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getStatus() == 1, // 是否启用
                true, // 账号是否过期
                true, // 凭证是否过期
                true, // 账号是否锁定
                authorities // 权限列表
        );
    }
}