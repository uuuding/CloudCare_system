-- 创建短信发送记录表
CREATE TABLE IF NOT EXISTS `sms_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `phone` varchar(20) NOT NULL COMMENT '手机号',
  `content` text NOT NULL COMMENT '短信内容',
  `type` varchar(20) NOT NULL COMMENT '短信类型：normal-普通短信，template-模板短信，batch-批量短信',
  `template_code` varchar(50) DEFAULT NULL COMMENT '模板代码',
  `template_name` varchar(100) DEFAULT NULL COMMENT '模板名称',
  `status` varchar(20) NOT NULL COMMENT '发送状态：success-成功，failed-失败，sending-发送中',
  `send_time` datetime NOT NULL COMMENT '发送时间',
  `response_code` varchar(20) DEFAULT NULL COMMENT '响应代码',
  `error_message` varchar(500) DEFAULT NULL COMMENT '错误信息',
  `params` text DEFAULT NULL COMMENT '模板参数（JSON格式）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_phone` (`phone`),
  KEY `idx_status` (`status`),
  KEY `idx_type` (`type`),
  KEY `idx_send_time` (`send_time`),
  KEY `idx_template_code` (`template_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短信发送记录表';

-- 插入一些示例数据
INSERT INTO `sms_record` (`phone`, `content`, `type`, `template_code`, `template_name`, `status`, `send_time`, `response_code`, `error_message`, `params`) VALUES
('13800138001', '【云护CloudCare平台】感谢您的注册，欢迎来到云护CloudCare智慧系统。', 'template', 'REGISTER_WELCOME', '注册欢迎', 'success', '2024-01-15 10:30:00', '0', NULL, NULL),
('13800138002', '【云护CloudCare平台】安全提醒：您的账户张三在2024-01-15 14:20有新的登录记录，登录地点：北京市朝阳区。如非本人操作请及时修改密码。', 'template', 'LOGIN_SECURITY', '登录安全提醒', 'success', '2024-01-15 14:20:15', '0', NULL, '{"username":"张三","loginTime":"2024-01-15 14:20","location":"北京市朝阳区"}'),
('13800138003', '这是一条测试短信，用于验证短信发送功能是否正常工作。', 'normal', NULL, NULL, 'failed', '2024-01-15 15:45:30', '-1', '手机号码格式错误', NULL),
('13800138004,13800138005,13800138006', '【云护CloudCare平台】系统维护通知：系统将于今晚22:00-24:00进行维护，期间可能影响正常使用，请提前做好准备。', 'batch', NULL, NULL, 'success', '2024-01-15 16:00:00', '0', NULL, NULL),
('13800138007', '【云护CloudCare平台】就诊提醒：王奶奶明天09:00在市人民医院心内科有预约，请提前30分钟到达。如需改期请及时联系。', 'template', 'APPOINTMENT_REMINDER', '就诊提醒', 'sending', '2024-01-15 17:30:00', NULL, NULL, '{"elderlyName":"王奶奶","appointmentTime":"09:00","hospitalName":"市人民医院","department":"心内科"}'),
('13800138008', '【云护CloudCare平台】您的验证码是123456，5分钟内有效。', 'template', 'VERIFICATION_CODE', '验证码', 'success', NOW(), '0', NULL, '{"code":"123456"}'),
('13800138009', '【云护CloudCare平台】设备提醒：老人李爷爷的血压计(BP001)出现故障，请及时检查或联系维修。', 'template', 'DEVICE_ALERT', '设备提醒', 'success', NOW(), '0', NULL, '{"elderlyName":"李爷爷","deviceName":"血压计","deviceCode":"BP001"}'),
('13800138010', '【云护CloudCare平台】紧急提醒：您关注的老人赵奶奶出现心率异常，心率过快。请及时关注并联系医护人员。', 'template', 'EMERGENCY_ALERT', '紧急提醒', 'success', NOW(), '0', NULL, '{"elderlyName":"赵奶奶","alertType":"心率异常","alertDescription":"心率过快"}');