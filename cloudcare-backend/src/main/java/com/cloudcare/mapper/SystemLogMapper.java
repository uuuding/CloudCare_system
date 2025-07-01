package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudcare.entity.SystemLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 系统日志Mapper接口
 *
 * @author cloudcare
 */
@Mapper
public interface SystemLogMapper extends BaseMapper<SystemLog> {

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
    IPage<SystemLog> selectLogPage(Page<SystemLog> page,
                                   @Param("level") String level,
                                   @Param("module") String module,
                                   @Param("username") String username,
                                   @Param("keyword") String keyword,
                                   @Param("startTime") LocalDateTime startTime,
                                   @Param("endTime") LocalDateTime endTime);

    /**
     * 获取日志统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getLogStats();

    /**
     * 清空所有日志
     *
     * @return 影响行数
     */
    int clearAllLogs();

    /**
     * 获取日志级别选项
     *
     * @return 日志级别列表
     */
    java.util.List<String> getLogLevels();

    /**
     * 获取模块选项
     *
     * @return 模块列表
     */
    java.util.List<String> getLogModules();

    /**
     * 获取最近日志
     *
     * @param limit 限制数量
     * @return 日志列表
     */
    List<SystemLog> getRecentLogs(@Param("limit") Integer limit);

    /**
     * 获取日志趋势数据
     *
     * @param days 天数
     * @return 趋势数据
     */
    List<Map<String, Object>> getLogTrend(@Param("days") Integer days);

    /**
     * 根据业务ID和类型查询日志
     *
     * @param businessId 业务ID
     * @param businessType 业务类型
     * @return 日志列表
     */
    List<SystemLog> getLogsByBusiness(@Param("businessId") String businessId, @Param("businessType") String businessType);

}