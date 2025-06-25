package com.cloudcare.security;

import com.alibaba.fastjson2.JSON;
import com.cloudcare.common.Result;
import com.cloudcare.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义未授权处理器
 *
 * @author cloudcare
 */
@Slf4j
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        log.error("未授权访问：{}", request.getRequestURI(), e);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSON.toJSONString(Result.error(ResultCode.FORBIDDEN, "没有相关权限")));
        response.getWriter().flush();
    }
}