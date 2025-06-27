package com.cloudcare.security;

import com.cloudcare.config.SecurityProperties;
import com.cloudcare.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证过滤器
 *
 * @author cloudcare
 */
@Slf4j
@Component
@Lazy
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Value("${cloudcare.jwt.header}")
    private String tokenHeader;

    @Value("${cloudcare.jwt.token-start-with}")
    private String tokenStartWith;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 如果是白名单URL，则直接放行
        if (securityProperties.getIgnoreUrls().contains(request.getRequestURI())) {
            SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
            return;
            }

        // 获取请求头中的token
        String authHeader = request.getHeader(tokenHeader);

        // 判断是否有token
        if (StringUtils.isNotBlank(authHeader) && authHeader.startsWith(tokenStartWith)) {
            // 去除Bearer前缀
            String authToken = authHeader.substring(tokenStartWith.length());

            // 从token中获取用户名
            String username = jwtUtil.getUsernameFromToken(authToken);

            if (StringUtils.isNotBlank(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 根据用户名获取用户详情
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 验证token是否有效
                if (jwtUtil.validateToken(authToken, userDetails)) {
                    // 创建认证对象
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 设置认证信息
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("用户[{}]认证成功", username);
                } else {
                    log.debug("用户[{}]的token无效", username);
                }
            }
        }

        chain.doFilter(request, response);
    }
}