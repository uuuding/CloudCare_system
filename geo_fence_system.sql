-- 老人防丢电子围栏系统数据库表结构

-- 1. 设备绑定表
CREATE TABLE IF NOT EXISTS `device_binding` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `macid` VARCHAR(50) NOT NULL COMMENT '设备编号（IMEI）',
    `elderly_id` INT NOT NULL COMMENT '老人ID',
    `bind_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
    `unbind_time` DATETIME NULL COMMENT '解绑时间',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '绑定状态：1-已绑定，0-已解绑',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` VARCHAR(50) NULL COMMENT '创建者',
    `update_by` VARCHAR(50) NULL COMMENT '更新者',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_macid_elderly_active` (`macid`, `elderly_id`, `status`),
    INDEX `idx_macid` (`macid`),
    INDEX `idx_elderly_id` (`elderly_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_bind_time` (`bind_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备绑定表';

-- 2. GPS定位数据表
CREATE TABLE IF NOT EXISTS `gps_location` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `macid` VARCHAR(50) NOT NULL COMMENT '设备编号（IMEI）',
    `elderly_id` INT NULL COMMENT '老人ID',
    `gps_time` BIGINT NULL COMMENT 'GPS时间戳（毫秒）',
    `heart_time` BIGINT NULL COMMENT '信号时间',
    `upd_time` BIGINT NULL COMMENT '最后更新位置时间',
    `lat` DOUBLE NULL COMMENT 'GPS纬度',
    `lon` DOUBLE NULL COMMENT 'GPS经度',
    `speed` DOUBLE NULL COMMENT '速度（单位：km/h）',
    `dir` DOUBLE NULL COMMENT '方向角度',
    `stats` VARCHAR(500) NULL COMMENT '状态字段，逗号分隔',
    `value` VARCHAR(500) NULL COMMENT '状态值字段，逗号分隔',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_macid` (`macid`),
    INDEX `idx_elderly_id` (`elderly_id`),
    INDEX `idx_gps_time` (`gps_time`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='GPS定位数据表';

-- 3. 电子围栏配置表
CREATE TABLE IF NOT EXISTS `geo_fence` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `elderly_id` INT NOT NULL COMMENT '老人ID',
    `fence_name` VARCHAR(100) NOT NULL COMMENT '围栏名称',
    `fence_type` VARCHAR(20) NOT NULL COMMENT '围栏类型：circle-圆形，polygon-多边形',
    `center_lat` DOUBLE NULL COMMENT '围栏中心点纬度（圆形围栏使用）',
    `center_lon` DOUBLE NULL COMMENT '围栏中心点经度（圆形围栏使用）',
    `radius` DOUBLE NULL COMMENT '围栏半径（米，圆形围栏使用）',
    `coordinates` TEXT NULL COMMENT '围栏坐标点（JSON格式，多边形围栏使用）',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '围栏状态：1-启用，0-禁用',
    `enter_alert` TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用进入提醒：1-启用，0-禁用',
    `exit_alert` TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用离开提醒：1-启用，0-禁用',
    `alert_type` VARCHAR(20) NOT NULL DEFAULT 'sms' COMMENT '提醒方式：sms-短信，app-应用推送，both-两者都有',
    `emergency_contacts` VARCHAR(500) NULL COMMENT '紧急联系人手机号（多个用逗号分隔）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` VARCHAR(50) NULL COMMENT '创建者',
    `update_by` VARCHAR(50) NULL COMMENT '更新者',
    PRIMARY KEY (`id`),
    INDEX `idx_elderly_id` (`elderly_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='电子围栏配置表';

-- 4. 电子围栏事件记录表
CREATE TABLE IF NOT EXISTS `geo_fence_event` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `elderly_id` INT NOT NULL COMMENT '老人ID',
    `fence_id` BIGINT NOT NULL COMMENT '围栏ID',
    `fence_name` VARCHAR(100) NOT NULL COMMENT '围栏名称',
    `event_type` VARCHAR(20) NOT NULL COMMENT '事件类型：enter-进入，exit-离开',
    `lat` DOUBLE NOT NULL COMMENT '事件发生时的纬度',
    `lon` DOUBLE NOT NULL COMMENT '事件发生时的经度',
    `location_id` BIGINT NULL COMMENT 'GPS定位记录ID',
    `macid` VARCHAR(50) NULL COMMENT '设备编号（IMEI）',
    `event_time` DATETIME NOT NULL COMMENT '事件发生时间',
    `alert_sent` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已发送提醒：1-已发送，0-未发送',
    `alert_sent_time` DATETIME NULL COMMENT '提醒发送时间',
    `alert_type` VARCHAR(20) NULL COMMENT '提醒方式',
    `alert_content` TEXT NULL COMMENT '提醒内容',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX `idx_elderly_id` (`elderly_id`),
    INDEX `idx_fence_id` (`fence_id`),
    INDEX `idx_event_type` (`event_type`),
    INDEX `idx_event_time` (`event_time`),
    INDEX `idx_alert_sent` (`alert_sent`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='电子围栏事件记录表';

-- 插入示例数据

-- 示例设备绑定数据
INSERT INTO `device_binding` (
    `macid`, `elderly_id`, `status`, `create_by`
) VALUES 
    ('TEST123456789', 1, 1, 'admin'),
    ('TEST987654321', 2, 1, 'admin');

-- 示例围栏配置（圆形围栏）
INSERT INTO `geo_fence` (
    `elderly_id`, `fence_name`, `fence_type`, `center_lat`, `center_lon`, `radius`, 
    `status`, `enter_alert`, `exit_alert`, `alert_type`, `emergency_contacts`, `create_by`
) VALUES (
    1, '家庭安全区域', 'circle', 22.69937, 114.12503, 500, 
    1, 0, 1, 'sms', '13800138000,13900139000', 'admin'
);

-- 示例围栏配置（多边形围栏）
INSERT INTO `geo_fence` (
    `elderly_id`, `fence_name`, `fence_type`, `coordinates`, 
    `status`, `enter_alert`, `exit_alert`, `alert_type`, `emergency_contacts`, `create_by`
) VALUES (
    1, '社区活动区域', 'polygon', '[{"lat":22.699,"lon":114.125},{"lat":22.700,"lon":114.125},{"lat":22.700,"lon":114.126},{"lat":22.699,"lon":114.126}]', 
    1, 1, 1, 'both', '13800138000', 'admin'
);

-- 创建索引优化查询性能
CREATE INDEX `idx_gps_location_composite` ON `gps_location` (`elderly_id`, `gps_time` DESC);
CREATE INDEX `idx_geo_fence_elderly_status` ON `geo_fence` (`elderly_id`, `status`);
CREATE INDEX `idx_geo_fence_event_composite` ON `geo_fence_event` (`elderly_id`, `event_time` DESC);

-- 添加外键约束（可选，根据实际情况决定是否启用）
-- ALTER TABLE `gps_location` ADD CONSTRAINT `fk_gps_location_elderly` FOREIGN KEY (`elderly_id`) REFERENCES `elderly_profile` (`id`) ON DELETE SET NULL;
-- ALTER TABLE `geo_fence` ADD CONSTRAINT `fk_geo_fence_elderly` FOREIGN KEY (`elderly_id`) REFERENCES `elderly_profile` (`id`) ON DELETE CASCADE;
-- ALTER TABLE `geo_fence_event` ADD CONSTRAINT `fk_geo_fence_event_elderly` FOREIGN KEY (`elderly_id`) REFERENCES `elderly_profile` (`id`) ON DELETE CASCADE;
-- ALTER TABLE `geo_fence_event` ADD CONSTRAINT `fk_geo_fence_event_fence` FOREIGN KEY (`fence_id`) REFERENCES `geo_fence` (`id`) ON DELETE CASCADE;
-- ALTER TABLE `geo_fence_event` ADD CONSTRAINT `fk_geo_fence_event_location` FOREIGN KEY (`location_id`) REFERENCES `gps_location` (`id`) ON DELETE SET NULL;

-- 创建数据清理存储过程（定期清理旧的GPS数据）
DELIMITER //
CREATE PROCEDURE CleanOldGpsData(IN days_to_keep INT)
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
    
    -- 记录清理日志
    SELECT CONCAT('清理了 ', ROW_COUNT(), ' 条GPS定位记录') AS result;
    
    COMMIT;
END //
DELIMITER ;

-- 使用示例：清理30天前的GPS数据
-- CALL CleanOldGpsData(30);

-- 创建视图：围栏事件统计
CREATE VIEW `v_fence_event_statistics` AS
SELECT 
    elderly_id,
    fence_id,
    fence_name,
    event_type,
    DATE(event_time) as event_date,
    COUNT(*) as event_count
FROM geo_fence_event
GROUP BY elderly_id, fence_id, fence_name, event_type, DATE(event_time)
ORDER BY event_date DESC, elderly_id, fence_id;

-- 创建视图：老人最新位置
CREATE VIEW `v_elderly_latest_location` AS
SELECT 
    gl1.elderly_id,
    gl1.macid,
    gl1.lat,
    gl1.lon,
    gl1.speed,
    gl1.dir,
    gl1.gps_time,
    gl1.create_time
FROM gps_location gl1
INNER JOIN (
    SELECT elderly_id, MAX(gps_time) as max_gps_time
    FROM gps_location
    WHERE elderly_id IS NOT NULL
    GROUP BY elderly_id
) gl2 ON gl1.elderly_id = gl2.elderly_id AND gl1.gps_time = gl2.max_gps_time;

COMMIT;