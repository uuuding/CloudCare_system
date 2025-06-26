package com.cloudcare.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * JWT认证提供者
 *
 * @author cloudcare
 */
@Slf4j
@Component
@Lazy
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取用户名和密码
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        // 根据用户名获取用户详情
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 检查用户状态
        if (!userDetails.isEnabled()) {
            throw new DisabledException("用户已被禁用");
        }

        // 验证密码
        try {
            log.debug("开始密码验证: 用户名={}, 输入密码={}, 数据库密码={}", 
                    username, password, userDetails.getPassword());
            
            boolean passwordMatches = passwordEncoder.matches(password, userDetails.getPassword());
            log.info("密码验证结果: {}, 用户名: {}", passwordMatches, username);
            
            if (!passwordMatches) {
                log.warn("密码不匹配: 用户名={}, 输入密码长度={}, 数据库密码长度={}", 
                        username, password.length(), userDetails.getPassword().length());
                throw new BadCredentialsException("用户名或密码错误");
            }
        } catch (Exception e) {
            log.error("密码验证异常: 用户名={}, 异常信息={}", username, e.getMessage(), e);
            throw new BadCredentialsException("密码验证失败: " + e.getMessage());
        }

        // 创建认证对象
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}