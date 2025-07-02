-- GPS设备表
CREATE TABLE IF NOT EXISTS `gps_device` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '设备ID',
  `device_name` varchar(100) NOT NULL COMMENT '设备名称',
  `imei` varchar(20) NOT NULL COMMENT 'IMEI号',
  `elderly_id` bigint DEFAULT NULL COMMENT '绑定的老人ID',
  `device_status` tinyint DEFAULT '1' COMMENT '设备状态：0-离线，1-在线，2-故障',
  `last_gps_time` datetime DEFAULT NULL COMMENT '最后GPS时间',
  `last_heartbeat_time` datetime DEFAULT NULL COMMENT '最后心跳时间',
  `remarks` text COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_imei` (`imei`),
  KEY `idx_elderly_id` (`elderly_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='GPS设备表';

-- GPS位置记录表
CREATE TABLE IF NOT EXISTS `gps_location` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '位置记录ID',
  `device_imei` varchar(20) NOT NULL COMMENT '设备IMEI',
  `latitude` decimal(10,6) NOT NULL COMMENT '纬度',
  `longitude` decimal(10,6) NOT NULL COMMENT '经度',
  `map_latitude` decimal(10,6) DEFAULT NULL COMMENT '地图纬度（转换后）',
  `map_longitude` decimal(10,6) DEFAULT NULL COMMENT '地图经度（转换后）',
  `speed` decimal(5,2) DEFAULT '0.00' COMMENT '速度（km/h）',
  `direction` decimal(5,2) DEFAULT '0.00' COMMENT '方向角度',
  `location_status` tinyint DEFAULT '1' COMMENT '定位状态：0-无效，1-有效',
  `location_time` datetime NOT NULL COMMENT '定位时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_device_imei` (`device_imei`),
  KEY `idx_location_time` (`location_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='GPS位置记录表';

-- 电子围栏表
CREATE TABLE IF NOT EXISTS `geo_fence` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '围栏ID',
  `fence_name` varchar(100) NOT NULL COMMENT '围栏名称',
  `elderly_id` bigint NOT NULL COMMENT '关联老人ID',
  `fence_type` tinyint NOT NULL COMMENT '围栏类型：1-圆形，2-多边形',
  `alarm_type` tinyint NOT NULL COMMENT '报警类型：1-进入报警，2-离开报警',
  `center_latitude` decimal(10,6) DEFAULT NULL COMMENT '中心点纬度（圆形围栏）',
  `center_longitude` decimal(10,6) DEFAULT NULL COMMENT '中心点经度（圆形围栏）',
  `radius` decimal(8,2) DEFAULT NULL COMMENT '半径（米，圆形围栏）',
  `polygon_points` text DEFAULT NULL COMMENT '多边形坐标点（JSON格式）',
  `effective_start_time` time DEFAULT NULL COMMENT '生效开始时间',
  `effective_end_time` time DEFAULT NULL COMMENT '生效结束时间',
  `fence_status` tinyint DEFAULT '1' COMMENT '围栏状态：0-禁用，1-启用',
  `remarks` text COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_elderly_id` (`elderly_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电子围栏表';

-- GPS报警记录表
CREATE TABLE IF NOT EXISTS `gps_alarm` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '报警记录ID',
  `alarm_id` varchar(50) DEFAULT NULL COMMENT '报警ID（来自设备）',
  `device_imei` varchar(20) NOT NULL COMMENT '设备IMEI',
  `device_name` varchar(100) DEFAULT NULL COMMENT '设备名称',
  `alarm_time` datetime NOT NULL COMMENT '报警时间',
  `latitude` decimal(10,6) NOT NULL COMMENT '报警位置纬度',
  `longitude` decimal(10,6) NOT NULL COMMENT '报警位置经度',
  `map_latitude` decimal(10,6) DEFAULT NULL COMMENT '地图纬度（转换后）',
  `map_longitude` decimal(10,6) DEFAULT NULL COMMENT '地图经度（转换后）',
  `speed` decimal(5,2) DEFAULT '0.00' COMMENT '速度（km/h）',
  `direction` int DEFAULT '0' COMMENT '方向角度',
  `alarm_type` tinyint NOT NULL COMMENT '报警类型：1-SOS，2-低电量，3-围栏，4-震动，5-移动',
  `description` varchar(500) DEFAULT NULL COMMENT '报警描述',
  `notes` text DEFAULT NULL COMMENT '备注信息',
  `process_status` tinyint DEFAULT '0' COMMENT '处理状态：0-未处理，1-已处理',
  `process_time` datetime DEFAULT NULL COMMENT '处理时间',
  `process_result` varchar(500) DEFAULT NULL COMMENT '处理结果',
  `process_notes` text DEFAULT NULL COMMENT '处理说明',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_device_imei` (`device_imei`),
  KEY `idx_alarm_time` (`alarm_time`),
  KEY `idx_alarm_type` (`alarm_type`),
  KEY `idx_process_status` (`process_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='GPS报警记录表';

-- 插入示例数据

-- 插入GPS设备示例数据
INSERT INTO `gps_device` (`device_name`, `imei`, `elderly_id`, `device_status`, `last_gps_time`, `last_heartbeat_time`, `remarks`) VALUES
('老人定位器001', '860123456789012', 1, 1, '2024-01-15 10:30:00', '2024-01-15 10:30:00', '张大爷的定位设备'),
('老人定位器002', '860123456789013', 2, 1, '2024-01-15 10:25:00', '2024-01-15 10:25:00', '李奶奶的定位设备'),
('老人定位器003', '860123456789014', 3, 1, '2024-01-15 10:20:00', '2024-01-15 10:20:00', '王爷爷的定位设备'),
('老人定位器004', '860123456789015', NULL, 0, '2024-01-14 15:20:00', '2024-01-14 15:20:00', '备用设备'),
('老人定位器005', '860123456789016', NULL, 0, NULL, NULL, '新设备待分配');

-- 插入GPS位置记录示例数据
INSERT INTO `gps_location` (`device_imei`, `latitude`, `longitude`, `map_latitude`, `map_longitude`, `speed`, `direction`, `location_status`, `location_time`) VALUES
-- 张大爷的位置记录
('860123456789012', 39.908823, 116.397470, 39.908823, 116.397470, 0.00, 0.00, 1, '2024-01-15 10:30:00'),
('860123456789012', 39.908920, 116.397580, 39.908920, 116.397580, 5.20, 45.00, 1, '2024-01-15 10:25:00'),
('860123456789012', 39.908750, 116.397300, 39.908750, 116.397300, 3.80, 180.00, 1, '2024-01-15 10:20:00'),
('860123456789012', 39.908650, 116.397200, 39.908650, 116.397200, 0.00, 0.00, 1, '2024-01-15 10:15:00'),
-- 李奶奶的位置记录
('860123456789013', 39.909123, 116.398470, 39.909123, 116.398470, 0.00, 0.00, 1, '2024-01-15 10:25:00'),
('860123456789013', 39.909200, 116.398600, 39.909200, 116.398600, 3.50, 90.00, 1, '2024-01-15 10:20:00'),
('860123456789013', 39.909050, 116.398350, 39.909050, 116.398350, 2.10, 270.00, 1, '2024-01-15 10:15:00'),
('860123456789013', 39.909000, 116.398250, 39.909000, 116.398250, 0.00, 0.00, 1, '2024-01-15 10:10:00'),
-- 王爷爷的位置记录
('860123456789014', 39.910500, 116.399800, 39.910500, 116.399800, 0.00, 0.00, 1, '2024-01-15 10:20:00'),
('860123456789014', 39.910400, 116.399700, 39.910400, 116.399700, 4.20, 225.00, 1, '2024-01-15 10:15:00'),
('860123456789014', 39.910300, 116.399600, 39.910300, 116.399600, 6.50, 180.00, 1, '2024-01-15 10:10:00'),
('860123456789014', 39.910200, 116.399500, 39.910200, 116.399500, 2.80, 135.00, 1, '2024-01-15 10:05:00');

-- 插入电子围栏示例数据
INSERT INTO `geo_fence` (`fence_name`, `elderly_id`, `fence_type`, `alarm_type`, `center_latitude`, `center_longitude`, `radius`, `effective_start_time`, `effective_end_time`, `fence_status`, `remarks`) VALUES
('张大爷家周边安全区域', 1, 1, 2, 39.908823, 116.397470, 500.00, '06:00:00', '22:00:00', 1, '家周边500米安全范围'),
('李奶奶小区围栏', 2, 1, 2, 39.909123, 116.398470, 300.00, '00:00:00', '23:59:59', 1, '小区内活动范围'),
('王爷爷公园活动区域', 3, 1, 2, 39.910500, 116.399800, 800.00, '07:00:00', '19:00:00', 1, '公园内活动范围'),
('张大爷医院围栏', 1, 1, 1, 39.912000, 116.401000, 200.00, '08:00:00', '18:00:00', 0, '医院就诊提醒区域'),
('李奶奶超市围栏', 2, 2, 1, NULL, NULL, NULL, '09:00:00', '21:00:00', 1, '超市购物提醒区域');

-- 为多边形围栏添加坐标点
UPDATE `geo_fence` SET `polygon_points` = '[{"lat":39.908500,"lng":116.398000},{"lat":39.908800,"lng":116.398500},{"lat":39.909200,"lng":116.398300},{"lat":39.909000,"lng":116.397800},{"lat":39.908500,"lng":116.398000}]' WHERE `fence_name` = '李奶奶超市围栏';

-- 插入GPS报警记录示例数据
INSERT INTO `gps_alarm` (`alarm_id`, `device_imei`, `device_name`, `alarm_time`, `latitude`, `longitude`, `map_latitude`, `map_longitude`, `speed`, `direction`, `alarm_type`, `description`, `notes`, `process_status`, `process_time`, `process_result`, `process_notes`) VALUES
('ALARM001', '860123456789012', '老人定位器001', '2024-01-15 09:30:00', 39.908823, 116.397470, 39.908823, 116.397470, 0.00, 0, 1, 'SOS紧急求助', '老人按下SOS按钮', 1, '2024-01-15 09:35:00', '已联系家属', '家属已到达现场，老人安全'),
('ALARM002', '860123456789013', '老人定位器002', '2024-01-15 08:45:00', 39.909123, 116.398470, 39.909123, 116.398470, 0.00, 0, 2, '设备电量不足', '电量低于20%', 0, NULL, NULL, NULL),
('ALARM003', '860123456789012', '老人定位器001', '2024-01-14 16:20:00', 39.910000, 116.400000, 39.910000, 116.400000, 8.50, 180, 3, '离开安全区域', '超出围栏范围', 1, '2024-01-14 16:25:00', '已确认安全', '老人去附近药店买药，已安全返回'),
('ALARM004', '860123456789014', '老人定位器003', '2024-01-15 07:15:00', 39.910500, 116.399800, 39.910500, 116.399800, 0.00, 0, 4, '设备震动报警', '检测到异常震动', 0, NULL, NULL, NULL),
('ALARM005', '860123456789013', '老人定位器002', '2024-01-14 14:30:00', 39.909500, 116.399000, 39.909500, 116.399000, 12.00, 90, 5, '移动速度异常', '移动速度超过设定阈值', 1, '2024-01-14 14:35:00', '确认乘坐交通工具', '老人乘坐公交车，属于正常出行'),
('ALARM006', '860123456789012', '老人定位器001', '2024-01-13 11:20:00', 39.908000, 116.396000, 39.908000, 116.396000, 0.00, 0, 1, 'SOS紧急求助', '老人按下SOS按钮', 1, '2024-01-13 11:25:00', '误触发', '老人误按SOS按钮，已确认安全'),
('ALARM007', '860123456789014', '老人定位器003', '2024-01-12 19:45:00', 39.911000, 116.400500, 39.911000, 116.400500, 0.00, 0, 3, '离开安全区域', '超出围栏范围', 1, '2024-01-12 19:50:00', '已联系老人', '老人在朋友家做客，已确认安全'),
('ALARM008', '860123456789013', '老人定位器002', '2024-01-11 06:30:00', 39.909123, 116.398470, 39.909123, 116.398470, 0.00, 0, 2, '设备电量不足', '电量低于15%', 1, '2024-01-11 08:00:00', '已充电', '提醒老人及时充电，设备已恢复正常');