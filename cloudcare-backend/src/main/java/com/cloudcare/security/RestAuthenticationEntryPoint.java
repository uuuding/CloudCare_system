package com.cloudcare.security;

import com.alibaba.fastjson2.JSON;
import com.cloudcare.common.Result;
import com.cloudcare.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义未认证处理器
 *
 * @author cloudcare
 */
@Slf4j
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        log.error("未认证访问：{}", request.getRequestURI(), e);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSON.toJSONString(Result.error(ResultCode.UNAUTHORIZED, "暂未登录或token已过期")));
        response.getWriter().flush();
    }
}