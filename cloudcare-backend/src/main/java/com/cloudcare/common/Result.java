package com.cloudcare.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用响应结果类
 *
 * @author cloudcare
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    private boolean success;

    /**
     * 返回代码
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 时间戳
     */
    private long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功返回结果
     *
     * @return 通用返回结果
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setCode(ResultCode.SUCCESS);
        result.setMessage("操作成功");
        return result;
    }

    /**
     * 成功返回结果
     *
     * @param data 返回数据
     * @return 通用返回结果
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setCode(ResultCode.SUCCESS);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 成功返回结果
     *
     * @param data    返回数据
     * @param message 返回消息
     * @return 通用返回结果
     */
    public static <T> Result<T> success(T data, String message) {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setCode(ResultCode.SUCCESS);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 失败返回结果
     *
     * @param code    错误码
     * @param message 错误消息
     * @return 通用返回结果
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 失败返回结果
     *
     * @param message 错误消息
     * @return 通用返回结果
     */
    public static <T> Result<T> error(String message) {
        return error(ResultCode.ERROR, message);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param message 错误消息
     * @return 通用返回结果
     */
    public static <T> Result<T> validateFailed(String message) {
        return error(ResultCode.VALIDATE_FAILED, message);
    }

    /**
     * 未登录返回结果
     *
     * @return 通用返回结果
     */
    public static <T> Result<T> unauthorized() {
        return error(ResultCode.UNAUTHORIZED, "暂未登录或token已过期");
    }

    /**
     * 未授权返回结果
     *
     * @return 通用返回结果
     */
    public static <T> Result<T> forbidden() {
        return error(ResultCode.FORBIDDEN, "没有相关权限");
    }
    
    /**
     * 失败返回结果
     *
     * @param code    错误码
     * @param message 错误消息
     * @return 通用返回结果
     */
    public static <T> Result<T> failure(Integer code, String message) {
        return error(code, message);
    }

}