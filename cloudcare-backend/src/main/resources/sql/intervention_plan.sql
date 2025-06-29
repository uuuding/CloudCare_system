-- 干预方案表
CREATE TABLE IF NOT EXISTS intervention_plan (
    plan_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '干预方案ID',
    elderly_id INT NOT NULL COMMENT '老人ID',
    elderly_name VARCHAR(50) NOT NULL COMMENT '老人姓名',
    alert_id BIGINT COMMENT '关联的健康预警ID',
    plan_title VARCHAR(200) NOT NULL COMMENT '干预方案标题',
    plan_type VARCHAR(50) NOT NULL COMMENT '干预类型：MEDICATION(用药), EXERCISE(运动), DIET(饮食), LIFESTYLE(生活方式), MONITORING(监测), MEDICAL(医疗)',
    priority_level VARCHAR(20) NOT NULL COMMENT '优先级：LOW(低), MEDIUM(中), HIGH(高), URGENT(紧急)',
    target_indicators TEXT COMMENT '目标指标（JSON格式）',
    intervention_content TEXT NOT NULL COMMENT '干预内容详情',
    implementation_method TEXT COMMENT '实施方法',
    frequency VARCHAR(100) COMMENT '执行频率',
    duration_days INT COMMENT '持续天数',
    responsible_person VARCHAR(100) COMMENT '负责人',
    contact_info VARCHAR(200) COMMENT '联系方式',
    start_date DATE COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态：PENDING(待执行), ACTIVE(执行中), COMPLETED(已完成), PAUSED(暂停), CANCELLED(已取消)',
    progress_rate DECIMAL(5,2) DEFAULT 0.00 COMMENT '完成进度百分比',
    effectiveness_score DECIMAL(3,1) COMMENT '效果评分(1-10)',
    notes TEXT COMMENT '备注',
    created_by VARCHAR(100) COMMENT '创建人',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by VARCHAR(100) COMMENT '更新人',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_elderly_id (elderly_id),
    INDEX idx_alert_id (alert_id),
    INDEX idx_status (status),
    INDEX idx_priority (priority_level),
    INDEX idx_plan_type (plan_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='干预方案表';

-- 干预执行记录表
CREATE TABLE IF NOT EXISTS intervention_execution (
    execution_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '执行记录ID',
    plan_id BIGINT NOT NULL COMMENT '干预方案ID',
    execution_date DATE NOT NULL COMMENT '执行日期',
    execution_time TIME COMMENT '执行时间',
    execution_status VARCHAR(20) NOT NULL COMMENT '执行状态：COMPLETED(已完成), PARTIAL(部分完成), MISSED(未执行)',
    completion_rate DECIMAL(5,2) DEFAULT 0.00 COMMENT '完成率',
    execution_details TEXT COMMENT '执行详情',
    side_effects TEXT COMMENT '副作用或不良反应',
    patient_feedback TEXT COMMENT '患者反馈',
    executor_name VARCHAR(100) COMMENT '执行人姓名',
    execution_location VARCHAR(200) COMMENT '执行地点',
    vital_signs_before TEXT COMMENT '执行前生命体征（JSON格式）',
    vital_signs_after TEXT COMMENT '执行后生命体征（JSON格式）',
    effectiveness_rating INT COMMENT '效果评价(1-5)',
    notes TEXT COMMENT '备注',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_plan_id (plan_id),
    INDEX idx_execution_date (execution_date),
    INDEX idx_execution_status (execution_status),
    FOREIGN KEY (plan_id) REFERENCES intervention_plan(plan_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='干预执行记录表';

-- 干预模板表
CREATE TABLE IF NOT EXISTS intervention_template (
    template_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '模板ID',
    template_name VARCHAR(200) NOT NULL COMMENT '模板名称',
    template_category VARCHAR(50) NOT NULL COMMENT '模板分类',
    alert_type VARCHAR(50) COMMENT '适用的预警类型',
    priority_level VARCHAR(20) COMMENT '建议优先级',
    template_content TEXT NOT NULL COMMENT '模板内容',
    implementation_guide TEXT COMMENT '实施指南',
    precautions TEXT COMMENT '注意事项',
    expected_outcomes TEXT COMMENT '预期效果',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    usage_count INT DEFAULT 0 COMMENT '使用次数',
    created_by VARCHAR(100) COMMENT '创建人',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_category (template_category),
    INDEX idx_alert_type (alert_type),
    INDEX idx_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='干预模板表';

-- 插入干预模板数据
INSERT INTO intervention_template (template_name, template_category, alert_type, priority_level, template_content, implementation_guide, precautions, expected_outcomes, usage_count) VALUES
('高血压饮食干预', 'DIET', 'BLOOD_PRESSURE_SYSTOLIC', 'HIGH', '低盐低脂饮食，每日盐摄入量控制在6g以下，增加蔬菜水果摄入', '1. 制定详细饮食计划\n2. 定期营养咨询\n3. 家属配合监督', '避免高钠食物，注意血糖变化', '血压下降5-10mmHg，改善心血管健康', 0),
('心率异常监测方案', 'MONITORING', 'HEART_RATE', 'HIGH', '24小时心率监测，每日记录心率变化，异常时及时就医', '1. 佩戴心率监测设备\n2. 定时记录数据\n3. 建立预警机制', '避免剧烈运动，注意休息', '心率稳定在正常范围，减少心律不齐发生', 1),
('体温异常护理', 'MEDICAL', 'TEMPERATURE', 'URGENT', '密切监测体温变化，物理降温，必要时药物治疗', '1. 每2小时测量体温\n2. 物理降温措施\n3. 及时医疗干预', '注意水电解质平衡，防止脱水', '体温恢复正常，避免并发症', 1),
('睡眠质量改善计划', 'LIFESTYLE', 'SLEEP', 'MEDIUM', '建立规律作息，改善睡眠环境，必要时心理疏导', '1. 制定作息时间表\n2. 优化睡眠环境\n3. 放松训练', '避免睡前刺激性活动，注意安全', '睡眠时间达到7-8小时，睡眠质量提升', 0),
('BMI控制运动方案', 'EXERCISE', 'BMI', 'MEDIUM', '制定个性化运动计划，循序渐进增加运动量', '1. 评估身体状况\n2. 制定运动计划\n3. 定期调整强度', '注意运动安全，避免过度疲劳', 'BMI控制在正常范围，提高身体素质', 1);

-- 更新现有数据的使用次数（如果表中已有数据）
UPDATE intervention_template SET usage_count = 0 WHERE usage_count IS NULL;

-- 插入示例干预方案数据
INSERT INTO intervention_plan (elderly_id, elderly_name, alert_id, plan_title, plan_type, priority_level, target_indicators, intervention_content, implementation_method, frequency, duration_days, responsible_person, contact_info, start_date, end_date, status, progress_rate, notes, created_by) VALUES
(1, '张三', 1, '高血压综合干预方案', 'MEDICATION', 'HIGH', '{"systolic_bp": "<140", "diastolic_bp": "<90"}', '1. 按时服用降压药物\n2. 低盐饮食控制\n3. 适量有氧运动\n4. 定期血压监测', '药物治疗+生活方式干预', '每日3次', 30, '李医生', '13800138001', '2024-01-15', '2024-02-14', 'ACTIVE', 65.50, '患者配合度良好', '系统管理员'),
(2, '李四', 2, '心率异常监测计划', 'MONITORING', 'MEDIUM', '{"heart_rate": "60-100"}', '24小时心电监测，记录心率变化趋势，异常时及时报警', '持续监测+定期评估', '持续监测', 14, '王护士', '13900139002', '2024-01-16', '2024-01-30', 'ACTIVE', 42.30, '监测数据正常', '系统管理员'),
(3, '王五', NULL, '睡眠质量改善方案', 'LIFESTYLE', 'LOW', '{"sleep_hours": "7-8"}', '建立规律作息时间，改善睡眠环境，睡前放松训练', '行为干预+环境调整', '每日执行', 21, '陈护士', '13700137003', '2024-01-17', '2024-02-07', 'PENDING', 0.00, '等待开始执行', '系统管理员');

-- 插入执行记录数据
INSERT INTO intervention_execution (plan_id, execution_date, execution_time, execution_status, completion_rate, execution_details, patient_feedback, executor_name, execution_location, effectiveness_rating, notes) VALUES
(1, '2024-01-15', '08:00:00', 'COMPLETED', 100.00, '按时服药，血压测量140/85mmHg', '感觉良好，无不适', '李医生', '老人房间', 4, '血压有所下降'),
(1, '2024-01-16', '08:00:00', 'COMPLETED', 100.00, '按时服药，血压测量138/82mmHg', '状态稳定', '李医生', '老人房间', 4, '血压持续改善'),
(2, '2024-01-16', '00:00:00', 'COMPLETED', 100.00, '24小时监测完成，平均心率78次/分', '佩戴舒适，无异常感觉', '王护士', '监护室', 5, '心率数据正常'),
(3, '2024-01-17', '22:00:00', 'PARTIAL', 70.00, '按时就寝，但入睡困难', '需要更多时间适应', '陈护士', '老人房间', 3, '需要调整方案');