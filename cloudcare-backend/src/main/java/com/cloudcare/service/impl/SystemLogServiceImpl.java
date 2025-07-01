package com.cloudcare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudcare.entity.SystemLog;
import com.cloudcare.mapper.SystemLogMapper;
import com.cloudcare.service.SystemLogService;
import com.cloudcare.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统日志服务实现类
 *
 * @author cloudcare
 */
@Slf4j
@Service
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper, SystemLog> implements SystemLogService {

    @Autowired
    private SystemLogMapper systemLogMapper;

    @Override
    public IPage<SystemLog> getLogPage(Page<SystemLog> page, String level, String module,
                                       String username, String keyword,
                                       LocalDateTime startTime, LocalDateTime endTime) {
        return systemLogMapper.selectLogPage(page, level, module, username, keyword, startTime, endTime);
    }

    @Override
    public Map<String, Object> getLogStats() {
        try {
            return systemLogMapper.getLogStats();
        } catch (Exception e) {
            log.error("获取日志统计信息失败", e);
            // 返回默认统计数据
            Map<String, Object> defaultStats = new HashMap<>();
            defaultStats.put("totalLogs", 0);
            defaultStats.put("errorLogs", 0);
            defaultStats.put("warnLogs", 0);
            defaultStats.put("infoLogs", 0);
            defaultStats.put("debugLogs", 0);
            return defaultStats;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean clearAllLogs() {
        try {
            systemLogMapper.clearAllLogs();
            log.info("清空系统日志成功，操作用户: {}", SecurityUtil.getCurrentUsername());
            return true;
        } catch (Exception e) {
            log.error("清空系统日志失败", e);
            return false;
        }
    }

    @Override
    public List<String> getLogLevels() {
        try {
            return systemLogMapper.getLogLevels();
        } catch (Exception e) {
            log.error("获取日志级别选项失败", e);
            return List.of("ERROR", "WARN", "INFO", "DEBUG");
        }
    }

    @Override
    public List<String> getLogModules() {
        try {
            return systemLogMapper.getLogModules();
        } catch (Exception e) {
            log.error("获取模块选项失败", e);
            return List.of();
        }
    }

    @Override
    public List<SystemLog> getRecentLogs(Integer limit) {
        try {
            return systemLogMapper.getRecentLogs(limit != null ? limit : 10);
        } catch (Exception e) {
            log.error("获取最近日志失败", e);
            return List.of();
        }
    }

    @Override
    public List<Map<String, Object>> getLogTrend(Integer days) {
        try {
            return systemLogMapper.getLogTrend(days != null ? days : 7);
        } catch (Exception e) {
            log.error("获取日志趋势数据失败", e);
            return List.of();
        }
    }

    @Override
    public List<SystemLog> getLogsByBusiness(String businessId, String businessType) {
        try {
            return systemLogMapper.getLogsByBusiness(businessId, businessType);
        } catch (Exception e) {
            log.error("根据业务ID查询日志失败", e);
            return List.of();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLog(String level, String module, String operation, String content) {
        SystemLog systemLog = new SystemLog();
        systemLog.setLevel(level);
        systemLog.setModule(module);
        systemLog.setOperation(operation);
        systemLog.setContent(content);
        systemLog.setUsername(SecurityUtil.getCurrentUsername());
        systemLog.setUserId(SecurityUtil.getCurrentUserId());
        systemLog.setIpAddress(SecurityUtil.getIpAddress());
        systemLog.setSessionId(SecurityUtil.getSessionId());
        systemLog.setCreateTime(LocalDateTime.now());
        
        save(systemLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLog(SystemLog systemLog) {
        if (systemLog.getCreateTime() == null) {
            systemLog.setCreateTime(LocalDateTime.now());
        }
        if (StringUtils.isBlank(systemLog.getUsername())) {
            systemLog.setUsername(SecurityUtil.getCurrentUsername());
        }
        if (systemLog.getUserId() == null) {
            systemLog.setUserId(SecurityUtil.getCurrentUserId());
        }
        if (StringUtils.isBlank(systemLog.getIpAddress())) {
            systemLog.setIpAddress(SecurityUtil.getIpAddress());
        }
        if (StringUtils.isBlank(systemLog.getSessionId())) {
            systemLog.setSessionId(SecurityUtil.getSessionId());
        }
        
        save(systemLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserLog(String username, Long userId, String module, String operation,
                            String content, String requestUrl, String requestMethod, String ipAddress) {
        SystemLog systemLog = new SystemLog();
        systemLog.setLevel("INFO");
        systemLog.setModule(module);
        systemLog.setOperation(operation);
        systemLog.setContent(content);
        systemLog.setUsername(username);
        systemLog.setUserId(userId);
        systemLog.setRequestUrl(requestUrl);
        systemLog.setRequestMethod(requestMethod);
        systemLog.setIpAddress(ipAddress);
        systemLog.setSessionId(SecurityUtil.getSessionId());
        systemLog.setCreateTime(LocalDateTime.now());
        
        save(systemLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveErrorLog(String module, String operation, String content, String stackTrace) {
        SystemLog systemLog = new SystemLog();
        systemLog.setLevel("ERROR");
        systemLog.setModule(module);
        systemLog.setOperation(operation);
        systemLog.setContent(content);
        systemLog.setStackTrace(stackTrace);
        systemLog.setUsername(SecurityUtil.getCurrentUsername());
        systemLog.setUserId(SecurityUtil.getCurrentUserId());
        systemLog.setIpAddress(SecurityUtil.getIpAddress());
        systemLog.setSessionId(SecurityUtil.getSessionId());
        systemLog.setCreateTime(LocalDateTime.now());
        
        save(systemLog);
    }

    @Override
    public List<SystemLog> exportLogs(String level, String module, String username,
                                      String keyword, LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<SystemLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(level), SystemLog::getLevel, level)
                .eq(StringUtils.isNotBlank(module), SystemLog::getModule, module)
                .eq(StringUtils.isNotBlank(username), SystemLog::getUsername, username)
                .and(StringUtils.isNotBlank(keyword), w -> w
                        .like(SystemLog::getContent, keyword)
                        .or()
                        .like(SystemLog::getOperation, keyword)
                        .or()
                        .like(SystemLog::getRequestUrl, keyword))
                .ge(startTime != null, SystemLog::getCreateTime, startTime)
                .le(endTime != null, SystemLog::getCreateTime, endTime)
                .orderByDesc(SystemLog::getCreateTime);
        
        return list(wrapper);
    }

}