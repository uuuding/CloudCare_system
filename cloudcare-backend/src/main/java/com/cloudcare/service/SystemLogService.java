package com.cloudcare.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudcare.entity.SystemLog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 系统日志服务接口
 *
 * @author cloudcare
 */
public interface SystemLogService extends IService<SystemLog> {

    /**
     * 分页查询系统日志
     *
     * @param page 分页对象
     * @param level 日志级别
     * @param module 模块名称
     * @param username 用户名
     * @param keyword 关键词
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 分页结果
     */
    IPage<SystemLog> getLogPage(Page<SystemLog> page, String level, String module, 
                                String username, String keyword, 
                                LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取日志统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getLogStats();

    /**
     * 清空所有日志
     *
     * @return 清空成功返回true，失败返回false
     */
    boolean clearAllLogs();

    /**
     * 获取日志级别选项
     *
     * @return 日志级别列表
     */
    List<String> getLogLevels();

    /**
     * 获取模块选项
     *
     * @return 模块列表
     */
    List<String> getLogModules();

    /**
     * 获取最近日志
     *
     * @param limit 限制数量
     * @return 最近日志列表
     */
    List<SystemLog> getRecentLogs(Integer limit);

    /**
     * 获取日志趋势数据
     *
     * @param days 天数
     * @return 趋势数据
     */
    List<Map<String, Object>> getLogTrend(Integer days);

    /**
     * 根据业务ID和类型查询日志
     *
     * @param businessId 业务ID
     * @param businessType 业务类型
     * @return 日志列表
     */
    List<SystemLog> getLogsByBusiness(String businessId, String businessType);

    /**
     * 记录系统日志
     *
     * @param level 日志级别
     * @param module 模块名称
     * @param operation 操作类型
     * @param content 日志内容
     */
    void saveLog(String level, String module, String operation, String content);

    /**
     * 记录系统日志（完整参数）
     *
     * @param systemLog 系统日志对象
     */
    void saveLog(SystemLog systemLog);

    /**
     * 记录用户操作日志
     *
     * @param username 用户名
     * @param userId 用户ID
     * @param module 模块名称
     * @param operation 操作类型
     * @param content 日志内容
     * @param requestUrl 请求URL
     * @param requestMethod 请求方法
     * @param ipAddress IP地址
     */
    void saveUserLog(String username, Long userId, String module, String operation, 
                     String content, String requestUrl, String requestMethod, String ipAddress);

    /**
     * 记录错误日志
     *
     * @param module 模块名称
     * @param operation 操作类型
     * @param content 错误内容
     * @param stackTrace 异常堆栈
     */
    void saveErrorLog(String module, String operation, String content, String stackTrace);

    /**
     * 导出日志数据
     *
     * @param level 日志级别
     * @param module 模块名称
     * @param username 用户名
     * @param keyword 关键词
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 日志数据列表
     */
    List<SystemLog> exportLogs(String level, String module, String username, 
                               String keyword, LocalDateTime startTime, LocalDateTime endTime);

}