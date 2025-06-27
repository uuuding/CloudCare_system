package com.cloudcare.exception;

import com.cloudcare.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author cloudcare
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
    public Result<String> handleValidException(Exception e) {
        BindingResult bindingResult = null;
        if (e instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
        } else if (e instanceof BindException) {
            bindingResult = ((BindException) e).getBindingResult();
        }
        
        StringBuilder message = new StringBuilder();
        if (bindingResult != null && bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message.append(fieldError.getField()).append(fieldError.getDefaultMessage());
            }
        } else {
            message.append("参数验证失败");
        }
        
        log.error("参数验证失败：{}", message, e);
        return Result.validateFailed(message.toString());
    }

    /**
     * 处理认证异常
     */
    @ExceptionHandler(value = {BadCredentialsException.class, UsernameNotFoundException.class})
    public Result<String> handleAuthenticationException(Exception e) {
        log.error("认证失败：{}", e.getMessage(), e);
        return Result.error("用户名或密码错误");
    }

    /**
     * 处理账号禁用异常
     */
    @ExceptionHandler(DisabledException.class)
    public Result<String> handleDisabledException(DisabledException e) {
        log.error("账号已被禁用：{}", e.getMessage(), e);
        return Result.error("账号已被禁用");
    }

    /**
     * 处理权限不足异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<String> handleAccessDeniedException(AccessDeniedException e) {
        log.error("权限不足：{}", e.getMessage(), e);
        return Result.forbidden();
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(BusinessException e) {
        log.error("业务异常：{}", e.getMessage(), e);
        return Result.error(e.getMessage());
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        log.error("系统异常：{}", e.getMessage(), e);
        return Result.error("系统异常，请联系管理员");
    }
}