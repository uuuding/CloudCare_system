-- 家属互动相关表结构

-- 创建家属成员表
CREATE TABLE IF NOT EXISTS `family_member` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `elder_id` BIGINT NOT NULL COMMENT '老人ID',
    `user_id` BIGINT NOT NULL COMMENT '家属用户ID',
    `relation` VARCHAR(50) NOT NULL COMMENT '与老人的关系',
    `contact_phone` VARCHAR(20) COMMENT '联系电话',
    `contact_email` VARCHAR(100) COMMENT '联系邮箱',
    `emergency_contact` TINYINT(1) DEFAULT 0 COMMENT '是否为紧急联系人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_elder_id` (`elder_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家属成员表';

-- 创建家属互动表
CREATE TABLE IF NOT EXISTS `family_interaction` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `elder_id` BIGINT NOT NULL COMMENT '老人ID',
    `family_member_id` BIGINT NOT NULL COMMENT '家属ID',
    `interaction_type` VARCHAR(20) NOT NULL COMMENT '互动类型：phone-电话沟通，video-视频通话，sms-短信交流，wechat-微信聊天，email-邮件往来，face_to_face-面对面交流，other-其他',
    `content` TEXT NOT NULL COMMENT '互动内容',
    `interaction_time` DATETIME NOT NULL COMMENT '互动时间',
    `reply_content` TEXT COMMENT '回复内容',
    `reply_time` DATETIME COMMENT '回复时间',
    `status` VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '状态：pending-待回复，replied-已回复，closed-已关闭',
    `remarks` TEXT COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_elder_id` (`elder_id`),
    KEY `idx_family_member_id` (`family_member_id`),
    KEY `idx_interaction_time` (`interaction_time`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家属互动表';

-- 插入测试数据

-- 插入家属成员测试数据
INSERT INTO `family_member` (`elder_id`, `user_id`, `relation`, `contact_phone`, `contact_email`, `emergency_contact`) VALUES
(1, 2, '儿子', '13800138001', 'son@example.com', 1),
(1, 3, '女儿', '13800138002', 'daughter@example.com', 0),
(2, 4, '儿子', '13800138003', 'son2@example.com', 1),
(2, 5, '儿媳', '13800138004', 'daughter_in_law@example.com', 0),
(3, 6, '女儿', '13800138005', 'daughter2@example.com', 1);

-- 插入家属互动测试数据
INSERT INTO `family_interaction` (`elder_id`, `family_member_id`, `interaction_type`, `content`, `interaction_time`, `reply_content`, `reply_time`, `status`, `remarks`) VALUES
(1, 1, 'phone', '询问老人身体状况，关心日常生活', '2024-01-15 10:30:00', '老人身体状况良好，饮食正常，精神状态佳', '2024-01-15 11:00:00', 'replied', '定期关怀电话'),
(1, 2, 'video', '视频通话，看望老人并了解近况', '2024-01-16 14:20:00', '已安排视频通话，老人很开心见到家人', '2024-01-16 14:30:00', 'replied', '每周定期视频'),
(2, 3, 'wechat', '通过微信询问老人的用药情况', '2024-01-17 09:15:00', NULL, NULL, 'pending', '需要及时回复'),
(2, 4, 'sms', '发送短信关心老人，询问是否需要帮助', '2024-01-18 16:45:00', '老人一切正常，暂时不需要特别帮助', '2024-01-18 17:00:00', 'replied', '日常关怀'),
(3, 5, 'email', '发送邮件询问老人的康复训练进展', '2024-01-19 08:30:00', NULL, NULL, 'pending', '康复训练相关'),
(1, 1, 'face_to_face', '周末探望老人，陪伴聊天', '2024-01-20 15:00:00', '家属已到访，与老人共度愉快时光', '2024-01-20 17:00:00', 'closed', '面对面探望'),
(2, 3, 'phone', '紧急电话，询问老人突发情况', '2024-01-21 22:30:00', '已及时处理，老人情况稳定', '2024-01-21 22:45:00', 'replied', '紧急情况处理'),
(3, 5, 'other', '通过护理人员转达家属关怀', '2024-01-22 11:20:00', NULL, NULL, 'pending', '间接沟通方式');