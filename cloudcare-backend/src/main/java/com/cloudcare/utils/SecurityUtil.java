package com.cloudcare.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 安全工具类
 *
 * @author cloudcare
 */
@Slf4j
public class SecurityUtil {

    /**
     * 获取当前登录用户名
     *
     * @return 当前登录用户名
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }

    /**
     * 获取当前请求的IP地址
     *
     * @return IP地址
     */
    public static String getIpAddress() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP，多个IP按照','分割
        if (ip != null && ip.indexOf(',') > 0) {
            ip = ip.substring(0, ip.indexOf(','));
        }
        return ip;
    }

    /**
     * 获取当前请求对象
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return attributes.getRequest();
    }

    /**
     * 获取当前请求的完整URL
     *
     * @return 完整URL
     */
    public static String getRequestUrl() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return "";
        }
        return request.getRequestURL().toString();
    }

    /**
     * 获取当前请求的URI
     *
     * @return URI
     */
    public static String getRequestUri() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return "";
        }
        return request.getRequestURI();
    }

    /**
     * 获取当前请求的方法
     *
     * @return 请求方法
     */
    public static String getRequestMethod() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return "";
        }
        return request.getMethod();
    }

    /**
     * 获取当前请求的查询参数
     *
     * @return 查询参数
     */
    public static String getRequestQueryString() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return "";
        }
        return request.getQueryString();
    }

    /**
     * 获取当前登录用户ID
     *
     * @return 当前登录用户ID
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            // 这里需要根据实际的UserDetails实现来获取用户ID
            // 如果UserDetails实现类中包含用户ID，可以强转后获取
            // 暂时返回null，实际使用时需要根据具体实现调整
            return null;
        }
        return null;
    }

    /**
     * 获取当前会话ID
     *
     * @return 会话ID
     */
    public static String getSessionId() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return "";
        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "";
        }
        return session.getId();
    }
}