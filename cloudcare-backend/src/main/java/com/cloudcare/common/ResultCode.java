package com.cloudcare.common;

/**
 * 响应状态码常量类
 *
 * @author cloudcare
 */
public class ResultCode {

    /**
     * 成功
     */
    public static final int SUCCESS = 200;

    /**
     * 失败
     */
    public static final int ERROR = 500;

    /**
     * 参数校验失败
     */
    public static final int VALIDATE_FAILED = 400;

    /**
     * 未认证
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * 未授权
     */
    public static final int FORBIDDEN = 403;

    /**
     * 资源不存在
     */
    public static final int NOT_FOUND = 404;

    /**
     * 方法不允许
     */
    public static final int METHOD_NOT_ALLOWED = 405;

    /**
     * 请求超时
     */
    public static final int REQUEST_TIMEOUT = 408;

    /**
     * 服务器内部错误
     */
    public static final int INTERNAL_SERVER_ERROR = 500;

    /**
     * 服务不可用
     */
    public static final int SERVICE_UNAVAILABLE = 503;

    /**
     * 用户名或密码错误
     */
    public static final int USERNAME_PASSWORD_ERROR = 1001;

    /**
     * 账号已被禁用
     */
    public static final int ACCOUNT_DISABLED = 1002;

    /**
     * 账号已过期
     */
    public static final int ACCOUNT_EXPIRED = 1003;

    /**
     * 验证码错误
     */
    public static final int CAPTCHA_ERROR = 1004;

    /**
     * 数据已存在
     */
    public static final int DATA_ALREADY_EXISTS = 2001;

    /**
     * 数据不存在
     */
    public static final int DATA_NOT_EXISTS = 2002;
    
    /**
     * 数据未找到
     */
    public static final int DATA_NOT_FOUND = 2002;

    /**
     * 数据库操作失败
     */
    public static final int DATABASE_ERROR = 2003;

    /**
     * 文件上传失败
     */
    public static final int FILE_UPLOAD_ERROR = 3001;

    /**
     * 文件下载失败
     */
    public static final int FILE_DOWNLOAD_ERROR = 3002;

    /**
     * 文件类型不支持
     */
    public static final int FILE_TYPE_NOT_SUPPORTED = 3003;

    /**
     * 文件大小超出限制
     */
    public static final int FILE_SIZE_EXCEEDED = 3004;

}