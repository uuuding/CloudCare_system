-- 创建系统日志表
CREATE TABLE IF NOT EXISTS `system_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `level` varchar(20) NOT NULL COMMENT '日志级别：DEBUG-调试，INFO-信息，WARN-警告，ERROR-错误',
  `module` varchar(50) NOT NULL COMMENT '模块名称：USER-用户，ELDERLY-老人档案，HEALTH-健康管理，MEDICAL-医疗服务，DEVICE-设备，SYSTEM-系统等',
  `username` varchar(100) DEFAULT NULL COMMENT '操作用户名',
  `user_id` bigint(20) DEFAULT NULL COMMENT '操作用户ID',
  `operation` varchar(200) NOT NULL COMMENT '操作类型/名称',
  `content` text NOT NULL COMMENT '日志内容详情',
  `request_url` varchar(500) DEFAULT NULL COMMENT '请求URL',
  `request_method` varchar(10) DEFAULT NULL COMMENT '请求方法：GET,POST,PUT,DELETE等',
  `request_params` text DEFAULT NULL COMMENT '请求参数（JSON格式）',
  `response_status` int(11) DEFAULT NULL COMMENT '响应状态码',
  `response_time` bigint(20) DEFAULT NULL COMMENT '响应时间（毫秒）',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(1000) DEFAULT NULL COMMENT '用户代理信息',
  `session_id` varchar(100) DEFAULT NULL COMMENT '会话ID',
  `stack_trace` text DEFAULT NULL COMMENT '异常堆栈信息（错误日志专用）',
  `business_id` varchar(100) DEFAULT NULL COMMENT '业务ID（如老人ID、订单ID等）',
  `business_type` varchar(50) DEFAULT NULL COMMENT '业务类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_level` (`level`),
  KEY `idx_module` (`module`),
  KEY `idx_username` (`username`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_operation` (`operation`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_ip_address` (`ip_address`),
  KEY `idx_business` (`business_id`, `business_type`),
  KEY `idx_level_module` (`level`, `module`),
  KEY `idx_time_level` (`create_time`, `level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统日志表';

-- 插入示例数据
INSERT INTO `system_log` (`level`, `module`, `username`, `user_id`, `operation`, `content`, `request_url`, `request_method`, `request_params`, `response_status`, `response_time`, `ip_address`, `user_agent`, `session_id`, `business_id`, `business_type`) VALUES
('INFO', 'USER', 'admin', 1, '用户登录', '用户admin成功登录系统', '/api/auth/login', 'POST', '{"username":"admin"}', 200, 156, '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 'SESSION_001', '1', 'USER'),
('WARN', 'ELDERLY', 'doctor1', 2, '档案修改', '修改老人档案信息，老人ID: 12345', '/api/elderly/update/12345', 'PUT', '{"elderlyId":12345,"name":"张三"}', 200, 234, '192.168.1.101', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 'SESSION_002', '12345', 'ELDERLY'),
('ERROR', 'HEALTH', 'nurse1', 3, '数据同步', '健康数据同步失败：连接超时', '/api/health/sync', 'POST', '{"deviceId":"BP001"}', 500, 5000, '192.168.1.102', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 'SESSION_003', 'BP001', 'DEVICE'),
('INFO', 'MEDICAL', 'admin', 1, '预约创建', '创建医疗预约，预约ID: AP001', '/api/medical/appointment', 'POST', '{"elderlyId":12345,"doctorId":1}', 201, 189, '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 'SESSION_001', 'AP001', 'APPOINTMENT'),
('DEBUG', 'SYSTEM', 'system', NULL, '定时任务', '执行数据清理定时任务', NULL, NULL, NULL, NULL, 2345, '127.0.0.1', 'System Task', NULL, NULL, 'TASK'),
('ERROR', 'DEVICE', 'technician1', 4, '设备故障', '血压计设备BP001出现通信故障', '/api/device/status/BP001', 'GET', NULL, 500, 1234, '192.168.1.103', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 'SESSION_004', 'BP001', 'DEVICE'),
('INFO', 'USER', 'nurse2', 5, '密码修改', '用户nurse2修改登录密码', '/api/user/password', 'PUT', '{"userId":5}', 200, 167, '192.168.1.104', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 'SESSION_005', '5', 'USER'),
('WARN', 'HEALTH', 'doctor2', 6, '异常数据', '检测到老人心率异常数据，心率: 120bpm', '/api/health/alert', 'POST', '{"elderlyId":12346,"heartRate":120}', 200, 298, '192.168.1.105', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 'SESSION_006', '12346', 'ELDERLY'),
('INFO', 'SYSTEM', 'admin', 1, '系统配置', '更新系统配置参数', '/api/system/config', 'PUT', '{"maxLoginAttempts":5}', 200, 145, '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 'SESSION_001', NULL, 'CONFIG'),
('ERROR', 'MEDICAL', 'doctor1', 2, '预约冲突', '预约时间冲突，无法创建预约', '/api/medical/appointment', 'POST', '{"elderlyId":12347,"doctorId":2,"time":"2024-01-15 10:00"}', 409, 123, '192.168.1.101', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 'SESSION_002', NULL, 'APPOINTMENT');

-- 创建系统日志统计视图（可选）
CREATE OR REPLACE VIEW `system_log_stats` AS
SELECT 
    DATE(create_time) as log_date,
    level,
    module,
    COUNT(*) as log_count
FROM system_log 
WHERE create_time >= DATE_SUB(NOW(), INTERVAL 30 DAY)
GROUP BY DATE(create_time), level, module
ORDER BY log_date DESC, log_count DESC;

-- 创建日志清理存储过程（可选）
DELIMITER //
CREATE PROCEDURE CleanOldLogs(IN days_to_keep INT)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;
    
    -- 删除指定天数之前的日志（保留ERROR级别的日志更长时间）
    DELETE FROM system_log 
    WHERE create_time < DATE_SUB(NOW(), INTERVAL days_to_keep DAY)
    AND level != 'ERROR';
    
    -- ERROR级别的日志保留更长时间（默认90天）
    DELETE FROM system_log 
    WHERE create_time < DATE_SUB(NOW(), INTERVAL (days_to_keep * 3) DAY)
    AND level = 'ERROR';
    
    COMMIT;
END //
DELIMITER ;

-- 创建日志级别统计函数（可选）
DELIMITER //
CREATE FUNCTION GetLogCountByLevel(log_level VARCHAR(20), start_date DATE, end_date DATE)
RETURNS INT
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE log_count INT DEFAULT 0;
    
    SELECT COUNT(*) INTO log_count
    FROM system_log
    WHERE level = log_level
    AND DATE(create_time) BETWEEN start_date AND end_date;
    
    RETURN log_count;
END //
DELIMITER ;