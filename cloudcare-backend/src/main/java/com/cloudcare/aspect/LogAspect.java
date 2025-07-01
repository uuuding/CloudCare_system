package com.cloudcare.aspect;

import com.alibaba.fastjson2.JSON;
import com.cloudcare.common.annotation.Log;
import com.cloudcare.common.enums.BusinessType;
import com.cloudcare.entity.SystemLog;
import com.cloudcare.service.SystemLogService;
import com.cloudcare.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

/**
 * 操作日志记录处理切面
 *
 * @author CloudCare
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LogAspect {

    private final SystemLogService systemLogService;

    /**
     * 配置织入点
     */
    @Pointcut("@annotation(com.cloudcare.common.annotation.Log)")
    public void logPointCut() {
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     * @param result    返回结果
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        handleLog(joinPoint, null, result);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    /**
     * 处理日志记录
     *
     * @param joinPoint 连接点
     * @param e         异常
     * @param result    返回结果
     */
    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object result) {
        try {
            // 获得注解
            Log controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }

            // 获取当前的用户
            String username;
            try {
                username = SecurityUtil.getCurrentUsername();
                if (username == null) {
                    username = "anonymous";
                }
            } catch (Exception ex) {
                log.warn("获取当前用户名失败: {}", ex.getMessage());
                username = "anonymous";
            }

            // 构建系统日志对象
            SystemLog systemLog = new SystemLog();
            systemLog.setModule(controllerLog.title());
            systemLog.setBusinessType(controllerLog.businessType().name());
            systemLog.setOperation(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()");
            systemLog.setRequestMethod(getRequestMethod());
            systemLog.setUsername(username);
            systemLog.setRequestUrl(getRequestUrl());
            systemLog.setIpAddress(SecurityUtil.getIpAddress());
            systemLog.setCreateTime(LocalDateTime.now());
            systemLog.setResponseStatus(e == null ? 200 : 500); // 200成功 500失败

            // 设置请求参数
            if (controllerLog.isSaveRequestData()) {
                setRequestValue(joinPoint, systemLog);
            }

            // 设置响应参数
            if (controllerLog.isSaveResponseData() && result != null) {
                systemLog.setContent(JSON.toJSONString(result));
            }

            // 设置异常信息
            if (e != null) {
                systemLog.setStackTrace(e.getMessage());
                systemLog.setLevel("ERROR");
            } else {
                systemLog.setLevel("INFO");
            }

            // 保存数据库
            systemLogService.saveLog(systemLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("记录操作日志异常: {}", exp.getMessage(), exp);
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param joinPoint 连接点
     * @param systemLog 系统日志
     */
    private void setRequestValue(JoinPoint joinPoint, SystemLog systemLog) {
        try {
            String requestMethod = systemLog.getRequestMethod();
            if ("PUT".equals(requestMethod) || "POST".equals(requestMethod)) {
                String params = argsArrayToString(joinPoint.getArgs());
                systemLog.setRequestParams(params);
            }
        } catch (Exception e) {
            log.warn("获取请求参数异常: {}", e.getMessage());
        }
    }

    /**
     * 获取请求方法
     */
    private String getRequestMethod() {
        HttpServletRequest request = getRequest();
        return request != null ? request.getMethod() : "";
    }

    /**
     * 获取请求URL
     */
    private String getRequestUrl() {
        HttpServletRequest request = getRequest();
        return request != null ? request.getRequestURI() : "";
    }

    /**
     * 获取当前请求对象
     */
    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (Objects.nonNull(o) && !isFilterObject(o)) {
                    try {
                        String jsonObj = JSON.toJSONString(o);
                        params.append(jsonObj).append(" ");
                    } catch (Exception e) {
                        log.warn("参数序列化异常: {}", e.getMessage());
                    }
                }
            }
        }
        return params.toString().trim();
    }

    /**
     * 判断是否需要过滤的对象
     *
     * @param o 对象信息
     * @return 如果是需要过滤的对象，则返回true；否则返回false
     */
    private boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(HttpServletRequest.class);
        } else if (clazz.getName().contains("HttpServletRequest") ||
                   clazz.getName().contains("HttpServletResponse") ||
                   clazz.getName().contains("MultipartFile")) {
            return true;
        }
        return false;
    }
}