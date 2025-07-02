-- =====================================================
-- 电子围栏功能完整数据库表结构
-- CloudCare老人防丢电子围栏系统
-- 创建时间: 2024-01-15
-- =====================================================

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =====================================================
-- 1. 核心数据表
-- =====================================================

-- 1.1 老人基础信息表（如果不存在）
CREATE TABLE IF NOT EXISTS `elderly_profile` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '老人ID',
    `name` VARCHAR(50) NOT NULL COMMENT '老人姓名',
    `gender` TINYINT NOT NULL DEFAULT 1 COMMENT '性别：1-男，2-女',
    `birth_date` DATE NULL COMMENT '出生日期',
    `phone` VARCHAR(20) NULL COMMENT '联系电话',
    `emergency_contact` VARCHAR(20) NULL COMMENT '紧急联系人电话',
    `emergency_contact_name` VARCHAR(50) NULL COMMENT '紧急联系人姓名',
    `address` VARCHAR(200) NULL COMMENT '家庭住址',
    `medical_history` TEXT NULL COMMENT '病史信息',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-停用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_name` (`name`),
    INDEX `idx_phone` (`phone`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='老人基础信息表';

-- 1.2 设备信息表
CREATE TABLE IF NOT EXISTS `device_info` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '设备ID',
    `macid` VARCHAR(50) NOT NULL UNIQUE COMMENT '设备编号（IMEI）',
    `device_name` VARCHAR(100) NULL COMMENT '设备名称',
    `device_type` VARCHAR(20) NOT NULL DEFAULT 'gps' COMMENT '设备类型：gps-GPS定位器，watch-智能手表',
    `elderly_id` INT NULL COMMENT '绑定的老人ID',
    `sim_number` VARCHAR(20) NULL COMMENT 'SIM卡号码',
    `battery_level` TINYINT NULL COMMENT '电池电量（0-100）',
    `signal_strength` TINYINT NULL COMMENT '信号强度（0-100）',
    `firmware_version` VARCHAR(20) NULL COMMENT '固件版本',
    `last_online_time` DATETIME NULL COMMENT '最后在线时间',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '设备状态：1-正常，2-离线，3-故障，0-停用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_macid` (`macid`),
    INDEX `idx_elderly_id` (`elderly_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_last_online` (`last_online_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备信息表';

-- 1.3 GPS定位数据表（优化版）
CREATE TABLE IF NOT EXISTS `gps_location` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `macid` VARCHAR(50) NOT NULL COMMENT '设备编号（IMEI）',
    `elderly_id` INT NULL COMMENT '老人ID',
    `gps_time` BIGINT NOT NULL COMMENT 'GPS时间戳（毫秒）',
    `heart_time` BIGINT NULL COMMENT '信号时间',
    `upd_time` BIGINT NULL COMMENT '最后更新位置时间',
    `lat` DOUBLE NOT NULL COMMENT 'GPS纬度',
    `lon` DOUBLE NOT NULL COMMENT 'GPS经度',
    `altitude` DOUBLE NULL COMMENT '海拔高度（米）',
    `accuracy` DOUBLE NULL COMMENT '定位精度（米）',
    `speed` DOUBLE NULL COMMENT '速度（单位：km/h）',
    `direction` DOUBLE NULL COMMENT '方向角度（0-360度）',
    `satellite_count` TINYINT NULL COMMENT '卫星数量',
    `location_type` VARCHAR(10) NOT NULL DEFAULT 'gps' COMMENT '定位类型：gps-GPS，wifi-WiFi，lbs-基站',
    `address` VARCHAR(200) NULL COMMENT '地址信息（逆地理编码）',
    `stats` VARCHAR(500) NULL COMMENT '状态字段，逗号分隔',
    `value` VARCHAR(500) NULL COMMENT '状态值字段，逗号分隔',
    `is_valid` TINYINT NOT NULL DEFAULT 1 COMMENT '数据有效性：1-有效，0-无效',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_macid` (`macid`),
    INDEX `idx_elderly_id` (`elderly_id`),
    INDEX `idx_gps_time` (`gps_time`),
    INDEX `idx_location` (`lat`, `lon`),
    INDEX `idx_create_time` (`create_time`),
    INDEX `idx_composite` (`elderly_id`, `gps_time` DESC),
    INDEX `idx_valid_time` (`is_valid`, `gps_time` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='GPS定位数据表';

-- 1.4 电子围栏配置表（增强版）
CREATE TABLE IF NOT EXISTS `geo_fence` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `elderly_id` INT NOT NULL COMMENT '老人ID',
    `fence_name` VARCHAR(100) NOT NULL COMMENT '围栏名称',
    `fence_description` VARCHAR(500) NULL COMMENT '围栏描述',
    `fence_type` VARCHAR(20) NOT NULL COMMENT '围栏类型：circle-圆形，polygon-多边形，rectangle-矩形',
    `center_lat` DOUBLE NULL COMMENT '围栏中心点纬度（圆形围栏使用）',
    `center_lon` DOUBLE NULL COMMENT '围栏中心点经度（圆形围栏使用）',
    `radius` DOUBLE NULL COMMENT '围栏半径（米，圆形围栏使用）',
    `coordinates` TEXT NULL COMMENT '围栏坐标点（JSON格式，多边形围栏使用）',
    `min_lat` DOUBLE NULL COMMENT '最小纬度（矩形围栏使用）',
    `max_lat` DOUBLE NULL COMMENT '最大纬度（矩形围栏使用）',
    `min_lon` DOUBLE NULL COMMENT '最小经度（矩形围栏使用）',
    `max_lon` DOUBLE NULL COMMENT '最大经度（矩形围栏使用）',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '围栏状态：1-启用，0-禁用',
    `enter_alert` TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用进入提醒：1-启用，0-禁用',
    `exit_alert` TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用离开提醒：1-启用，0-禁用',
    `alert_type` VARCHAR(20) NOT NULL DEFAULT 'sms' COMMENT '提醒方式：sms-短信，app-应用推送，both-两者都有',
    `alert_delay` INT NOT NULL DEFAULT 0 COMMENT '告警延迟时间（秒），0表示立即告警',
    `emergency_contacts` VARCHAR(500) NULL COMMENT '紧急联系人手机号（多个用逗号分隔）',
    `active_time_start` TIME NULL COMMENT '生效开始时间（如：08:00）',
    `active_time_end` TIME NULL COMMENT '生效结束时间（如：22:00）',
    `active_days` VARCHAR(20) NULL COMMENT '生效日期（1234567表示周一到周日）',
    `priority` TINYINT NOT NULL DEFAULT 1 COMMENT '优先级：1-低，2-中，3-高',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` VARCHAR(50) NULL COMMENT '创建者',
    `update_by` VARCHAR(50) NULL COMMENT '更新者',
    PRIMARY KEY (`id`),
    INDEX `idx_elderly_id` (`elderly_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_priority` (`priority`),
    INDEX `idx_fence_type` (`fence_type`),
    INDEX `idx_create_time` (`create_time`),
    INDEX `idx_elderly_status` (`elderly_id`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='电子围栏配置表';

-- 1.5 电子围栏事件记录表（增强版）
CREATE TABLE IF NOT EXISTS `geo_fence_event` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `elderly_id` INT NOT NULL COMMENT '老人ID',
    `fence_id` BIGINT NOT NULL COMMENT '围栏ID',
    `fence_name` VARCHAR(100) NOT NULL COMMENT '围栏名称',
    `event_type` VARCHAR(20) NOT NULL COMMENT '事件类型：enter-进入，exit-离开，stay-停留',
    `lat` DOUBLE NOT NULL COMMENT '事件发生时的纬度',
    `lon` DOUBLE NOT NULL COMMENT '事件发生时的经度',
    `location_id` BIGINT NULL COMMENT 'GPS定位记录ID',
    `macid` VARCHAR(50) NULL COMMENT '设备编号（IMEI）',
    `event_time` DATETIME NOT NULL COMMENT '事件发生时间',
    `duration` INT NULL COMMENT '停留时长（秒，仅stay事件使用）',
    `alert_sent` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已发送提醒：1-已发送，0-未发送',
    `alert_sent_time` DATETIME NULL COMMENT '提醒发送时间',
    `alert_type` VARCHAR(20) NULL COMMENT '提醒方式',
    `alert_content` TEXT NULL COMMENT '提醒内容',
    `alert_recipients` VARCHAR(500) NULL COMMENT '提醒接收人（手机号，多个用逗号分隔）',
    `processing_status` VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '处理状态：pending-待处理，processed-已处理，ignored-已忽略',
    `processed_by` VARCHAR(50) NULL COMMENT '处理人',
    `processed_time` DATETIME NULL COMMENT '处理时间',
    `remarks` VARCHAR(500) NULL COMMENT '备注信息',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX `idx_elderly_id` (`elderly_id`),
    INDEX `idx_fence_id` (`fence_id`),
    INDEX `idx_event_type` (`event_type`),
    INDEX `idx_event_time` (`event_time`),
    INDEX `idx_alert_sent` (`alert_sent`),
    INDEX `idx_processing_status` (`processing_status`),
    INDEX `idx_create_time` (`create_time`),
    INDEX `idx_composite` (`elderly_id`, `event_time` DESC),
    INDEX `idx_fence_event` (`fence_id`, `event_type`, `event_time` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='电子围栏事件记录表';

-- 1.6 围栏告警规则表
CREATE TABLE IF NOT EXISTS `fence_alert_rule` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '规则ID',
    `fence_id` BIGINT NOT NULL COMMENT '围栏ID',
    `rule_name` VARCHAR(100) NOT NULL COMMENT '规则名称',
    `rule_type` VARCHAR(20) NOT NULL COMMENT '规则类型：time_based-基于时间，frequency_based-基于频率',
    `trigger_condition` VARCHAR(50) NOT NULL COMMENT '触发条件：immediate-立即，delay-延迟，multiple-多次',
    `trigger_value` INT NULL COMMENT '触发值（延迟秒数或次数）',
    `alert_template` TEXT NULL COMMENT '告警模板内容',
    `escalation_level` TINYINT NOT NULL DEFAULT 1 COMMENT '升级级别：1-普通，2-重要，3-紧急',
    `escalation_delay` INT NULL COMMENT '升级延迟时间（分钟）',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_fence_id` (`fence_id`),
    INDEX `idx_rule_type` (`rule_type`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='围栏告警规则表';

-- =====================================================
-- 2. 视图定义
-- =====================================================

-- 2.1 老人最新位置视图
CREATE OR REPLACE VIEW `v_elderly_latest_location` AS
SELECT 
    gl1.elderly_id,
    ep.name as elderly_name,
    gl1.macid,
    gl1.lat,
    gl1.lon,
    gl1.altitude,
    gl1.accuracy,
    gl1.speed,
    gl1.direction,
    gl1.address,
    gl1.location_type,
    gl1.gps_time,
    gl1.create_time,
    di.device_name,
    di.battery_level,
    di.signal_strength,
    di.status as device_status
FROM gps_location gl1
INNER JOIN (
    SELECT elderly_id, MAX(gps_time) as max_gps_time
    FROM gps_location
    WHERE elderly_id IS NOT NULL AND is_valid = 1
    GROUP BY elderly_id
) gl2 ON gl1.elderly_id = gl2.elderly_id AND gl1.gps_time = gl2.max_gps_time
LEFT JOIN elderly_profile ep ON gl1.elderly_id = ep.id
LEFT JOIN device_info di ON gl1.macid = di.macid;

-- 2.2 围栏事件统计视图
CREATE OR REPLACE VIEW `v_fence_event_statistics` AS
SELECT 
    gfe.elderly_id,
    ep.name as elderly_name,
    gfe.fence_id,
    gfe.fence_name,
    gfe.event_type,
    DATE(gfe.event_time) as event_date,
    COUNT(*) as event_count,
    COUNT(CASE WHEN gfe.alert_sent = 1 THEN 1 END) as alert_sent_count,
    COUNT(CASE WHEN gfe.processing_status = 'pending' THEN 1 END) as pending_count
FROM geo_fence_event gfe
LEFT JOIN elderly_profile ep ON gfe.elderly_id = ep.id
GROUP BY gfe.elderly_id, ep.name, gfe.fence_id, gfe.fence_name, gfe.event_type, DATE(gfe.event_time)
ORDER BY event_date DESC, gfe.elderly_id, gfe.fence_id;

-- 2.3 活跃围栏视图
CREATE OR REPLACE VIEW `v_active_fences` AS
SELECT 
    gf.id,
    gf.elderly_id,
    ep.name as elderly_name,
    gf.fence_name,
    gf.fence_type,
    gf.center_lat,
    gf.center_lon,
    gf.radius,
    gf.priority,
    gf.enter_alert,
    gf.exit_alert,
    gf.alert_type,
    gf.emergency_contacts,
    gf.create_time,
    COUNT(gfe.id) as total_events,
    MAX(gfe.event_time) as last_event_time
FROM geo_fence gf
LEFT JOIN elderly_profile ep ON gf.elderly_id = ep.id
LEFT JOIN geo_fence_event gfe ON gf.id = gfe.fence_id
WHERE gf.status = 1
GROUP BY gf.id, gf.elderly_id, ep.name, gf.fence_name, gf.fence_type, 
         gf.center_lat, gf.center_lon, gf.radius, gf.priority,
         gf.enter_alert, gf.exit_alert, gf.alert_type, gf.emergency_contacts, gf.create_time
ORDER BY gf.priority DESC, gf.create_time DESC;

-- =====================================================
-- 3. 存储过程
-- =====================================================

-- 3.1 清理历史GPS数据
DELIMITER //
CREATE PROCEDURE `CleanOldGpsData`(IN days_to_keep INT)
BEGIN
    DECLARE exit handler for sqlexception
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;
    
    -- 删除指定天数之前的GPS定位数据
    DELETE FROM gps_location 
    WHERE create_time < DATE_SUB(NOW(), INTERVAL days_to_keep DAY);
    
    -- 记录清理结果
    SELECT CONCAT('清理了 ', ROW_COUNT(), ' 条GPS定位记录') AS result;
    
    COMMIT;
END //
DELIMITER ;

-- 3.2 清理历史围栏事件
DELIMITER //
CREATE PROCEDURE `CleanOldFenceEvents`(IN days_to_keep INT)
BEGIN
    DECLARE exit handler for sqlexception
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;
    
    -- 删除指定天数之前的围栏事件（保留重要事件）
    DELETE FROM geo_fence_event 
    WHERE create_time < DATE_SUB(NOW(), INTERVAL days_to_keep DAY)
    AND processing_status = 'processed';
    
    -- 记录清理结果
    SELECT CONCAT('清理了 ', ROW_COUNT(), ' 条围栏事件记录') AS result;
    
    COMMIT;
END //
DELIMITER ;

-- 3.3 获取老人当前围栏状态
DELIMITER //
CREATE PROCEDURE `GetElderlyFenceStatus`(IN elderly_id_param INT)
BEGIN
    SELECT 
        gf.id as fence_id,
        gf.fence_name,
        gf.fence_type,
        gf.status as fence_status,
        CASE 
            WHEN gf.fence_type = 'circle' THEN
                CASE WHEN (
                    6371000 * ACOS(
                        COS(RADIANS(gf.center_lat)) * 
                        COS(RADIANS(ll.lat)) * 
                        COS(RADIANS(ll.lon) - RADIANS(gf.center_lon)) + 
                        SIN(RADIANS(gf.center_lat)) * 
                        SIN(RADIANS(ll.lat))
                    )
                ) <= gf.radius THEN 'inside' ELSE 'outside' END
            ELSE 'unknown'
        END as current_status,
        ll.lat as current_lat,
        ll.lon as current_lon,
        ll.gps_time as last_update_time
    FROM geo_fence gf
    CROSS JOIN (
        SELECT lat, lon, gps_time
        FROM gps_location 
        WHERE elderly_id = elderly_id_param 
        AND is_valid = 1
        ORDER BY gps_time DESC 
        LIMIT 1
    ) ll
    WHERE gf.elderly_id = elderly_id_param 
    AND gf.status = 1
    ORDER BY gf.priority DESC;
END //
DELIMITER ;

-- =====================================================
-- 4. 函数定义
-- =====================================================

-- 4.1 计算两点间距离（米）
DELIMITER //
CREATE FUNCTION `CalculateDistance`(
    lat1 DOUBLE, 
    lon1 DOUBLE, 
    lat2 DOUBLE, 
    lon2 DOUBLE
) RETURNS DOUBLE
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE distance DOUBLE;
    
    -- 使用Haversine公式计算距离
    SET distance = 6371000 * ACOS(
        COS(RADIANS(lat1)) * 
        COS(RADIANS(lat2)) * 
        COS(RADIANS(lon2) - RADIANS(lon1)) + 
        SIN(RADIANS(lat1)) * 
        SIN(RADIANS(lat2))
    );
    
    RETURN distance;
END //
DELIMITER ;

-- 4.2 检查点是否在圆形围栏内
DELIMITER //
CREATE FUNCTION `IsPointInCircleFence`(
    point_lat DOUBLE,
    point_lon DOUBLE,
    center_lat DOUBLE,
    center_lon DOUBLE,
    radius DOUBLE
) RETURNS BOOLEAN
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE distance DOUBLE;
    
    SET distance = CalculateDistance(point_lat, point_lon, center_lat, center_lon);
    
    RETURN distance <= radius;
END //
DELIMITER ;

-- =====================================================
-- 5. 触发器
-- =====================================================

-- 5.1 GPS数据插入时自动检查围栏
DELIMITER //
CREATE TRIGGER `tr_gps_location_fence_check`
AFTER INSERT ON `gps_location`
FOR EACH ROW
BEGIN
    -- 这里可以添加围栏检查逻辑
    -- 实际项目中建议在应用层处理，避免数据库触发器过于复杂
    NULL;
END //
DELIMITER ;

-- =====================================================
-- 6. 示例数据
-- =====================================================

-- 6.1 插入示例老人信息
INSERT INTO `elderly_profile` (`name`, `gender`, `birth_date`, `phone`, `emergency_contact`, `emergency_contact_name`, `address`) VALUES
('张奶奶', 2, '1945-03-15', '13800138001', '13900139001', '张小明', '北京市朝阳区建国路88号'),
('李爷爷', 1, '1940-08-20', '13800138002', '13900139002', '李小红', '上海市浦东新区陆家嘴路100号'),
('王奶奶', 2, '1950-12-05', '13800138003', '13900139003', '王小华', '广州市天河区珠江新城');

-- 6.2 插入示例设备信息
INSERT INTO `device_info` (`macid`, `device_name`, `device_type`, `elderly_id`, `sim_number`, `battery_level`, `signal_strength`) VALUES
('860123456789001', '张奶奶的GPS定位器', 'gps', 1, '13800138001', 85, 90),
('860123456789002', '李爷爷的智能手表', 'watch', 2, '13800138002', 72, 85),
('860123456789003', '王奶奶的GPS定位器', 'gps', 3, '13800138003', 90, 88);

-- 6.3 插入示例围栏配置
INSERT INTO `geo_fence` (
    `elderly_id`, `fence_name`, `fence_description`, `fence_type`, 
    `center_lat`, `center_lon`, `radius`, 
    `status`, `enter_alert`, `exit_alert`, `alert_type`, 
    `emergency_contacts`, `priority`, `create_by`
) VALUES 
(1, '家庭安全区域', '张奶奶家周围500米安全区域', 'circle', 
 39.9042, 116.4074, 500, 
 1, 0, 1, 'sms', 
 '13900139001,13800138001', 2, 'admin'),
(1, '社区活动中心', '社区老年活动中心区域', 'circle', 
 39.9100, 116.4100, 200, 
 1, 1, 1, 'both', 
 '13900139001', 1, 'admin'),
(2, '医院安全区域', '李爷爷常去的医院区域', 'circle', 
 31.2304, 121.4737, 300, 
 1, 0, 1, 'sms', 
 '13900139002', 3, 'admin');

-- 6.4 插入示例GPS数据
INSERT INTO `gps_location` (
    `macid`, `elderly_id`, `gps_time`, `lat`, `lon`, 
    `altitude`, `accuracy`, `speed`, `direction`, 
    `location_type`, `address`, `is_valid`
) VALUES 
('860123456789001', 1, UNIX_TIMESTAMP(NOW()) * 1000, 39.9042, 116.4074, 
 50.5, 5.0, 0.0, 0.0, 
 'gps', '北京市朝阳区建国路88号', 1),
('860123456789002', 2, UNIX_TIMESTAMP(NOW()) * 1000, 31.2304, 121.4737, 
 10.2, 3.0, 2.5, 45.0, 
 'gps', '上海市浦东新区陆家嘴路100号', 1),
('860123456789003', 3, UNIX_TIMESTAMP(NOW()) * 1000, 23.1291, 113.2644, 
 25.8, 4.0, 0.0, 0.0, 
 'gps', '广州市天河区珠江新城', 1);

-- =====================================================
-- 7. 性能优化索引
-- =====================================================

-- 复合索引优化查询性能
CREATE INDEX `idx_gps_location_time_elderly` ON `gps_location` (`elderly_id`, `gps_time` DESC, `is_valid`);
CREATE INDEX `idx_fence_event_time_status` ON `geo_fence_event` (`event_time` DESC, `processing_status`);
CREATE INDEX `idx_fence_elderly_priority` ON `geo_fence` (`elderly_id`, `status`, `priority` DESC);

-- =====================================================
-- 8. 外键约束（可选）
-- =====================================================

-- 根据实际需要启用外键约束
-- ALTER TABLE `device_info` ADD CONSTRAINT `fk_device_elderly` FOREIGN KEY (`elderly_id`) REFERENCES `elderly_profile` (`id`) ON DELETE SET NULL;
-- ALTER TABLE `gps_location` ADD CONSTRAINT `fk_gps_elderly` FOREIGN KEY (`elderly_id`) REFERENCES `elderly_profile` (`id`) ON DELETE SET NULL;
-- ALTER TABLE `geo_fence` ADD CONSTRAINT `fk_fence_elderly` FOREIGN KEY (`elderly_id`) REFERENCES `elderly_profile` (`id`) ON DELETE CASCADE;
-- ALTER TABLE `geo_fence_event` ADD CONSTRAINT `fk_event_elderly` FOREIGN KEY (`elderly_id`) REFERENCES `elderly_profile` (`id`) ON DELETE CASCADE;
-- ALTER TABLE `geo_fence_event` ADD CONSTRAINT `fk_event_fence` FOREIGN KEY (`fence_id`) REFERENCES `geo_fence` (`id`) ON DELETE CASCADE;
-- ALTER TABLE `fence_alert_rule` ADD CONSTRAINT `fk_rule_fence` FOREIGN KEY (`fence_id`) REFERENCES `geo_fence` (`id`) ON DELETE CASCADE;

SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- 使用说明
-- =====================================================
/*
1. 数据清理：
   - 清理30天前的GPS数据：CALL CleanOldGpsData(30);
   - 清理90天前的围栏事件：CALL CleanOldFenceEvents(90);

2. 查询示例：
   - 查看老人最新位置：SELECT * FROM v_elderly_latest_location;
   - 查看围栏事件统计：SELECT * FROM v_fence_event_statistics;
   - 查看活跃围栏：SELECT * FROM v_active_fences;
   - 获取老人围栏状态：CALL GetElderlyFenceStatus(1);

3. 距离计算：
   - 计算两点距离：SELECT CalculateDistance(39.9042, 116.4074, 39.9100, 116.4100);
   - 检查点是否在围栏内：SELECT IsPointInCircleFence(39.9042, 116.4074, 39.9100, 116.4100, 500);

4. 性能建议：
   - 定期清理历史数据
   - 根据查询模式调整索引
   - 监控数据库性能指标
   - 考虑分表分库策略（数据量大时）
*/

COMMIT;