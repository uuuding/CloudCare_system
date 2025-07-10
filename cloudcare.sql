/*
 Navicat Premium Data Transfer

 Source Server         : CloudCare
 Source Server Type    : MySQL
 Source Server Version : 80401 (8.4.1)
 Source Host           : localhost:3306
 Source Schema         : cloudcare

 Target Server Type    : MySQL
 Target Server Version : 80401 (8.4.1)
 File Encoding         : 65001

 Date: 05/07/2025 09:10:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for assessment_report
-- ----------------------------
DROP TABLE IF EXISTS `assessment_report`;
CREATE TABLE `assessment_report`  (
  `report_id` bigint NOT NULL AUTO_INCREMENT COMMENT '报告ID',
  `elder_id` bigint NULL DEFAULT NULL COMMENT '老人ID',
  `report_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '报告编号',
  `report_type` tinyint(1) NULL DEFAULT NULL COMMENT '报告类型（1：入院评估，2：定期评估，3：专项评估）',
  `assessment_time` datetime NULL DEFAULT NULL COMMENT '评估时间',
  `assessor_id` bigint NULL DEFAULT NULL COMMENT '评估人ID',
  `assessor_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评估人姓名',
  `physical_score` int NULL DEFAULT NULL COMMENT '身体状况评分（0-100）',
  `physical_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身体状况描述',
  `mental_score` int NULL DEFAULT NULL COMMENT '心理状况评分（0-100）',
  `mental_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '心理状况描述',
  `cognitive_score` int NULL DEFAULT NULL COMMENT '认知能力评分（0-100）',
  `cognitive_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '认知能力描述',
  `daily_life_score` int NULL DEFAULT NULL COMMENT '日常生活能力评分（0-100）',
  `daily_life_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日常生活能力描述',
  `social_score` int NULL DEFAULT NULL COMMENT '社会参与评分（0-100）',
  `social_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '社会参与描述',
  `total_score` int NULL DEFAULT NULL COMMENT '总评分（0-100）',
  `conclusion` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评估结论',
  `suggestion` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '建议',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志（0：未删除，1：已删除）',
  PRIMARY KEY (`report_id`) USING BTREE,
  INDEX `idx_elder_id`(`elder_id` ASC) USING BTREE,
  INDEX `idx_assessor_id`(`assessor_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评估报告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of assessment_report
-- ----------------------------

-- ----------------------------
-- Table structure for device_binding
-- ----------------------------
DROP TABLE IF EXISTS `device_binding`;
CREATE TABLE `device_binding`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `macid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '设备编号（IMEI）',
  `elderly_id` int NOT NULL COMMENT '老人ID',
  `bind_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
  `unbind_time` datetime NULL DEFAULT NULL COMMENT '解绑时间',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '绑定状态：1-已绑定，0-已解绑',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_macid_elderly_active`(`macid` ASC, `elderly_id` ASC, `status` ASC) USING BTREE,
  INDEX `idx_macid`(`macid` ASC) USING BTREE,
  INDEX `idx_elderly_id`(`elderly_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_bind_time`(`bind_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '设备绑定表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_binding
-- ----------------------------
INSERT INTO `device_binding` VALUES (1, '1234567890', 1, '2025-07-03 18:06:07', NULL, 1, '2025-07-03 18:06:07', '2025-07-03 18:06:54', 'admin', NULL);
INSERT INTO `device_binding` VALUES (2, 'TEST987654321', 2, '2025-07-03 18:06:07', NULL, 1, '2025-07-03 18:06:07', '2025-07-03 18:06:07', 'admin', NULL);
INSERT INTO `device_binding` VALUES (3, '865167048259411', 1, '2025-07-03 20:07:43', NULL, 1, '2025-07-03 20:07:43', '2025-07-03 20:07:43', 'system', NULL);
INSERT INTO `device_binding` VALUES (6, '868120130035220', 2, '2025-07-05 08:55:50', NULL, 1, '2025-07-05 08:55:50', '2025-07-05 08:55:50', 'system', NULL);
INSERT INTO `device_binding` VALUES (7, '868120196085960', 2, '2025-07-05 09:04:28', NULL, 1, '2025-07-05 09:04:28', '2025-07-05 09:04:28', 'system', NULL);

-- ----------------------------
-- Table structure for device_info
-- ----------------------------
DROP TABLE IF EXISTS `device_info`;
CREATE TABLE `device_info`  (
  `device_id` bigint NOT NULL AUTO_INCREMENT COMMENT '设备ID',
  `device_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备编号',
  `device_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备名称',
  `device_type` tinyint(1) NULL DEFAULT NULL COMMENT '设备类型（1：体温计，2：血压计，3：血糖仪，4：心率监测仪，5：血氧仪，6：体重秤，7：其他）',
  `device_model` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备型号',
  `manufacturer` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备厂商',
  `purchase_date` datetime NULL DEFAULT NULL COMMENT '购买日期',
  `warranty_period` int NULL DEFAULT NULL COMMENT '保修期限（月）',
  `device_status` tinyint(1) NULL DEFAULT 1 COMMENT '设备状态（0：停用，1：正常，2：维修中，3：已报废）',
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '使用位置',
  `manager_id` bigint NULL DEFAULT NULL COMMENT '负责人ID',
  `manager_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人姓名',
  `last_maintenance_time` datetime NULL DEFAULT NULL COMMENT '最后检修时间',
  `next_maintenance_time` datetime NULL DEFAULT NULL COMMENT '下次检修时间',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备IP地址',
  `mac_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'MAC地址',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志（0：未删除，1：已删除）',
  PRIMARY KEY (`device_id`) USING BTREE,
  INDEX `idx_manager_id`(`manager_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '设备信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_info
-- ----------------------------

-- ----------------------------
-- Table structure for doctor_info
-- ----------------------------
DROP TABLE IF EXISTS `doctor_info`;
CREATE TABLE `doctor_info`  (
  `doctor_id` bigint NOT NULL AUTO_INCREMENT COMMENT '医生ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `doctor_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '医生编号',
  `doctor_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '医生姓名',
  `gender` tinyint(1) NULL DEFAULT 0 COMMENT '性别（0：未知，1：男，2：女）',
  `birth_date` date NULL DEFAULT NULL COMMENT '出生日期',
  `id_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证号',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `title` tinyint(1) NULL DEFAULT NULL COMMENT '职称（1：主任医师，2：副主任医师，3：主治医师，4：住院医师）',
  `department` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '科室',
  `specialization` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专业领域',
  `license_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '执业证号',
  `work_years` int NULL DEFAULT NULL COMMENT '工作年限',
  `introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '简介',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态（0：禁用，1：正常）',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志（0：未删除，1：已删除）',
  PRIMARY KEY (`doctor_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '医生信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of doctor_info
-- ----------------------------
INSERT INTO `doctor_info` VALUES (1, 1001, 'DC001', '张三', 1, '1985-06-15', '320105198506151234', '13800138000', 'zhangsan@example.com', 1, '内科', '心血管疾病', '2023A001', 15, '张三医生是心血管疾病领域的专家，拥有丰富的临床经验。', 1, 'admin', '2024-06-01 09:00:00', 'admin', '2024-06-01 10:00:00', '无', 0);
INSERT INTO `doctor_info` VALUES (2, 1002, 'DC002', '李四', 2, '1990-03-20', '320105199003205678', '13900139000', 'lisi@example.com', 2, '外科', '骨科疾病', '2023B002', 10, '李四医生擅长骨科疾病的诊断和治疗，曾多次参与复杂手术。', 1, 'admin', '2024-06-02 09:00:00', 'admin', '2024-06-02 10:00:00', '无', 0);
INSERT INTO `doctor_info` VALUES (3, 1003, 'DC003', '王五', 0, '1978-11-08', '320105197811089012', '13700137000', 'wangwu@example.com', 3, '妇产科', '妇科疾病', '2023C003', 20, '王五医生在妇产科领域有较高的知名度，尤其擅长妇科疾病的治疗。', 1, 'admin', '2024-06-03 09:00:00', 'admin', '2024-06-03 10:00:00', '无', 0);
INSERT INTO `doctor_info` VALUES (4, 1004, 'DC004', '赵六', 1, '1992-07-25', '320105199207253456', '13600136000', 'zhaoliu@example.com', 4, '儿科', '儿童疾病', '2023D004', 5, '赵六医生专注于儿童疾病的治疗，具有丰富的儿科临床经验。', 1, 'admin', '2024-06-04 09:00:00', 'admin', '2024-06-04 10:00:00', '无', 0);
INSERT INTO `doctor_info` VALUES (5, 1005, 'DC005', '周七', 2, '1988-04-10', '320105198804107890', '13500135000', 'zhouqi@example.com', 1, '神经内科', '神经系统疾病', '2023E005', 12, '周七医生在神经系统疾病方面有深入的研究，擅长各类神经疾病的诊断和治疗。', 1, 'admin', '2024-06-05 09:00:00', 'admin', '2024-06-05 10:00:00', '无', 0);

-- ----------------------------
-- Table structure for elderly_chronic_disease
-- ----------------------------
DROP TABLE IF EXISTS `elderly_chronic_disease`;
CREATE TABLE `elderly_chronic_disease`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `elderly_id` int NOT NULL,
  `disease_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `disease_category` enum('A','B','C') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `diagnosis_date` date NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `elderly_id`(`elderly_id` ASC) USING BTREE,
  CONSTRAINT `elderly_chronic_disease_ibfk_1` FOREIGN KEY (`elderly_id`) REFERENCES `elderly_profile` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of elderly_chronic_disease
-- ----------------------------
INSERT INTO `elderly_chronic_disease` VALUES (1, 1, '高血压', 'A', '2021-05-10');
INSERT INTO `elderly_chronic_disease` VALUES (2, 1, '糖尿病', 'B', '2019-03-22');
INSERT INTO `elderly_chronic_disease` VALUES (3, 2, '高血压', 'A', '2020-02-15');
INSERT INTO `elderly_chronic_disease` VALUES (4, 2, '高 cholesterol', 'B', '2018-11-30');
INSERT INTO `elderly_chronic_disease` VALUES (5, 3, '糖尿病', 'B', '2020-07-18');
INSERT INTO `elderly_chronic_disease` VALUES (6, 3, '关节炎', 'C', '2017-12-09');
INSERT INTO `elderly_chronic_disease` VALUES (7, 4, '肺炎', 'C', '2022-01-11');
INSERT INTO `elderly_chronic_disease` VALUES (8, 4, '高血压', 'A', '2018-05-25');
INSERT INTO `elderly_chronic_disease` VALUES (9, 5, '高血压', 'A', '2020-12-01');
INSERT INTO `elderly_chronic_disease` VALUES (10, 5, '糖尿病', 'B', '2021-08-19');
INSERT INTO `elderly_chronic_disease` VALUES (11, 6, '高血压', 'B', '2019-09-13');
INSERT INTO `elderly_chronic_disease` VALUES (12, 6, '心脏病', 'A', '2021-11-09');
INSERT INTO `elderly_chronic_disease` VALUES (13, 7, '糖尿病', 'B', '2020-10-23');
INSERT INTO `elderly_chronic_disease` VALUES (14, 7, '高血糖', 'B', '2018-05-05');
INSERT INTO `elderly_chronic_disease` VALUES (15, 8, '肺结核', 'C', '2021-06-12');
INSERT INTO `elderly_chronic_disease` VALUES (16, 8, '关节炎', 'C', '2017-02-21');
INSERT INTO `elderly_chronic_disease` VALUES (17, 9, '高血压', 'A', '2019-03-03');
INSERT INTO `elderly_chronic_disease` VALUES (18, 9, '肺部疾病', 'B', '2021-09-10');
INSERT INTO `elderly_chronic_disease` VALUES (19, 10, '高血压', 'A', '2021-06-25');
INSERT INTO `elderly_chronic_disease` VALUES (20, 10, '糖尿病', 'B', '2020-10-12');

-- ----------------------------
-- Table structure for elderly_observations
-- ----------------------------
DROP TABLE IF EXISTS `elderly_observations`;
CREATE TABLE `elderly_observations`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `elderly_id` int NOT NULL,
  `observation_time` datetime NOT NULL,
  `body_temperature` decimal(4, 1) NOT NULL,
  `systolic_bp` int NOT NULL,
  `cough` tinyint(1) NOT NULL DEFAULT 0,
  `heart_rate` int NOT NULL,
  `sleep_hours` decimal(3, 1) NOT NULL,
  `height` decimal(5, 1) NULL DEFAULT 165.0 COMMENT '身高(cm)',
  `weight` decimal(5, 1) NULL DEFAULT 50.0 COMMENT '体重(kg)',
  `observation_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '湖南省',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `elderly_id`(`elderly_id` ASC) USING BTREE,
  CONSTRAINT `elderly_observations_ibfk_1` FOREIGN KEY (`elderly_id`) REFERENCES `elderly_profile` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of elderly_observations
-- ----------------------------
INSERT INTO `elderly_observations` VALUES (2, 2, '2023-06-16 11:00:00', 37.0, 130, 0, 80, 8.0, 165.0, 50.0, '湖南省');
INSERT INTO `elderly_observations` VALUES (3, 3, '2023-06-17 09:00:00', 36.6, 125, 0, 78, 6.5, 165.0, 50.0, '湖南省');
INSERT INTO `elderly_observations` VALUES (4, 4, '2023-06-18 08:30:00', 37.5, 160, 1, 92, 5.5, 165.0, 50.0, '湖南省');
INSERT INTO `elderly_observations` VALUES (5, 5, '2023-06-19 10:00:00', 36.8, 135, 0, 88, 7.0, 165.0, 50.0, '湖南省');
INSERT INTO `elderly_observations` VALUES (6, 6, '2023-06-20 09:30:00', 37.1, 140, 1, 90, 6.0, 165.0, 50.0, '湖南省');
INSERT INTO `elderly_observations` VALUES (7, 7, '2023-06-21 11:15:00', 36.7, 120, 0, 85, 7.2, 165.0, 50.0, '湖南省');
INSERT INTO `elderly_observations` VALUES (8, 8, '2023-06-22 10:45:00', 36.9, 125, 1, 91, 6.5, 165.0, 50.0, '湖南省');
INSERT INTO `elderly_observations` VALUES (9, 9, '2023-06-23 08:50:00', 37.2, 150, 1, 95, 5.0, 165.0, 50.0, '湖南省');
INSERT INTO `elderly_observations` VALUES (10, 10, '2023-06-24 09:40:00', 36.4, 140, 0, 82, 7.8, 165.0, 50.0, '湖南省');

-- ----------------------------
-- Table structure for elderly_profile
-- ----------------------------
DROP TABLE IF EXISTS `elderly_profile`;
CREATE TABLE `elderly_profile`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `age` int NOT NULL,
  `gender` enum('男','女','其他') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '其他',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_name_age`(`name` ASC, `age` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of elderly_profile
-- ----------------------------
INSERT INTO `elderly_profile` VALUES (1, '老人1', 68, '男', '2025-06-27 16:22:00', '2025-06-27 16:22:00');
INSERT INTO `elderly_profile` VALUES (2, '老人2', 72, '女', '2025-06-27 16:22:00', '2025-06-27 16:22:00');
INSERT INTO `elderly_profile` VALUES (3, '老人3', 65, '男', '2025-06-27 16:22:00', '2025-06-27 16:22:00');
INSERT INTO `elderly_profile` VALUES (4, '老人4', 77, '女', '2025-06-27 16:22:00', '2025-06-27 16:22:00');
INSERT INTO `elderly_profile` VALUES (5, '老人5', 80, '男', '2025-06-27 16:22:00', '2025-06-27 16:22:00');
INSERT INTO `elderly_profile` VALUES (6, '老人6', 70, '女', '2025-06-27 16:22:00', '2025-06-27 16:22:00');
INSERT INTO `elderly_profile` VALUES (7, '老人7', 63, '男', '2025-06-27 16:22:00', '2025-06-27 16:22:00');
INSERT INTO `elderly_profile` VALUES (8, '老人8', 75, '女', '2025-06-27 16:22:00', '2025-06-27 16:22:00');
INSERT INTO `elderly_profile` VALUES (9, '老人9', 69, '男', '2025-06-27 16:22:00', '2025-06-27 16:22:00');
INSERT INTO `elderly_profile` VALUES (10, '老人10', 74, '女', '2025-06-27 16:22:00', '2025-06-27 16:22:00');

-- ----------------------------
-- Table structure for family_interaction
-- ----------------------------
DROP TABLE IF EXISTS `family_interaction`;
CREATE TABLE `family_interaction`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `elder_id` bigint NOT NULL COMMENT '老人ID',
  `family_member_id` bigint NOT NULL COMMENT '家属ID',
  `interaction_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '互动类型：phone-电话沟通，video-视频通话，sms-短信交流，wechat-微信聊天，email-邮件往来，face_to_face-面对面交流，other-其他',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '互动内容',
  `interaction_time` datetime NOT NULL COMMENT '互动时间',
  `reply_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '回复内容',
  `reply_time` datetime NULL DEFAULT NULL COMMENT '回复时间',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'pending' COMMENT '状态：pending-待回复，replied-已回复，closed-已关闭',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_elder_id`(`elder_id` ASC) USING BTREE,
  INDEX `idx_family_member_id`(`family_member_id` ASC) USING BTREE,
  INDEX `idx_interaction_time`(`interaction_time` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '家属互动表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of family_interaction
-- ----------------------------
INSERT INTO `family_interaction` VALUES (1, 1, 1, 'phone', '询问老人身体状况，关心日常生活', '2024-01-15 10:30:00', '老人身体状况良好，饮食正常，精神状态佳', '2024-01-15 11:00:00', 'replied', '定期关怀电话', '2025-06-27 21:33:52', '2025-06-27 21:33:52');
INSERT INTO `family_interaction` VALUES (2, 1, 2, 'video', '视频通话，看望老人并了解近况', '2024-01-16 14:20:00', '已安排视频通话，老人很开心见到家人', '2024-01-16 14:30:00', 'replied', '每周定期视频', '2025-06-27 21:33:52', '2025-06-27 21:33:52');
INSERT INTO `family_interaction` VALUES (3, 2, 3, 'wechat', '通过微信询问老人的用药情况', '2024-01-17 09:15:00', NULL, NULL, 'pending', '需要及时回复', '2025-06-27 21:33:52', '2025-06-27 21:33:52');
INSERT INTO `family_interaction` VALUES (4, 2, 4, 'sms', '发送短信关心老人，询问是否需要帮助', '2024-01-18 16:45:00', '老人一切正常，暂时不需要特别帮助', '2024-01-18 17:00:00', 'replied', '日常关怀', '2025-06-27 21:33:52', '2025-06-27 21:33:52');
INSERT INTO `family_interaction` VALUES (5, 3, 5, 'email', '发送邮件询问老人的康复训练进展', '2024-01-19 08:30:00', NULL, NULL, 'pending', '康复训练相关', '2025-06-27 21:33:52', '2025-06-27 21:33:52');
INSERT INTO `family_interaction` VALUES (6, 1, 1, 'face_to_face', '周末探望老人，陪伴聊天', '2024-01-20 15:00:00', '家属已到访，与老人共度愉快时光', '2024-01-20 17:00:00', 'closed', '面对面探望', '2025-06-27 21:33:52', '2025-06-27 21:33:52');
INSERT INTO `family_interaction` VALUES (7, 2, 3, 'phone', '紧急电话，询问老人突发情况', '2024-01-21 22:30:00', '已及时处理，老人情况稳定', '2024-01-21 22:45:00', 'replied', '紧急情况处理', '2025-06-27 21:33:52', '2025-06-27 21:33:52');
INSERT INTO `family_interaction` VALUES (8, 3, 5, 'other', '通过护理人员转达家属关怀', '2024-01-22 11:20:00', NULL, NULL, 'pending', '间接沟通方式', '2025-06-27 21:33:52', '2025-06-27 21:33:52');
INSERT INTO `family_interaction` VALUES (9, 1, 1, 'phone', '询问老人身体状况，关心日常生活', '2024-01-15 10:30:00', '老人身体状况良好，饮食正常，精神状态佳', '2024-01-15 11:00:00', 'replied', '定期关怀电话', '2025-06-29 21:33:02', '2025-06-29 21:33:02');
INSERT INTO `family_interaction` VALUES (10, 1, 2, 'video', '视频通话，看望老人并了解近况', '2024-01-16 14:20:00', '已安排视频通话，老人很开心见到家人', '2024-01-16 14:30:00', 'replied', '每周定期视频', '2025-06-29 21:33:02', '2025-06-29 21:33:02');
INSERT INTO `family_interaction` VALUES (11, 2, 3, 'wechat', '通过微信询问老人的用药情况', '2024-01-17 09:15:00', NULL, NULL, 'pending', '需要及时回复', '2025-06-29 21:33:02', '2025-06-29 21:33:02');
INSERT INTO `family_interaction` VALUES (12, 2, 4, 'sms', '发送短信关心老人，询问是否需要帮助', '2024-01-18 16:45:00', '老人一切正常，暂时不需要特别帮助', '2024-01-18 17:00:00', 'replied', '日常关怀', '2025-06-29 21:33:02', '2025-06-29 21:33:02');
INSERT INTO `family_interaction` VALUES (13, 3, 5, 'email', '发送邮件询问老人的康复训练进展', '2024-01-19 08:30:00', NULL, NULL, 'pending', '康复训练相关', '2025-06-29 21:33:02', '2025-06-29 21:33:02');
INSERT INTO `family_interaction` VALUES (14, 1, 1, 'face_to_face', '周末探望老人，陪伴聊天', '2024-01-20 15:00:00', '家属已到访，与老人共度愉快时光', '2024-01-20 17:00:00', 'closed', '面对面探望', '2025-06-29 21:33:02', '2025-06-29 21:33:02');
INSERT INTO `family_interaction` VALUES (15, 2, 3, 'phone', '紧急电话，询问老人突发情况', '2024-01-21 22:30:00', '已及时处理，老人情况稳定', '2024-01-21 22:45:00', 'replied', '紧急情况处理', '2025-06-29 21:33:02', '2025-06-29 21:33:02');
INSERT INTO `family_interaction` VALUES (16, 3, 5, 'other', '通过护理人员转达家属关怀', '2024-01-22 11:20:00', NULL, NULL, 'pending', '间接沟通方式', '2025-06-29 21:33:02', '2025-06-29 21:33:02');

-- ----------------------------
-- Table structure for family_member
-- ----------------------------
DROP TABLE IF EXISTS `family_member`;
CREATE TABLE `family_member`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `elder_id` bigint NOT NULL COMMENT '老人ID',
  `user_id` bigint NOT NULL COMMENT '家属用户ID',
  `relation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '与老人的关系',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `contact_email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系邮箱',
  `emergency_contact` tinyint(1) NULL DEFAULT 0 COMMENT '是否为紧急联系人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_elder_id`(`elder_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '家属成员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of family_member
-- ----------------------------
INSERT INTO `family_member` VALUES (1, 1, 2, '儿子', '13800138001', 'son@example.com', 1, '2025-06-27 21:33:52', '2025-06-27 21:33:52');
INSERT INTO `family_member` VALUES (2, 1, 3, '女儿', '13800138002', 'daughter@example.com', 0, '2025-06-27 21:33:52', '2025-06-27 21:33:52');
INSERT INTO `family_member` VALUES (3, 2, 4, '儿子', '13800138003', 'son2@example.com', 1, '2025-06-27 21:33:52', '2025-06-27 21:33:52');
INSERT INTO `family_member` VALUES (4, 2, 5, '儿媳', '13800138004', 'daughter_in_law@example.com', 0, '2025-06-27 21:33:52', '2025-06-27 21:33:52');
INSERT INTO `family_member` VALUES (5, 3, 6, '女儿', '13800138005', 'daughter2@example.com', 1, '2025-06-27 21:33:52', '2025-06-27 21:33:52');
INSERT INTO `family_member` VALUES (6, 1, 2, '儿子', '13800138001', 'son@example.com', 1, '2025-06-29 21:33:02', '2025-06-29 21:33:02');
INSERT INTO `family_member` VALUES (7, 1, 3, '女儿', '13800138002', 'daughter@example.com', 0, '2025-06-29 21:33:02', '2025-06-29 21:33:02');
INSERT INTO `family_member` VALUES (8, 2, 4, '儿子', '13800138003', 'son2@example.com', 1, '2025-06-29 21:33:02', '2025-06-29 21:33:02');
INSERT INTO `family_member` VALUES (9, 2, 5, '儿媳', '13800138004', 'daughter_in_law@example.com', 0, '2025-06-29 21:33:02', '2025-06-29 21:33:02');
INSERT INTO `family_member` VALUES (10, 3, 6, '女儿', '13800138005', 'daughter2@example.com', 1, '2025-06-29 21:33:02', '2025-06-29 21:33:02');

-- ----------------------------
-- Table structure for geo_fence
-- ----------------------------
DROP TABLE IF EXISTS `geo_fence`;
CREATE TABLE `geo_fence`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `elderly_id` int NOT NULL COMMENT '老人ID',
  `fence_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '围栏名称',
  `fence_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '围栏类型：circle-圆形，polygon-多边形',
  `center_lat` double NULL DEFAULT NULL COMMENT '围栏中心点纬度（圆形围栏使用）',
  `center_lon` double NULL DEFAULT NULL COMMENT '围栏中心点经度（圆形围栏使用）',
  `radius` double NULL DEFAULT NULL COMMENT '围栏半径（米，圆形围栏使用）',
  `coordinates` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '围栏坐标点（JSON格式，多边形围栏使用）',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '围栏状态：1-启用，0-禁用',
  `enter_alert` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用进入提醒：1-启用，0-禁用',
  `exit_alert` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用离开提醒：1-启用，0-禁用',
  `alert_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'sms' COMMENT '提醒方式：sms-短信，app-应用推送，both-两者都有',
  `emergency_contacts` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '紧急联系人手机号（多个用逗号分隔）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_elderly_id`(`elderly_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_geo_fence_elderly_status`(`elderly_id` ASC, `status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '电子围栏配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of geo_fence
-- ----------------------------
INSERT INTO `geo_fence` VALUES (1, 1, '家庭安全区域', 'circle', 30.783676, 103.85817, 500, NULL, 1, 0, 1, 'both', '15200813540', '2025-07-03 18:06:07', '2025-07-04 16:11:19', 'admin', NULL);
INSERT INTO `geo_fence` VALUES (2, 2, '社区活动区域', 'circle', 30.780606, 103.862692, 500, '[{\"lat\":22.699,\"lon\":114.125},{\"lat\":22.700,\"lon\":114.125},{\"lat\":22.700,\"lon\":114.126},{\"lat\":22.699,\"lon\":114.126}]', 1, 1, 1, 'both', '15200813540', '2025-07-03 18:06:07', '2025-07-05 08:56:34', 'admin', NULL);

-- ----------------------------
-- Table structure for geo_fence_event
-- ----------------------------
DROP TABLE IF EXISTS `geo_fence_event`;
CREATE TABLE `geo_fence_event`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `elderly_id` int NOT NULL COMMENT '老人ID',
  `fence_id` bigint NOT NULL COMMENT '围栏ID',
  `fence_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '围栏名称',
  `event_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '事件类型：enter-进入，exit-离开',
  `lat` double NOT NULL COMMENT '事件发生时的纬度',
  `lon` double NOT NULL COMMENT '事件发生时的经度',
  `location_id` bigint NULL DEFAULT NULL COMMENT 'GPS定位记录ID',
  `macid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '设备编号（IMEI）',
  `event_time` datetime NOT NULL COMMENT '事件发生时间',
  `alert_sent` tinyint NOT NULL DEFAULT 0 COMMENT '是否已发送提醒：1-已发送，0-未发送',
  `alert_sent_time` datetime NULL DEFAULT NULL COMMENT '提醒发送时间',
  `alert_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '提醒方式',
  `alert_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '提醒内容',
  `is_read` tinyint NOT NULL DEFAULT 0 COMMENT '是否已读：1-已读，0-未读',
  `read_time` datetime NULL DEFAULT NULL COMMENT '已读时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_elderly_id`(`elderly_id` ASC) USING BTREE,
  INDEX `idx_fence_id`(`fence_id` ASC) USING BTREE,
  INDEX `idx_event_type`(`event_type` ASC) USING BTREE,
  INDEX `idx_event_time`(`event_time` ASC) USING BTREE,
  INDEX `idx_alert_sent`(`alert_sent` ASC) USING BTREE,
  INDEX `idx_is_read`(`is_read` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '电子围栏事件记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of geo_fence_event
-- ----------------------------
INSERT INTO `geo_fence_event` VALUES (1, 1, 2, '社区活动区域', 'enter', 22.6993799, 114.1250394, 5, '865167048259411', '2025-07-03 20:48:22', 1, '2025-07-03 20:48:22', 'both', '【云护CloudCare】老人老人1已进入电子围栏\"社区活动区域\"，当前位置：纬度22.699380，经度114.125039，事件时间：2025-07-03 20:48:21，请及时关注。', 1, '2025-07-03 20:58:22', '2025-07-03 20:48:22');
INSERT INTO `geo_fence_event` VALUES (2, 1, 1, '家庭安全区域', 'exit', 22.7035, 114.131, 7, '865167048259411', '2025-07-03 20:59:08', 1, '2025-07-03 20:59:08', 'sms', '【云护CloudCare】老人老人1已离开电子围栏\"家庭安全区域\"，当前位置：纬度22.703500，经度114.131000，事件时间：2025-07-03 20:59:07，请及时关注。', 1, '2025-07-03 20:59:29', '2025-07-03 20:59:08');
INSERT INTO `geo_fence_event` VALUES (3, 1, 2, '社区活动区域', 'exit', 22.7035, 114.131, 7, '865167048259411', '2025-07-03 20:59:08', 1, '2025-07-03 20:59:08', 'both', '【云护CloudCare】老人老人1已离开电子围栏\"社区活动区域\"，当前位置：纬度22.703500，经度114.131000，事件时间：2025-07-03 20:59:08，请及时关注。', 1, '2025-07-03 20:59:21', '2025-07-03 20:59:08');
INSERT INTO `geo_fence_event` VALUES (4, 1, 1, '家庭安全区域', 'exit', 22.69937, 114.12503, 34, '865167048259411', '2025-07-04 16:11:30', 1, '2025-07-04 16:11:30', 'both', '【云护CloudCare】老人老人1已离开电子围栏\"家庭安全区域\"，当前位置：纬度22.699370，经度114.125030，事件时间：2025-07-04 16:11:29，请及时关注。', 1, '2025-07-04 16:11:47', '2025-07-04 16:11:30');
INSERT INTO `geo_fence_event` VALUES (5, 1, 1, '家庭安全区域', 'exit', 30.78, 103.85, 40, '865167048259411', '2025-07-04 16:12:19', 1, '2025-07-04 16:12:19', 'both', '【云护CloudCare】老人老人1已离开电子围栏\"家庭安全区域\"，当前位置：纬度30.780000，经度103.850000，事件时间：2025-07-04 16:12:19，请及时关注。', 1, '2025-07-04 16:12:37', '2025-07-04 16:12:19');
INSERT INTO `geo_fence_event` VALUES (6, 1, 1, '家庭安全区域', 'exit', 22.69937, 114.12503, 48, '865167048259411', '2025-07-04 16:13:54', 1, '2025-07-04 16:13:54', 'both', '【云护CloudCare】老人老人1已离开电子围栏\"家庭安全区域\"，当前位置：纬度22.699370，经度114.125030，事件时间：2025-07-04 16:13:54，请及时关注。', 1, '2025-07-04 16:14:08', '2025-07-04 16:13:54');
INSERT INTO `geo_fence_event` VALUES (7, 1, 1, '家庭安全区域', 'exit', 22.69937, 114.12503, 53, '865167048259411', '2025-07-04 16:14:50', 1, '2025-07-04 16:14:50', 'both', '【云护CloudCare】老人老人1已离开电子围栏\"家庭安全区域\"，当前位置：纬度22.699370，经度114.125030，事件时间：2025-07-04 16:14:49，请及时关注。', 1, '2025-07-04 16:15:09', '2025-07-04 16:14:50');
INSERT INTO `geo_fence_event` VALUES (8, 1, 1, '家庭安全区域', 'enter', 30.783676, 103.85817, 86, '865167048259411', '2025-07-04 16:35:03', 1, '2025-07-04 16:35:07', 'both', '【云护CloudCare】老人老人1已进入电子围栏\"家庭安全区域\"，当前位置：纬度30.783676，经度103.858170，事件时间：2025-07-04 16:35:02，请及时关注。', 1, '2025-07-04 16:35:17', '2025-07-04 16:35:03');
INSERT INTO `geo_fence_event` VALUES (9, 1, 1, '家庭安全区域', 'enter', 30.783676, 103.85817, 90, '865167048259411', '2025-07-04 16:53:04', 1, '2025-07-04 16:53:07', 'both', '【云护CloudCare】老人老人1已进入电子围栏\"家庭安全区域\"，当前位置：纬度30.783676，经度103.858170，事件时间：2025-07-04 16:53:03，请及时关注。', 1, '2025-07-04 16:53:23', '2025-07-04 16:53:04');
INSERT INTO `geo_fence_event` VALUES (10, 1, 1, '家庭安全区域', 'enter', 30.783676, 103.85817, 93, '865167048259411', '2025-07-04 21:23:23', 1, '2025-07-04 21:32:01', 'both', '【云护CloudCare】老人老人1已进入电子围栏\"家庭安全区域\"，当前位置：纬度30.783676，经度103.858170，事件时间：2025-07-04 21:23:22，请及时关注。', 1, '2025-07-04 21:23:36', '2025-07-04 21:23:23');
INSERT INTO `geo_fence_event` VALUES (14, 1, 1, '家庭安全区域', 'enter', 30.783676, 103.85817, 105, '865167048259411', '2025-07-04 21:38:38', 1, '2025-07-04 21:38:42', 'both', '【云护CloudCare】老人老人1已进入电子围栏\"家庭安全区域\"，当前位置：纬度30.783676，经度103.858170，事件时间：2025-07-04 21:38:38，请及时关注。', 1, '2025-07-04 21:38:46', '2025-07-04 21:38:38');
INSERT INTO `geo_fence_event` VALUES (15, 1, 1, '家庭安全区域', 'exit', 22.6993799, 114.1250394, 110, '865167048259411', '2025-07-04 21:39:09', 1, '2025-07-04 21:39:09', 'both', '【云护CloudCare】老人老人1已离开电子围栏\"家庭安全区域\"，当前位置：纬度22.699380，经度114.125039，事件时间：2025-07-04 21:39:08，请及时关注。', 1, '2025-07-04 21:39:17', '2025-07-04 21:39:09');

-- ----------------------------
-- Table structure for gps_location
-- ----------------------------
DROP TABLE IF EXISTS `gps_location`;
CREATE TABLE `gps_location`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `macid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '设备编号（IMEI）',
  `elderly_id` int NULL DEFAULT NULL COMMENT '老人ID',
  `gps_time` bigint NULL DEFAULT NULL COMMENT 'GPS时间戳（毫秒）',
  `heart_time` bigint NULL DEFAULT NULL COMMENT '信号时间',
  `upd_time` bigint NULL DEFAULT NULL COMMENT '最后更新位置时间',
  `lat` double NULL DEFAULT NULL COMMENT 'GPS纬度',
  `lon` double NULL DEFAULT NULL COMMENT 'GPS经度',
  `map_lat` double NULL DEFAULT NULL COMMENT '地图纬度',
  `map_lon` double NULL DEFAULT NULL COMMENT '地图经度',
  `speed` double NULL DEFAULT NULL COMMENT '速度（单位：km/h）',
  `dir` double NULL DEFAULT NULL COMMENT '方向角度',
  `stats` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态字段，逗号分隔',
  `value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态值字段，逗号分隔',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_macid`(`macid` ASC) USING BTREE,
  INDEX `idx_elderly_id`(`elderly_id` ASC) USING BTREE,
  INDEX `idx_gps_time`(`gps_time` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_gps_location_composite`(`elderly_id` ASC, `gps_time` DESC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 111 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'GPS定位数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gps_location
-- ----------------------------
INSERT INTO `gps_location` VALUES (1, '865167048259411', NULL, 1751540000000, 1751540010000, 1751540000000, 22.69937, 114.12503, 22.69937, 114.12503, 0, 0, '1,0,1,,,1,1', '1000,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-03 20:00:53', '2025-07-03 20:00:53');
INSERT INTO `gps_location` VALUES (2, '865167048259411', NULL, 1751540000000, 1751540010000, 1751540000000, 22.69937, 114.12503, 22.69937, 114.12503, 0, 0, '1,0,1,,,1,1', '1000,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-03 20:06:51', '2025-07-03 20:06:51');
INSERT INTO `gps_location` VALUES (3, '865167048259411', 1, 1751540000000, 1751540010000, 1751540000000, 22.69937, 114.12503, 22.69937, 114.12503, 0, 0, '1,0,1,,,1,1', '1000,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-03 20:08:01', '2025-07-03 20:08:01');
INSERT INTO `gps_location` VALUES (4, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 22.7035, 114.131, 22.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-03 20:42:27', '2025-07-03 20:42:27');
INSERT INTO `gps_location` VALUES (5, '865167048259411', 1, 1656399055758, 1656399055758, 1656399055758, 22.6993799, 114.1250394, NULL, NULL, 0, 90, '0,1,1,0,1,1,1', '12744.49,0,0,,0,0,0,0,0,0,0,0,0,0,0,0,100', '2025-07-03 20:48:22', '2025-07-03 20:48:22');
INSERT INTO `gps_location` VALUES (6, '865167048259411', 1, 1656399055758, 1656399055758, 1656399055758, 22.6993799, 114.1250394, NULL, NULL, 0, 90, '0,1,1,0,1,1,1', '12744.49,0,0,,0,0,0,0,0,0,0,0,0,0,0,0,100', '2025-07-03 20:59:02', '2025-07-03 20:59:02');
INSERT INTO `gps_location` VALUES (7, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 22.7035, 114.131, 22.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-03 20:59:08', '2025-07-03 20:59:08');
INSERT INTO `gps_location` VALUES (8, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 22.7035, 114.131, 22.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 10:38:12', '2025-07-04 10:38:12');
INSERT INTO `gps_location` VALUES (9, '865167048259411', 1, 1656399055758, 1656399055758, 1656399055758, 22.6993799, 114.1250394, NULL, NULL, 0, 90, '0,1,1,0,1,1,1', '12744.49,0,0,,0,0,0,0,0,0,0,0,0,0,0,0,100', '2025-07-04 10:41:04', '2025-07-04 10:41:04');
INSERT INTO `gps_location` VALUES (10, '865167048259411', 1, 1751540000000, 1751540010000, 1751540000000, 22.69937, 114.12503, 22.69937, 114.12503, 0, 0, '1,0,1,,,1,1', '1000,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 10:41:26', '2025-07-04 10:41:26');
INSERT INTO `gps_location` VALUES (11, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 22.7035, 114.131, 22.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 10:41:28', '2025-07-04 10:41:28');
INSERT INTO `gps_location` VALUES (12, '865167048259411', 1, 1656399055758, 1656399055758, 1656399055758, 22.6993799, 114.1250394, NULL, NULL, 0, 90, '0,1,1,0,1,1,1', '12744.49,0,0,,0,0,0,0,0,0,0,0,0,0,0,0,100', '2025-07-04 10:41:29', '2025-07-04 10:41:29');
INSERT INTO `gps_location` VALUES (13, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 22.7035, 114.131, 22.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 10:50:01', '2025-07-04 10:50:01');
INSERT INTO `gps_location` VALUES (14, '865167048259411', 1, 1751540000000, 1751540010000, 1751540000000, 22.69937, 114.12503, 22.69937, 114.12503, 0, 0, '1,0,1,,,1,1', '1000,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 10:50:37', '2025-07-04 10:50:37');
INSERT INTO `gps_location` VALUES (15, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 22.7035, 114.131, 22.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 14:38:19', '2025-07-04 14:38:19');
INSERT INTO `gps_location` VALUES (16, '865167048259411', 1, 1656399055758, 1656399055758, 1656399055758, 22.6993799, 114.1250394, NULL, NULL, 0, 90, '0,1,1,0,1,1,1', '12744.49,0,0,,0,0,0,0,0,0,0,0,0,0,0,0,100', '2025-07-04 14:40:19', '2025-07-04 14:40:19');
INSERT INTO `gps_location` VALUES (17, '865167048259411', 1, 1751540000000, 1751540010000, 1751540000000, 22.69937, 114.12503, 22.69937, 114.12503, 0, 0, '1,0,1,,,1,1', '1000,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 14:40:21', '2025-07-04 14:40:21');
INSERT INTO `gps_location` VALUES (18, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 32.7035, 114.131, 32.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 14:41:42', '2025-07-04 14:41:42');
INSERT INTO `gps_location` VALUES (19, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 32.7035, 114.131, 32.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 15:50:50', '2025-07-04 15:50:50');
INSERT INTO `gps_location` VALUES (20, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 32.7035, 114.131, 32.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 15:54:33', '2025-07-04 15:54:33');
INSERT INTO `gps_location` VALUES (21, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 32.7035, 114.131, 32.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 15:57:04', '2025-07-04 15:57:04');
INSERT INTO `gps_location` VALUES (22, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 32.7035, 114.131, 32.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 15:57:24', '2025-07-04 15:57:24');
INSERT INTO `gps_location` VALUES (23, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 32.7035, 114.131, 32.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 15:57:25', '2025-07-04 15:57:25');
INSERT INTO `gps_location` VALUES (24, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 32.7035, 114.131, 32.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 15:57:26', '2025-07-04 15:57:26');
INSERT INTO `gps_location` VALUES (25, '865167048259411', 1, 1751540000000, 1751540010000, 1751540000000, 22.69937, 114.12503, 22.69937, 114.12503, 0, 0, '1,0,1,,,1,1', '1000,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 15:59:32', '2025-07-04 15:59:32');
INSERT INTO `gps_location` VALUES (26, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 32.7035, 114.131, 32.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 15:59:34', '2025-07-04 15:59:34');
INSERT INTO `gps_location` VALUES (27, '865167048259411', 1, 1656399055758, 1656399055758, 1656399055758, 22.6993799, 114.1250394, NULL, NULL, 0, 90, '0,1,1,0,1,1,1', '12744.49,0,0,,0,0,0,0,0,0,0,0,0,0,0,0,100', '2025-07-04 15:59:35', '2025-07-04 15:59:35');
INSERT INTO `gps_location` VALUES (28, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 30.78, 103.85, 30.78, 103.85, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:04:45', '2025-07-04 16:04:45');
INSERT INTO `gps_location` VALUES (29, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 30.783676, 103.85817, 30.783676, 103.85817, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:05:18', '2025-07-04 16:05:18');
INSERT INTO `gps_location` VALUES (30, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 32.7035, 114.131, 32.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:09:16', '2025-07-04 16:09:16');
INSERT INTO `gps_location` VALUES (31, '865167048259411', 1, 1656399055758, 1656399055758, 1656399055758, 22.6993799, 114.1250394, NULL, NULL, 0, 90, '0,1,1,0,1,1,1', '12744.49,0,0,,0,0,0,0,0,0,0,0,0,0,0,0,100', '2025-07-04 16:09:28', '2025-07-04 16:09:28');
INSERT INTO `gps_location` VALUES (32, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 30.78, 103.85, 30.78, 103.85, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:09:30', '2025-07-04 16:09:30');
INSERT INTO `gps_location` VALUES (33, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 30.783676, 103.85817, 30.783676, 103.85817, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:09:32', '2025-07-04 16:09:32');
INSERT INTO `gps_location` VALUES (34, '865167048259411', 1, 1751540000000, 1751540010000, 1751540000000, 22.69937, 114.12503, 22.69937, 114.12503, 0, 0, '1,0,1,,,1,1', '1000,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:11:30', '2025-07-04 16:11:30');
INSERT INTO `gps_location` VALUES (35, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 32.7035, 114.131, 32.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:11:32', '2025-07-04 16:11:32');
INSERT INTO `gps_location` VALUES (36, '865167048259411', 1, 1656399055758, 1656399055758, 1656399055758, 22.6993799, 114.1250394, NULL, NULL, 0, 90, '0,1,1,0,1,1,1', '12744.49,0,0,,0,0,0,0,0,0,0,0,0,0,0,0,100', '2025-07-04 16:11:33', '2025-07-04 16:11:33');
INSERT INTO `gps_location` VALUES (37, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 30.78, 103.85, 30.78, 103.85, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:11:35', '2025-07-04 16:11:35');
INSERT INTO `gps_location` VALUES (38, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 30.783676, 103.85817, 30.783676, 103.85817, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:11:36', '2025-07-04 16:11:36');
INSERT INTO `gps_location` VALUES (39, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 30.783676, 103.85817, 30.783676, 103.85817, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:12:13', '2025-07-04 16:12:13');
INSERT INTO `gps_location` VALUES (40, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 30.78, 103.85, 30.78, 103.85, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:12:19', '2025-07-04 16:12:19');
INSERT INTO `gps_location` VALUES (41, '865167048259411', 1, 1656399055758, 1656399055758, 1656399055758, 22.6993799, 114.1250394, NULL, NULL, 0, 90, '0,1,1,0,1,1,1', '12744.49,0,0,,0,0,0,0,0,0,0,0,0,0,0,0,100', '2025-07-04 16:12:32', '2025-07-04 16:12:32');
INSERT INTO `gps_location` VALUES (42, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 32.7035, 114.131, 32.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:12:36', '2025-07-04 16:12:36');
INSERT INTO `gps_location` VALUES (43, '865167048259411', 1, 1751540000000, 1751540010000, 1751540000000, 22.69937, 114.12503, 22.69937, 114.12503, 0, 0, '1,0,1,,,1,1', '1000,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:13:22', '2025-07-04 16:13:22');
INSERT INTO `gps_location` VALUES (44, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 32.7035, 114.131, 32.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:13:35', '2025-07-04 16:13:35');
INSERT INTO `gps_location` VALUES (45, '865167048259411', 1, 1656399055758, 1656399055758, 1656399055758, 22.6993799, 114.1250394, NULL, NULL, 0, 90, '0,1,1,0,1,1,1', '12744.49,0,0,,0,0,0,0,0,0,0,0,0,0,0,0,100', '2025-07-04 16:13:40', '2025-07-04 16:13:40');
INSERT INTO `gps_location` VALUES (46, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 30.78, 103.85, 30.78, 103.85, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:13:45', '2025-07-04 16:13:45');
INSERT INTO `gps_location` VALUES (47, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 30.783676, 103.85817, 30.783676, 103.85817, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:13:49', '2025-07-04 16:13:49');
INSERT INTO `gps_location` VALUES (48, '865167048259411', 1, 1751540000000, 1751540010000, 1751540000000, 22.69937, 114.12503, 22.69937, 114.12503, 0, 0, '1,0,1,,,1,1', '1000,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:13:54', '2025-07-04 16:13:54');
INSERT INTO `gps_location` VALUES (49, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 32.7035, 114.131, 32.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:14:26', '2025-07-04 16:14:26');
INSERT INTO `gps_location` VALUES (50, '865167048259411', 1, 1656399055758, 1656399055758, 1656399055758, 22.6993799, 114.1250394, NULL, NULL, 0, 90, '0,1,1,0,1,1,1', '12744.49,0,0,,0,0,0,0,0,0,0,0,0,0,0,0,100', '2025-07-04 16:14:31', '2025-07-04 16:14:31');
INSERT INTO `gps_location` VALUES (51, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 30.78, 103.85, 30.78, 103.85, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:14:36', '2025-07-04 16:14:36');
INSERT INTO `gps_location` VALUES (52, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 30.783676, 103.85817, 30.783676, 103.85817, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:14:41', '2025-07-04 16:14:41');
INSERT INTO `gps_location` VALUES (53, '865167048259411', 1, 1751540000000, 1751540010000, 1751540000000, 22.69937, 114.12503, 22.69937, 114.12503, 0, 0, '1,0,1,,,1,1', '1000,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:14:50', '2025-07-04 16:14:50');
INSERT INTO `gps_location` VALUES (54, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 103.85817, 30.783676, 103.85817, 30.783676, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:18:56', '2025-07-04 16:18:56');
INSERT INTO `gps_location` VALUES (55, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 103.85817, 30.783676, 103.85817, 30.783676, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:19:07', '2025-07-04 16:19:07');
INSERT INTO `gps_location` VALUES (56, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 103.85817, 30.783676, 103.85817, 30.783676, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:19:08', '2025-07-04 16:19:08');
INSERT INTO `gps_location` VALUES (57, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 30.78, 103.85, 30.78, 103.85, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:19:10', '2025-07-04 16:19:10');
INSERT INTO `gps_location` VALUES (58, '865167048259411', 1, 1656399055758, 1656399055758, 1656399055758, 22.6993799, 114.1250394, NULL, NULL, 0, 90, '0,1,1,0,1,1,1', '12744.49,0,0,,0,0,0,0,0,0,0,0,0,0,0,0,100', '2025-07-04 16:19:13', '2025-07-04 16:19:13');
INSERT INTO `gps_location` VALUES (59, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 32.7035, 114.131, 32.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:19:16', '2025-07-04 16:19:16');
INSERT INTO `gps_location` VALUES (60, '865167048259411', 1, 1751540000000, 1751540010000, 1751540000000, 22.69937, 114.12503, 22.69937, 114.12503, 0, 0, '1,0,1,,,1,1', '1000,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:19:19', '2025-07-04 16:19:19');
INSERT INTO `gps_location` VALUES (61, '865167048259411', 1, 1751540000000, 1751540010000, 1751540000000, 22.69937, 114.12503, 22.69937, 114.12503, 0, 0, '1,0,1,,,1,1', '1000,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:19:26', '2025-07-04 16:19:26');
INSERT INTO `gps_location` VALUES (62, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 32.7035, 114.131, 32.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:19:28', '2025-07-04 16:19:28');
INSERT INTO `gps_location` VALUES (63, '865167048259411', 1, 1656399055758, 1656399055758, 1656399055758, 22.6993799, 114.1250394, NULL, NULL, 0, 90, '0,1,1,0,1,1,1', '12744.49,0,0,,0,0,0,0,0,0,0,0,0,0,0,0,100', '2025-07-04 16:19:29', '2025-07-04 16:19:29');
INSERT INTO `gps_location` VALUES (64, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 30.78, 103.85, 30.78, 103.85, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:19:30', '2025-07-04 16:19:30');
INSERT INTO `gps_location` VALUES (65, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 103.85817, 30.783676, 103.85817, 30.783676, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:19:31', '2025-07-04 16:19:31');
INSERT INTO `gps_location` VALUES (66, '865167048259411', 1, 1751540000000, 1751540010000, 1751540000000, 22.69937, 114.12503, 22.69937, 114.12503, 0, 0, '1,0,1,,,1,1', '1000,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:19:54', '2025-07-04 16:19:54');
INSERT INTO `gps_location` VALUES (67, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 32.7035, 114.131, 32.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:19:55', '2025-07-04 16:19:55');
INSERT INTO `gps_location` VALUES (68, '865167048259411', 1, 1656399055758, 1656399055758, 1656399055758, 22.6993799, 114.1250394, NULL, NULL, 0, 90, '0,1,1,0,1,1,1', '12744.49,0,0,,0,0,0,0,0,0,0,0,0,0,0,0,100', '2025-07-04 16:19:57', '2025-07-04 16:19:57');
INSERT INTO `gps_location` VALUES (69, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 30.78, 103.85, 30.78, 103.85, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:19:58', '2025-07-04 16:19:58');
INSERT INTO `gps_location` VALUES (70, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 103.85817, 30.783676, 103.85817, 30.783676, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:19:59', '2025-07-04 16:19:59');
INSERT INTO `gps_location` VALUES (71, '865167048259411', 1, 1751540000000, 1751540010000, 1751540000000, 22.69937, 114.12503, 22.69937, 114.12503, 0, 0, '1,0,1,,,1,1', '1000,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:27:15', '2025-07-04 16:27:15');
INSERT INTO `gps_location` VALUES (72, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 32.7035, 114.131, 32.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:27:16', '2025-07-04 16:27:16');
INSERT INTO `gps_location` VALUES (73, '865167048259411', 1, 1656399055758, 1656399055758, 1656399055758, 22.6993799, 114.1250394, NULL, NULL, 0, 90, '0,1,1,0,1,1,1', '12744.49,0,0,,0,0,0,0,0,0,0,0,0,0,0,0,100', '2025-07-04 16:27:19', '2025-07-04 16:27:19');
INSERT INTO `gps_location` VALUES (74, '865167048259411', 1, 1751540000000, 1751540010000, 1751540000000, 22.69937, 114.12503, 22.69937, 114.12503, 0, 0, '1,0,1,,,1,1', '1000,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:27:24', '2025-07-04 16:27:24');
INSERT INTO `gps_location` VALUES (75, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 32.7035, 114.131, 32.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:27:28', '2025-07-04 16:27:28');
INSERT INTO `gps_location` VALUES (76, '865167048259411', 1, 1656399055758, 1656399055758, 1656399055758, 22.6993799, 114.1250394, NULL, NULL, 0, 90, '0,1,1,0,1,1,1', '12744.49,0,0,,0,0,0,0,0,0,0,0,0,0,0,0,100', '2025-07-04 16:27:34', '2025-07-04 16:27:34');
INSERT INTO `gps_location` VALUES (77, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 30.78, 103.85, 30.78, 103.85, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:27:37', '2025-07-04 16:27:37');
INSERT INTO `gps_location` VALUES (78, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 103.85817, 30.783676, 103.85817, 30.783676, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:27:40', '2025-07-04 16:27:40');
INSERT INTO `gps_location` VALUES (79, '865167048259411', 1, 1751540000000, 1751540010000, 1751540000000, 22.69937, 114.12503, 22.69937, 114.12503, 0, 0, '1,0,1,,,1,1', '1000,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:30:23', '2025-07-04 16:30:23');
INSERT INTO `gps_location` VALUES (80, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 32.7035, 114.131, 32.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:30:26', '2025-07-04 16:30:26');
INSERT INTO `gps_location` VALUES (81, '865167048259411', 1, 1656399055758, 1656399055758, 1656399055758, 22.6993799, 114.1250394, NULL, NULL, 0, 90, '0,1,1,0,1,1,1', '12744.49,0,0,,0,0,0,0,0,0,0,0,0,0,0,0,100', '2025-07-04 16:30:28', '2025-07-04 16:30:28');
INSERT INTO `gps_location` VALUES (82, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 30.78, 103.85, 30.78, 103.85, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:30:29', '2025-07-04 16:30:29');
INSERT INTO `gps_location` VALUES (83, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 103.85817, 30.783676, 103.85817, 30.783676, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:30:30', '2025-07-04 16:30:30');
INSERT INTO `gps_location` VALUES (84, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 103.85817, 30.783676, 103.85817, 30.783676, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:32:16', '2025-07-04 16:32:16');
INSERT INTO `gps_location` VALUES (85, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 103.85817, 30.783676, 103.85817, 30.783676, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:33:57', '2025-07-04 16:33:57');
INSERT INTO `gps_location` VALUES (86, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 30.783676, 103.85817, 30.783676, 103.85817, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:35:03', '2025-07-04 16:35:03');
INSERT INTO `gps_location` VALUES (87, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 32.7035, 114.131, 32.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:51:34', '2025-07-04 16:51:34');
INSERT INTO `gps_location` VALUES (88, '865167048259411', 1, 1656399055758, 1656399055758, 1656399055758, 22.6993799, 114.1250394, NULL, NULL, 0, 90, '0,1,1,0,1,1,1', '12744.49,0,0,,0,0,0,0,0,0,0,0,0,0,0,0,100', '2025-07-04 16:51:54', '2025-07-04 16:51:54');
INSERT INTO `gps_location` VALUES (89, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 30.78, 103.85, 30.78, 103.85, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:52:01', '2025-07-04 16:52:01');
INSERT INTO `gps_location` VALUES (90, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 30.783676, 103.85817, 30.783676, 103.85817, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 16:53:04', '2025-07-04 16:53:04');
INSERT INTO `gps_location` VALUES (91, '865167048259411', 1, 1656399055758, 1656399055758, 1656399055758, 22.6993799, 114.1250394, NULL, NULL, 0, 90, '0,1,1,0,1,1,1', '12744.49,0,0,,0,0,0,0,0,0,0,0,0,0,0,0,100', '2025-07-04 21:22:07', '2025-07-04 21:22:07');
INSERT INTO `gps_location` VALUES (92, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 30.78, 103.85, 30.78, 103.85, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 21:22:18', '2025-07-04 21:22:18');
INSERT INTO `gps_location` VALUES (93, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 30.783676, 103.85817, 30.783676, 103.85817, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 21:23:23', '2025-07-04 21:23:23');
INSERT INTO `gps_location` VALUES (95, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 30.783676, 103.85817, 30.783676, 103.85817, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 21:25:15', '2025-07-04 21:25:15');
INSERT INTO `gps_location` VALUES (96, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 30.783676, 103.85817, 30.783676, 103.85817, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 21:25:20', '2025-07-04 21:25:20');
INSERT INTO `gps_location` VALUES (97, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 30.783676, 103.85817, 30.783676, 103.85817, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 21:25:22', '2025-07-04 21:25:22');
INSERT INTO `gps_location` VALUES (100, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 30.783676, 103.85817, 30.783676, 103.85817, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 21:27:19', '2025-07-04 21:27:19');
INSERT INTO `gps_location` VALUES (101, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 30.78, 103.85, 30.78, 103.85, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 21:38:11', '2025-07-04 21:38:11');
INSERT INTO `gps_location` VALUES (102, '865167048259411', 1, 1656399055758, 1656399055758, 1656399055758, 22.6993799, 114.1250394, NULL, NULL, 0, 90, '0,1,1,0,1,1,1', '12744.49,0,0,,0,0,0,0,0,0,0,0,0,0,0,0,100', '2025-07-04 21:38:31', '2025-07-04 21:38:31');
INSERT INTO `gps_location` VALUES (103, '865167048259411', 1, 1751540060000, 1751540070000, 1751540060000, 32.7035, 114.131, 32.7035, 114.131, 20, 45, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 21:38:33', '2025-07-04 21:38:33');
INSERT INTO `gps_location` VALUES (104, '865167048259411', 1, 1751540000000, 1751540010000, 1751540000000, 22.69937, 114.12503, 22.69937, 114.12503, 0, 0, '1,0,1,,,1,1', '1000,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 21:38:35', '2025-07-04 21:38:35');
INSERT INTO `gps_location` VALUES (105, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 30.783676, 103.85817, 30.783676, 103.85817, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 21:38:38', '2025-07-04 21:38:38');
INSERT INTO `gps_location` VALUES (106, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 30.783676, 103.85817, 30.783676, 103.85817, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 21:39:04', '2025-07-04 21:39:04');
INSERT INTO `gps_location` VALUES (107, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 30.783676, 103.85817, 30.783676, 103.85817, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 21:39:05', '2025-07-04 21:39:05');
INSERT INTO `gps_location` VALUES (108, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 30.783676, 103.85817, 30.783676, 103.85817, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 21:39:06', '2025-07-04 21:39:06');
INSERT INTO `gps_location` VALUES (109, '865167048259411', 1, 1751540120000, 1751540130000, 1751540120000, 30.783676, 103.85817, 30.783676, 103.85817, 15, 90, '1,0,1,,,1,1', '1100,,,,240,12.1,12,100,,,,,,,,GPS,', '2025-07-04 21:39:07', '2025-07-04 21:39:07');
INSERT INTO `gps_location` VALUES (110, '865167048259411', 1, 1656399055758, 1656399055758, 1656399055758, 22.6993799, 114.1250394, NULL, NULL, 0, 90, '0,1,1,0,1,1,1', '12744.49,0,0,,0,0,0,0,0,0,0,0,0,0,0,0,100', '2025-07-04 21:39:09', '2025-07-04 21:39:09');

-- ----------------------------
-- Table structure for health_alert
-- ----------------------------
DROP TABLE IF EXISTS `health_alert`;
CREATE TABLE `health_alert`  (
  `alert_id` bigint NOT NULL AUTO_INCREMENT COMMENT '预警ID',
  `elderly_id` bigint NOT NULL COMMENT '老人ID',
  `elderly_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '老人姓名',
  `alert_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '预警类型（TEMPERATURE, BLOOD_PRESSURE, HEART_RATE, BMI, SLEEP等）',
  `alert_level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '预警级别（LOW, MEDIUM, HIGH, CRITICAL）',
  `alert_title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '预警标题',
  `alert_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '预警描述',
  `trigger_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '触发值',
  `normal_range` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '正常范围',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'ACTIVE' COMMENT '预警状态（ACTIVE, RESOLVED, IGNORED）',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `resolved_at` timestamp NULL DEFAULT NULL COMMENT '解决时间',
  `resolved_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理人员',
  `resolved_note` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '处理备注',
  `observation_id` bigint NULL DEFAULT NULL COMMENT '关联的观察记录ID',
  PRIMARY KEY (`alert_id`) USING BTREE,
  INDEX `idx_elderly_id`(`elderly_id` ASC) USING BTREE,
  INDEX `idx_alert_type`(`alert_type` ASC) USING BTREE,
  INDEX `idx_alert_level`(`alert_level` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '健康预警记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of health_alert
-- ----------------------------

-- ----------------------------
-- Table structure for health_alert_rule
-- ----------------------------
DROP TABLE IF EXISTS `health_alert_rule`;
CREATE TABLE `health_alert_rule`  (
  `rule_id` bigint NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `rule_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '规则名称',
  `alert_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '预警类型',
  `alert_level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '预警级别',
  `min_threshold` decimal(10, 2) NULL DEFAULT NULL COMMENT '最小阈值',
  `max_threshold` decimal(10, 2) NULL DEFAULT NULL COMMENT '最大阈值',
  `message_template` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '消息模板',
  `min_age` int NULL DEFAULT NULL COMMENT '适用最小年龄',
  `max_age` int NULL DEFAULT NULL COMMENT '适用最大年龄',
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '适用性别（MALE, FEMALE, ALL）',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`rule_id`) USING BTREE,
  UNIQUE INDEX `rule_name`(`rule_name` ASC) USING BTREE,
  INDEX `idx_alert_type`(`alert_type` ASC) USING BTREE,
  INDEX `idx_enabled`(`enabled` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '健康预警规则表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of health_alert_rule
-- ----------------------------
INSERT INTO `health_alert_rule` VALUES (1, '体温过低-严重', 'TEMPERATURE', 'CRITICAL', NULL, 35.00, '体温严重偏低：{value}°C，正常范围：36.0-37.5°C，请立即就医！', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');
INSERT INTO `health_alert_rule` VALUES (2, '体温过低-中等', 'TEMPERATURE', 'MEDIUM', 35.00, 36.00, '体温偏低：{value}°C，正常范围：36.0-37.5°C，请注意保暖', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');
INSERT INTO `health_alert_rule` VALUES (3, '体温过高-中等', 'TEMPERATURE', 'MEDIUM', 37.50, 38.50, '体温偏高：{value}°C，正常范围：36.0-37.5°C，请注意休息', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');
INSERT INTO `health_alert_rule` VALUES (4, '体温过高-严重', 'TEMPERATURE', 'HIGH', 38.50, 39.50, '体温过高：{value}°C，正常范围：36.0-37.5°C，建议就医', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');
INSERT INTO `health_alert_rule` VALUES (5, '体温过高-危急', 'TEMPERATURE', 'CRITICAL', 39.50, NULL, '体温危急：{value}°C，正常范围：36.0-37.5°C，请立即就医！', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');
INSERT INTO `health_alert_rule` VALUES (6, '收缩压过低-严重', 'BLOOD_PRESSURE_SYSTOLIC', 'HIGH', NULL, 90.00, '收缩压过低：{value}mmHg，正常范围：90-140mmHg，请注意监测', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');
INSERT INTO `health_alert_rule` VALUES (7, '收缩压过高-中等', 'BLOOD_PRESSURE_SYSTOLIC', 'MEDIUM', 140.00, 160.00, '收缩压偏高：{value}mmHg，正常范围：90-140mmHg，请注意饮食', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');
INSERT INTO `health_alert_rule` VALUES (8, '收缩压过高-严重', 'BLOOD_PRESSURE_SYSTOLIC', 'HIGH', 160.00, 180.00, '收缩压过高：{value}mmHg，正常范围：90-140mmHg，建议就医', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');
INSERT INTO `health_alert_rule` VALUES (9, '收缩压过高-危急', 'BLOOD_PRESSURE_SYSTOLIC', 'CRITICAL', 180.00, NULL, '收缩压危急：{value}mmHg，正常范围：90-140mmHg，请立即就医！', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');
INSERT INTO `health_alert_rule` VALUES (10, '舒张压过低-严重', 'BLOOD_PRESSURE_DIASTOLIC', 'HIGH', NULL, 60.00, '舒张压过低：{value}mmHg，正常范围：60-90mmHg，请注意监测', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');
INSERT INTO `health_alert_rule` VALUES (11, '舒张压过高-中等', 'BLOOD_PRESSURE_DIASTOLIC', 'MEDIUM', 90.00, 100.00, '舒张压偏高：{value}mmHg，正常范围：60-90mmHg，请注意饮食', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');
INSERT INTO `health_alert_rule` VALUES (12, '舒张压过高-严重', 'BLOOD_PRESSURE_DIASTOLIC', 'HIGH', 100.00, 110.00, '舒张压过高：{value}mmHg，正常范围：60-90mmHg，建议就医', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');
INSERT INTO `health_alert_rule` VALUES (13, '舒张压过高-危急', 'BLOOD_PRESSURE_DIASTOLIC', 'CRITICAL', 110.00, NULL, '舒张压危急：{value}mmHg，正常范围：60-90mmHg，请立即就医！', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');
INSERT INTO `health_alert_rule` VALUES (14, '心率过低-中等', 'HEART_RATE', 'MEDIUM', NULL, 60.00, '心率偏低：{value}次/分，正常范围：60-100次/分，请注意监测', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');
INSERT INTO `health_alert_rule` VALUES (15, '心率过低-严重', 'HEART_RATE', 'HIGH', NULL, 50.00, '心率过低：{value}次/分，正常范围：60-100次/分，建议就医', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');
INSERT INTO `health_alert_rule` VALUES (16, '心率过高-中等', 'HEART_RATE', 'MEDIUM', 100.00, 120.00, '心率偏高：{value}次/分，正常范围：60-100次/分，请注意休息', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');
INSERT INTO `health_alert_rule` VALUES (17, '心率过高-严重', 'HEART_RATE', 'HIGH', 120.00, 150.00, '心率过高：{value}次/分，正常范围：60-100次/分，建议就医', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');
INSERT INTO `health_alert_rule` VALUES (18, '心率过高-危急', 'HEART_RATE', 'CRITICAL', 150.00, NULL, '心率危急：{value}次/分，正常范围：60-100次/分，请立即就医！', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');
INSERT INTO `health_alert_rule` VALUES (19, 'BMI过低-中等', 'BMI', 'MEDIUM', NULL, 18.50, 'BMI偏低：{value}，正常范围：18.5-24.9，建议增加营养', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');
INSERT INTO `health_alert_rule` VALUES (20, 'BMI过高-中等', 'BMI', 'MEDIUM', 24.90, 28.00, 'BMI偏高：{value}，正常范围：18.5-24.9，建议控制饮食', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');
INSERT INTO `health_alert_rule` VALUES (21, 'BMI过高-严重', 'BMI', 'HIGH', 28.00, 32.00, 'BMI过高：{value}，正常范围：18.5-24.9，建议就医咨询', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');
INSERT INTO `health_alert_rule` VALUES (22, 'BMI过高-危急', 'BMI', 'CRITICAL', 32.00, NULL, 'BMI严重超标：{value}，正常范围：18.5-24.9，请立即就医！', NULL, NULL, 'ALL', 1, '2025-06-29 14:35:55', '2025-06-29 14:35:55');

-- ----------------------------
-- Table structure for health_record
-- ----------------------------
DROP TABLE IF EXISTS `health_record`;
CREATE TABLE `health_record`  (
  `record_id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `elder_id` bigint NULL DEFAULT NULL COMMENT '老人ID',
  `record_type` tinyint(1) NULL DEFAULT NULL COMMENT '记录类型（1：体温，2：血压，3：血糖，4：心率，5：血氧，6：体重，7：其他）',
  `record_time` datetime NULL DEFAULT NULL COMMENT '记录时间',
  `temperature` float(5, 2) NULL DEFAULT NULL COMMENT '体温值（℃）',
  `systolic_pressure` int NULL DEFAULT NULL COMMENT '收缩压（mmHg）',
  `diastolic_pressure` int NULL DEFAULT NULL COMMENT '舒张压（mmHg）',
  `blood_sugar` float(5, 2) NULL DEFAULT NULL COMMENT '血糖值（mmol/L）',
  `heart_rate` int NULL DEFAULT NULL COMMENT '心率（次/分）',
  `blood_oxygen` int NULL DEFAULT NULL COMMENT '血氧饱和度（%）',
  `weight` float(5, 2) NULL DEFAULT NULL COMMENT '体重（kg）',
  `other_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '其他指标值',
  `device_id` bigint NULL DEFAULT NULL COMMENT '测量设备ID',
  `record_source` tinyint(1) NULL DEFAULT NULL COMMENT '记录来源（1：设备自动记录，2：人工录入）',
  `is_abnormal` tinyint(1) NULL DEFAULT 0 COMMENT '是否异常（0：正常，1：异常）',
  `abnormal_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '异常描述',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`record_id`) USING BTREE,
  INDEX `idx_elder_id`(`elder_id` ASC) USING BTREE,
  INDEX `idx_device_id`(`device_id` ASC) USING BTREE,
  INDEX `idx_record_time`(`record_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '健康记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of health_record
-- ----------------------------

-- ----------------------------
-- Table structure for health_warning
-- ----------------------------
DROP TABLE IF EXISTS `health_warning`;
CREATE TABLE `health_warning`  (
  `warning_id` bigint NOT NULL AUTO_INCREMENT COMMENT '预警ID',
  `elder_id` bigint NULL DEFAULT NULL COMMENT '老人ID',
  `record_id` bigint NULL DEFAULT NULL COMMENT '健康记录ID',
  `warning_type` tinyint(1) NULL DEFAULT NULL COMMENT '预警类型（1：体温异常，2：血压异常，3：血糖异常，4：心率异常，5：血氧异常，6：体重异常，7：其他）',
  `warning_level` tinyint(1) NULL DEFAULT NULL COMMENT '预警级别（1：轻度，2：中度，3：重度）',
  `warning_time` datetime NULL DEFAULT NULL COMMENT '预警时间',
  `warning_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '预警内容',
  `process_status` tinyint(1) NULL DEFAULT 0 COMMENT '处理状态（0：未处理，1：已处理）',
  `process_time` datetime NULL DEFAULT NULL COMMENT '处理时间',
  `process_user_id` bigint NULL DEFAULT NULL COMMENT '处理人ID',
  `process_result` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '处理结果',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`warning_id`) USING BTREE,
  INDEX `idx_elder_id`(`elder_id` ASC) USING BTREE,
  INDEX `idx_record_id`(`record_id` ASC) USING BTREE,
  INDEX `idx_warning_time`(`warning_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '健康预警表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of health_warning
-- ----------------------------

-- ----------------------------
-- Table structure for institution_info
-- ----------------------------
DROP TABLE IF EXISTS `institution_info`;
CREATE TABLE `institution_info`  (
  `institution_id` bigint NOT NULL AUTO_INCREMENT COMMENT '机构ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '机构名称',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址',
  `contact_person` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `bed_total` int NULL DEFAULT 0 COMMENT '总床位数',
  `bed_available` int NULL DEFAULT 0 COMMENT '可用床位数',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '简介',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '机构类型',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '运营中' COMMENT '机构状态：运营中、筹备中、暂停服务',
  PRIMARY KEY (`institution_id`) USING BTREE,
  INDEX `idx_institution_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '养老机构信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of institution_info
-- ----------------------------

-- ----------------------------
-- Table structure for intervention_execution
-- ----------------------------
DROP TABLE IF EXISTS `intervention_execution`;
CREATE TABLE `intervention_execution`  (
  `execution_id` bigint NOT NULL AUTO_INCREMENT COMMENT '执行记录ID',
  `plan_id` bigint NOT NULL COMMENT '干预方案ID',
  `execution_date` date NOT NULL COMMENT '执行日期',
  `execution_time` time NULL DEFAULT NULL COMMENT '执行时间',
  `execution_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '执行状态：COMPLETED(已完成), PARTIAL(部分完成), MISSED(未执行)',
  `completion_rate` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '完成率',
  `execution_details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '执行详情',
  `side_effects` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '副作用或不良反应',
  `patient_feedback` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '患者反馈',
  `executor_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '执行人姓名',
  `execution_location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '执行地点',
  `vital_signs_before` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '执行前生命体征（JSON格式）',
  `vital_signs_after` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '执行后生命体征（JSON格式）',
  `effectiveness_rating` int NULL DEFAULT NULL COMMENT '效果评价(1-5)',
  `notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '备注',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`execution_id`) USING BTREE,
  INDEX `idx_plan_id`(`plan_id` ASC) USING BTREE,
  INDEX `idx_execution_date`(`execution_date` ASC) USING BTREE,
  INDEX `idx_execution_status`(`execution_status` ASC) USING BTREE,
  CONSTRAINT `intervention_execution_ibfk_1` FOREIGN KEY (`plan_id`) REFERENCES `intervention_plan` (`plan_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '干预执行记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of intervention_execution
-- ----------------------------

-- ----------------------------
-- Table structure for intervention_plan
-- ----------------------------
DROP TABLE IF EXISTS `intervention_plan`;
CREATE TABLE `intervention_plan`  (
  `plan_id` bigint NOT NULL AUTO_INCREMENT COMMENT '计划ID',
  `elder_id` bigint NULL DEFAULT NULL COMMENT '老人ID',
  `plan_type` tinyint(1) NULL DEFAULT NULL COMMENT '干预类型（1：饮食，2：运动，3：心理，4：用药，5：综合）',
  `plan_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '干预内容',
  `start_date` date NULL DEFAULT NULL COMMENT '开始时间',
  `end_date` date NULL DEFAULT NULL COMMENT '结束时间',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态（0：执行中，1：已完成，2：中止）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`plan_id`) USING BTREE,
  INDEX `idx_elder_id`(`elder_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '健康干预计划表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of intervention_plan
-- ----------------------------

-- ----------------------------
-- Table structure for intervention_template
-- ----------------------------
DROP TABLE IF EXISTS `intervention_template`;
CREATE TABLE `intervention_template`  (
  `template_id` bigint NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `template_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板名称',
  `template_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板分类',
  `alert_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '适用的预警类型',
  `priority_level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '建议优先级',
  `template_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板内容',
  `implementation_guide` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '实施指南',
  `precautions` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '注意事项',
  `expected_outcomes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '预期效果',
  `is_active` tinyint(1) NULL DEFAULT 1 COMMENT '是否启用',
  `usage_count` int NULL DEFAULT 0 COMMENT '使用次数',
  `created_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`template_id`) USING BTREE,
  INDEX `idx_category`(`template_category` ASC) USING BTREE,
  INDEX `idx_alert_type`(`alert_type` ASC) USING BTREE,
  INDEX `idx_active`(`is_active` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '干预模板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of intervention_template
-- ----------------------------
INSERT INTO `intervention_template` VALUES (1, '高血压饮食干预', 'DIET', 'BLOOD_PRESSURE_SYSTOLIC', 'HIGH', '低盐低脂饮食，每日盐摄入量控制在6g以下，增加蔬菜水果摄入', '1. 制定详细饮食计划\n2. 定期营养咨询\n3. 家属配合监督', '避免高钠食物，注意血糖变化', '血压下降5-10mmHg，改善心血管健康', 1, 0, NULL, '2025-06-29 21:32:54', '2025-06-29 21:32:54');
INSERT INTO `intervention_template` VALUES (2, '心率异常监测方案', 'MONITORING', 'HEART_RATE', 'HIGH', '24小时心率监测，每日记录心率变化，异常时及时就医', '1. 佩戴心率监测设备\n2. 定时记录数据\n3. 建立预警机制', '避免剧烈运动，注意休息', '心率稳定在正常范围，减少心律不齐发生', 1, 1, NULL, '2025-06-29 21:32:54', '2025-06-29 21:32:54');
INSERT INTO `intervention_template` VALUES (3, '体温异常护理', 'MEDICAL', 'TEMPERATURE', 'URGENT', '密切监测体温变化，物理降温，必要时药物治疗', '1. 每2小时测量体温\n2. 物理降温措施\n3. 及时医疗干预', '注意水电解质平衡，防止脱水', '体温恢复正常，避免并发症', 1, 1, NULL, '2025-06-29 21:32:54', '2025-06-29 21:32:54');
INSERT INTO `intervention_template` VALUES (4, '睡眠质量改善计划', 'LIFESTYLE', 'SLEEP', 'MEDIUM', '建立规律作息，改善睡眠环境，必要时心理疏导', '1. 制定作息时间表\n2. 优化睡眠环境\n3. 放松训练', '避免睡前刺激性活动，注意安全', '睡眠时间达到7-8小时，睡眠质量提升', 1, 0, NULL, '2025-06-29 21:32:54', '2025-06-29 21:32:54');
INSERT INTO `intervention_template` VALUES (5, 'BMI控制运动方案', 'EXERCISE', 'BMI', 'MEDIUM', '制定个性化运动计划，循序渐进增加运动量', '1. 评估身体状况\n2. 制定运动计划\n3. 定期调整强度', '注意运动安全，避免过度疲劳', 'BMI控制在正常范围，提高身体素质', 1, 1, NULL, '2025-06-29 21:32:54', '2025-06-29 21:32:54');

-- ----------------------------
-- Table structure for medical_appointment
-- ----------------------------
DROP TABLE IF EXISTS `medical_appointment`;
CREATE TABLE `medical_appointment`  (
  `appointment_id` bigint NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  `elder_id` bigint NULL DEFAULT NULL COMMENT '老人ID',
  `doctor_id` bigint NULL DEFAULT NULL COMMENT '医生ID',
  `appointment_time` datetime NULL DEFAULT NULL COMMENT '预约时间',
  `appointment_type` tinyint(1) NULL DEFAULT NULL COMMENT '类型（1：门诊，2：上门，3：检查）',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态（0：待就诊，1：已完成，2：已取消）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`appointment_id`) USING BTREE,
  INDEX `idx_elder_id`(`elder_id` ASC) USING BTREE,
  INDEX `idx_doctor_id`(`doctor_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '医疗预约表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of medical_appointment
-- ----------------------------
INSERT INTO `medical_appointment` VALUES (1, 3, 1, '2025-06-28 00:00:00', 1, 1, '', '2025-06-27 17:05:41', '2025-06-27 17:05:41');
INSERT INTO `medical_appointment` VALUES (2, 2, 1, '2025-06-28 00:00:00', 1, 1, '', '2025-06-27 17:06:25', '2025-06-27 17:06:25');

-- ----------------------------
-- Table structure for service_schedule
-- ----------------------------
DROP TABLE IF EXISTS `service_schedule`;
CREATE TABLE `service_schedule`  (
  `schedule_id` bigint NOT NULL AUTO_INCREMENT COMMENT '调度ID',
  `service_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务名称',
  `service_type` int NOT NULL COMMENT '服务类型（1：日常护理，2：医疗服务，3：康复训练，4：心理疏导，5：营养配餐，6：清洁卫生）',
  `elder_id` bigint NOT NULL COMMENT '老人ID',
  `staff_id` bigint NOT NULL COMMENT '服务人员ID',
  `scheduled_start_time` datetime NOT NULL COMMENT '计划开始时间',
  `scheduled_end_time` datetime NOT NULL COMMENT '计划结束时间',
  `actual_start_time` datetime NULL DEFAULT NULL COMMENT '实际开始时间',
  `actual_end_time` datetime NULL DEFAULT NULL COMMENT '实际结束时间',
  `status` int NOT NULL DEFAULT 1 COMMENT '服务状态（1：待执行，2：进行中，3：已完成，4：已取消）',
  `priority` int NOT NULL DEFAULT 2 COMMENT '优先级（1：低，2：中，3：高，4：紧急）',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务描述',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '服务备注',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`schedule_id`) USING BTREE,
  INDEX `idx_elder_id`(`elder_id` ASC) USING BTREE,
  INDEX `idx_staff_id`(`staff_id` ASC) USING BTREE,
  INDEX `idx_service_type`(`service_type` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_priority`(`priority` ASC) USING BTREE,
  INDEX `idx_scheduled_time`(`scheduled_start_time` ASC, `scheduled_end_time` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_deleted`(`deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '服务调度表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of service_schedule
-- ----------------------------

-- ----------------------------
-- Table structure for sms_record
-- ----------------------------
DROP TABLE IF EXISTS `sms_record`;
CREATE TABLE `sms_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '短信内容',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '短信类型：normal-普通短信，template-模板短信，batch-批量短信',
  `template_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模板代码',
  `template_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模板名称',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送状态：success-成功，failed-失败，sending-发送中',
  `send_time` datetime NOT NULL COMMENT '发送时间',
  `response_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '响应代码',
  `error_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '错误信息',
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '模板参数（JSON格式）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_phone`(`phone` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_type`(`type` ASC) USING BTREE,
  INDEX `idx_send_time`(`send_time` ASC) USING BTREE,
  INDEX `idx_template_code`(`template_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '短信发送记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_record
-- ----------------------------
INSERT INTO `sms_record` VALUES (9, '15983660989', '【云护CloudCare平台】感谢您的注册，欢迎来到云护CloudCare智慧系统。', 'template', 'REGISTER_WELCOME', '注册欢迎', 'success', '2025-07-01 12:03:55', '0', NULL, NULL, '2025-07-01 12:03:55', '2025-07-01 12:03:55');
INSERT INTO `sms_record` VALUES (10, '15200813540', '哈基米啊系噶', 'normal', NULL, NULL, 'success', '2025-07-01 14:13:55', '0', NULL, NULL, '2025-07-01 14:13:54', '2025-07-01 14:13:54');
INSERT INTO `sms_record` VALUES (11, '15200813540', '【云护CloudCare】老人老人1已进入电子围栏\"社区活动区域\"，当前位置：纬度22.699380，经度114.125039，事件时间：2025-07-03 20:48:21，请及时关注。', 'normal', NULL, NULL, 'success', '2025-07-03 20:48:22', '0', NULL, NULL, '2025-07-03 20:48:22', '2025-07-03 20:48:22');
INSERT INTO `sms_record` VALUES (12, '15200813540', '【云护CloudCare】老人老人1已离开电子围栏\"家庭安全区域\"，当前位置：纬度22.703500，经度114.131000，事件时间：2025-07-03 20:59:07，请及时关注。', 'normal', NULL, NULL, 'success', '2025-07-03 20:59:08', '0', NULL, NULL, '2025-07-03 20:59:08', '2025-07-03 20:59:08');
INSERT INTO `sms_record` VALUES (13, '15200813540', '【云护CloudCare】老人老人1已离开电子围栏\"社区活动区域\"，当前位置：纬度22.703500，经度114.131000，事件时间：2025-07-03 20:59:08，请及时关注。', 'normal', NULL, NULL, 'success', '2025-07-03 20:59:08', '0', NULL, NULL, '2025-07-03 20:59:08', '2025-07-03 20:59:08');
INSERT INTO `sms_record` VALUES (14, '15200813540', '【云护CloudCare】老人老人1已离开电子围栏\"家庭安全区域\"，当前位置：纬度22.699370，经度114.125030，事件时间：2025-07-04 16:11:29，请及时关注。', 'normal', NULL, NULL, 'success', '2025-07-04 16:11:30', '0', NULL, NULL, '2025-07-04 16:11:30', '2025-07-04 16:11:30');
INSERT INTO `sms_record` VALUES (15, '15983660989', '【云护CloudCare】老人老人1已离开电子围栏\"家庭安全区域\"，当前位置：纬度22.699370，经度114.125030，事件时间：2025-07-04 16:11:29，请及时关注。', 'normal', NULL, NULL, 'success', '2025-07-04 16:11:30', '0', NULL, NULL, '2025-07-04 16:11:30', '2025-07-04 16:11:30');
INSERT INTO `sms_record` VALUES (16, '15200813540', '【云护CloudCare】老人老人1已离开电子围栏\"家庭安全区域\"，当前位置：纬度30.780000，经度103.850000，事件时间：2025-07-04 16:12:19，请及时关注。', 'normal', NULL, NULL, 'success', '2025-07-04 16:12:20', '0', NULL, NULL, '2025-07-04 16:12:19', '2025-07-04 16:12:19');
INSERT INTO `sms_record` VALUES (17, '15983660989', '【云护CloudCare】老人老人1已离开电子围栏\"家庭安全区域\"，当前位置：纬度30.780000，经度103.850000，事件时间：2025-07-04 16:12:19，请及时关注。', 'normal', NULL, NULL, 'success', '2025-07-04 16:12:20', '0', NULL, NULL, '2025-07-04 16:12:19', '2025-07-04 16:12:19');
INSERT INTO `sms_record` VALUES (18, '15200813540', '【云护CloudCare】老人老人1已离开电子围栏\"家庭安全区域\"，当前位置：纬度22.699370，经度114.125030，事件时间：2025-07-04 16:13:54，请及时关注。', 'normal', NULL, NULL, 'success', '2025-07-04 16:13:54', '0', NULL, NULL, '2025-07-04 16:13:54', '2025-07-04 16:13:54');
INSERT INTO `sms_record` VALUES (19, '15983660989', '【云护CloudCare】老人老人1已离开电子围栏\"家庭安全区域\"，当前位置：纬度22.699370，经度114.125030，事件时间：2025-07-04 16:13:54，请及时关注。', 'normal', NULL, NULL, 'success', '2025-07-04 16:13:54', '0', NULL, NULL, '2025-07-04 16:13:54', '2025-07-04 16:13:54');
INSERT INTO `sms_record` VALUES (20, '15200813540', '【云护CloudCare】老人老人1已离开电子围栏\"家庭安全区域\"，当前位置：纬度22.699370，经度114.125030，事件时间：2025-07-04 16:14:49，请及时关注。', 'normal', NULL, NULL, 'success', '2025-07-04 16:14:50', '0', NULL, NULL, '2025-07-04 16:14:50', '2025-07-04 16:14:50');
INSERT INTO `sms_record` VALUES (21, '15983660989', '【云护CloudCare】老人老人1已离开电子围栏\"家庭安全区域\"，当前位置：纬度22.699370，经度114.125030，事件时间：2025-07-04 16:14:49，请及时关注。', 'normal', NULL, NULL, 'success', '2025-07-04 16:14:50', '0', NULL, NULL, '2025-07-04 16:14:50', '2025-07-04 16:14:50');
INSERT INTO `sms_record` VALUES (22, '15200813540', '【云护CloudCare】老人老人1已进入电子围栏\"家庭安全区域\"，当前位置：纬度30.783676，经度103.858170，事件时间：2025-07-04 16:35:02，请及时关注。', 'normal', NULL, NULL, 'success', '2025-07-04 16:35:07', '0', NULL, NULL, '2025-07-04 16:35:06', '2025-07-04 16:35:06');
INSERT INTO `sms_record` VALUES (23, '15983660989', '【云护CloudCare】老人老人1已进入电子围栏\"家庭安全区域\"，当前位置：纬度30.783676，经度103.858170，事件时间：2025-07-04 16:35:02，请及时关注。', 'normal', NULL, NULL, 'success', '2025-07-04 16:35:07', '0', NULL, NULL, '2025-07-04 16:35:06', '2025-07-04 16:35:06');
INSERT INTO `sms_record` VALUES (24, '15200813540', '【云护CloudCare】老人老人1已进入电子围栏\"家庭安全区域\"，当前位置：纬度30.783676，经度103.858170，事件时间：2025-07-04 16:53:03，请及时关注。', 'normal', NULL, NULL, 'success', '2025-07-04 16:53:08', '0', NULL, NULL, '2025-07-04 16:53:07', '2025-07-04 16:53:07');
INSERT INTO `sms_record` VALUES (25, '15983660989', '【云护CloudCare】老人老人1已进入电子围栏\"家庭安全区域\"，当前位置：纬度30.783676，经度103.858170，事件时间：2025-07-04 16:53:03，请及时关注。', 'normal', NULL, NULL, 'success', '2025-07-04 16:53:08', '0', NULL, NULL, '2025-07-04 16:53:07', '2025-07-04 16:53:07');
INSERT INTO `sms_record` VALUES (26, '15200813540', '【云护CloudCare】老人老人1已进入电子围栏\"家庭安全区域\"，当前位置：纬度30.783676，经度103.85817，事件时间：2025-07-04 21:23:23，请及时关注。', 'template', 'FENCE_ENTER_ALERT', '围栏进入提醒', 'success', '2025-07-04 21:32:01', '0', NULL, '{\"latitude\":\"30.783676\",\"elder_name\":\"老人1\",\"fence_name\":\"家庭安全区域\",\"event_time\":\"2025-07-04 21:23:23\",\"longitude\":\"103.85817\"}', '2025-07-04 21:32:01', '2025-07-04 21:32:01');
INSERT INTO `sms_record` VALUES (27, '15983660989', '【云护CloudCare】老人老人1已进入电子围栏\"家庭安全区域\"，当前位置：纬度30.783676，经度103.85817，事件时间：2025-07-04 21:23:23，请及时关注。', 'template', 'FENCE_ENTER_ALERT', '围栏进入提醒', 'success', '2025-07-04 21:32:02', '0', NULL, '{\"latitude\":\"30.783676\",\"elder_name\":\"老人1\",\"fence_name\":\"家庭安全区域\",\"event_time\":\"2025-07-04 21:23:23\",\"longitude\":\"103.85817\"}', '2025-07-04 21:32:01', '2025-07-04 21:32:01');
INSERT INTO `sms_record` VALUES (28, '15200813540', '【云护CloudCare】老人老人1已进入电子围栏\"家庭安全区域\"，当前位置：纬度30.783676，经度103.85817，事件时间：2025-07-04 21:38:38，请及时关注。', 'template', 'FENCE_ENTER_ALERT', '围栏进入提醒', 'success', '2025-07-04 21:38:43', '0', NULL, '{\"latitude\":\"30.783676\",\"elder_name\":\"老人1\",\"fence_name\":\"家庭安全区域\",\"event_time\":\"2025-07-04 21:38:38\",\"longitude\":\"103.85817\"}', '2025-07-04 21:38:42', '2025-07-04 21:38:42');
INSERT INTO `sms_record` VALUES (29, '15983660989', '【云护CloudCare】老人老人1已进入电子围栏\"家庭安全区域\"，当前位置：纬度30.783676，经度103.85817，事件时间：2025-07-04 21:38:38，请及时关注。', 'template', 'FENCE_ENTER_ALERT', '围栏进入提醒', 'success', '2025-07-04 21:38:43', '0', NULL, '{\"latitude\":\"30.783676\",\"elder_name\":\"老人1\",\"fence_name\":\"家庭安全区域\",\"event_time\":\"2025-07-04 21:38:38\",\"longitude\":\"103.85817\"}', '2025-07-04 21:38:42', '2025-07-04 21:38:42');
INSERT INTO `sms_record` VALUES (30, '15200813540', '【云护CloudCare】老人老人1已离开电子围栏\"家庭安全区域\"，当前位置：纬度22.6993799，经度114.1250394，事件时间：2025-07-04 21:39:08.961250700，请及时关注。', 'template', 'FENCE_LEAVE_ALERT', '围栏离开提醒', 'success', '2025-07-04 21:39:09', '0', NULL, '{\"latitude\":\"22.6993799\",\"elder_name\":\"老人1\",\"fence_name\":\"家庭安全区域\",\"event_time\":\"2025-07-04 21:39:08.961250700\",\"longitude\":\"114.1250394\"}', '2025-07-04 21:39:09', '2025-07-04 21:39:09');
INSERT INTO `sms_record` VALUES (31, '15983660989', '【云护CloudCare】老人老人1已离开电子围栏\"家庭安全区域\"，当前位置：纬度22.6993799，经度114.1250394，事件时间：2025-07-04 21:39:08.961250700，请及时关注。', 'template', 'FENCE_LEAVE_ALERT', '围栏离开提醒', 'success', '2025-07-04 21:39:09', '0', NULL, '{\"latitude\":\"22.6993799\",\"elder_name\":\"老人1\",\"fence_name\":\"家庭安全区域\",\"event_time\":\"2025-07-04 21:39:08.961250700\",\"longitude\":\"114.1250394\"}', '2025-07-04 21:39:09', '2025-07-04 21:39:09');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `gender` tinyint(1) NULL DEFAULT 0 COMMENT '性别（0：未知，1：男，2：女）',
  `user_type` tinyint(1) NULL DEFAULT NULL COMMENT '用户类型（1：管理员，2：医生，3：老人）',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态（0：禁用，1：正常）',
  `login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  `login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志（0：未删除，1：已删除）',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '1234', '$2a$10$GScvbYmdvHGuZGs1E3gABusaOGdCBDMHfGnroBM.5LSYxF7stRouO', 'dzx', '15983660989', '3296416743@qq.com', '/api/files/avatar/6f510432-12f9-49b6-b67c-6cb98f35021e.png', 0, 1, 1, '0:0:0:0:0:0:0:1', '2025-07-05 08:53:21', 'system', '2025-06-27 11:14:01', '1234', '2025-07-05 09:05:56', NULL, 0);
INSERT INTO `sys_user` VALUES (2, '哈基米66', '$2a$10$zKltzU3hC4FU3EKrvZtEJO5XrpOLMP8hbu0cl3boAi36IZIJ6n.6S', 'lr', '15794634924', NULL, NULL, 0, 3, 1, NULL, NULL, 'system', '2025-06-27 19:45:07', '1234', '2025-06-27 21:18:46', NULL, 0);
INSERT INTO `sys_user` VALUES (3, '哈基米曼波', '$2a$10$bSJI.fKoSdMico1oKrV09evV67gAgefHEG1frhtDOHBQsomLJKtp2', '张籽岷', '15200813540', NULL, NULL, 0, 1, 1, NULL, NULL, 'system', '2025-06-30 16:40:48', NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (4, 'test063016', '$2a$10$1PunY3csOEGolH8fXYDHf.zawBeVFrLSon.OY.4bQmv9K02qtsKvC', 'DZX', '15983660989', NULL, NULL, 0, 1, 1, NULL, NULL, 'system', '2025-06-30 16:58:12', NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (5, 'test063017', '$2a$10$cpYtGNhNJMwo2DFvPXkPD.K6u8ebbxujbS5CYYyrErnbDitUlZ8lq', 'dzx', '15983660989', NULL, NULL, 0, 1, 1, NULL, NULL, 'system', '2025-06-30 17:51:28', NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (6, 'test063018', '$2a$10$aWTcr8ERnzxrmbVp3o0lSeoJn/CPxgn31jGiHNmNGiICkO3O2Tsby', 'DZX', '15983660989', NULL, NULL, 0, 1, 1, NULL, NULL, 'system', '2025-06-30 17:54:21', NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (7, 'test063019', '$2a$10$0Fiae.HKIey7kia9yst6g.J699OtScX6sBGDYChd.IAjj1Dt1CFsy', 'DZX', '15983660989', NULL, NULL, 0, 1, 1, NULL, NULL, 'system', '2025-06-30 18:10:52', NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (8, 'test063020', '$2a$10$AY6zwpHRq9MrJuZaCLoVyuDZUetKU7JPZxDiVKjl5UEZhI8AVOe56', 'dzx', '15983660989', NULL, NULL, 0, 1, 1, NULL, NULL, 'system', '2025-06-30 18:22:20', NULL, NULL, NULL, 0);
INSERT INTO `sys_user` VALUES (9, 'test062918', '$2a$10$jQT85FDl5IhjbV/W08VYkunRvZ6vzFBQAaEjOIkGGR/FTJx7g4VaK', 'dzx', '15983660989', NULL, NULL, 0, 1, 1, NULL, NULL, 'system', '2025-06-30 18:35:22', NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for system_log
-- ----------------------------
DROP TABLE IF EXISTS `system_log`;
CREATE TABLE `system_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日志级别：DEBUG-调试，INFO-信息，WARN-警告，ERROR-错误',
  `module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模块名称：USER-用户，ELDERLY-老人档案，HEALTH-健康管理，MEDICAL-医疗服务，DEVICE-设备，SYSTEM-系统等',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作用户名',
  `user_id` bigint NULL DEFAULT NULL COMMENT '操作用户ID',
  `operation` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型/名称',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日志内容详情',
  `request_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求URL',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求方法：GET,POST,PUT,DELETE等',
  `request_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求参数（JSON格式）',
  `response_status` int NULL DEFAULT NULL COMMENT '响应状态码',
  `response_time` bigint NULL DEFAULT NULL COMMENT '响应时间（毫秒）',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户代理信息',
  `session_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会话ID',
  `stack_trace` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '异常堆栈信息（错误日志专用）',
  `business_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务ID（如老人ID、订单ID等）',
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_level`(`level` ASC) USING BTREE,
  INDEX `idx_module`(`module` ASC) USING BTREE,
  INDEX `idx_username`(`username` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_operation`(`operation` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_ip_address`(`ip_address` ASC) USING BTREE,
  INDEX `idx_business`(`business_id` ASC, `business_type` ASC) USING BTREE,
  INDEX `idx_level_module`(`level` ASC, `module` ASC) USING BTREE,
  INDEX `idx_time_level`(`create_time` ASC, `level` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_log
-- ----------------------------
INSERT INTO `system_log` VALUES (1, 'INFO', 'USER', 'admin', 1, '用户登录', '用户admin成功登录系统', '/api/auth/login', 'POST', '{\"username\":\"admin\"}', 200, 156, '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 'SESSION_001', NULL, '1', 'USER', '2025-07-01 20:52:27', '2025-07-01 20:52:27');
INSERT INTO `system_log` VALUES (2, 'WARN', 'ELDERLY', 'doctor1', 2, '档案修改', '修改老人档案信息，老人ID: 12345', '/api/elderly/update/12345', 'PUT', '{\"elderlyId\":12345,\"name\":\"张三\"}', 200, 234, '192.168.1.101', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 'SESSION_002', NULL, '12345', 'ELDERLY', '2025-07-01 20:52:27', '2025-07-01 20:52:27');
INSERT INTO `system_log` VALUES (3, 'ERROR', 'HEALTH', 'nurse1', 3, '数据同步', '健康数据同步失败：连接超时', '/api/health/sync', 'POST', '{\"deviceId\":\"BP001\"}', 500, 5000, '192.168.1.102', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 'SESSION_003', NULL, 'BP001', 'DEVICE', '2025-07-01 20:52:27', '2025-07-01 20:52:27');
INSERT INTO `system_log` VALUES (4, 'INFO', 'MEDICAL', 'admin', 1, '预约创建', '创建医疗预约，预约ID: AP001', '/api/medical/appointment', 'POST', '{\"elderlyId\":12345,\"doctorId\":1}', 201, 189, '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 'SESSION_001', NULL, 'AP001', 'APPOINTMENT', '2025-07-01 20:52:27', '2025-07-01 20:52:27');
INSERT INTO `system_log` VALUES (5, 'DEBUG', 'SYSTEM', 'system', NULL, '定时任务', '执行数据清理定时任务', NULL, NULL, NULL, NULL, 2345, '127.0.0.1', 'System Task', NULL, NULL, NULL, 'TASK', '2025-07-01 20:52:27', '2025-07-01 20:52:27');
INSERT INTO `system_log` VALUES (6, 'ERROR', 'DEVICE', 'technician1', 4, '设备故障', '血压计设备BP001出现通信故障', '/api/device/status/BP001', 'GET', NULL, 500, 1234, '192.168.1.103', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 'SESSION_004', NULL, 'BP001', 'DEVICE', '2025-07-01 20:52:27', '2025-07-01 20:52:27');
INSERT INTO `system_log` VALUES (7, 'INFO', 'USER', 'nurse2', 5, '密码修改', '用户nurse2修改登录密码', '/api/user/password', 'PUT', '{\"userId\":5}', 200, 167, '192.168.1.104', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 'SESSION_005', NULL, '5', 'USER', '2025-07-01 20:52:27', '2025-07-01 20:52:27');
INSERT INTO `system_log` VALUES (8, 'WARN', 'HEALTH', 'doctor2', 6, '异常数据', '检测到老人心率异常数据，心率: 120bpm', '/api/health/alert', 'POST', '{\"elderlyId\":12346,\"heartRate\":120}', 200, 298, '192.168.1.105', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 'SESSION_006', NULL, '12346', 'ELDERLY', '2025-07-01 20:52:27', '2025-07-01 20:52:27');
INSERT INTO `system_log` VALUES (9, 'INFO', 'SYSTEM', 'admin', 1, '系统配置', '更新系统配置参数', '/api/system/config', 'PUT', '{\"maxLoginAttempts\":5}', 200, 145, '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 'SESSION_001', NULL, NULL, 'CONFIG', '2025-07-01 20:52:27', '2025-07-01 20:52:27');
INSERT INTO `system_log` VALUES (10, 'ERROR', 'MEDICAL', 'doctor1', 2, '预约冲突', '预约时间冲突，无法创建预约', '/api/medical/appointment', 'POST', '{\"elderlyId\":12347,\"doctorId\":2,\"time\":\"2024-01-15 10:00\"}', 409, 123, '192.168.1.101', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 'SESSION_002', NULL, NULL, 'APPOINTMENT', '2025-07-01 20:52:27', '2025-07-01 20:52:27');
INSERT INTO `system_log` VALUES (11, 'INFO', '用户认证', '1234', NULL, 'com.cloudcare.controller.AuthController.logout()', '{\"code\":200,\"data\":true,\"message\":\"退出成功\",\"success\":true,\"timestamp\":1751376787928}', '/api/auth/logout', 'POST', '', 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-01 21:33:08', '2025-07-01 21:33:08');
INSERT INTO `system_log` VALUES (12, 'INFO', '用户认证', '1234', NULL, 'com.cloudcare.controller.AuthController.login()', '{\"code\":200,\"data\":{\"realName\":\"dzx\",\"phone\":\"15983660989\",\"avatar\":\"/api/files/avatar/3eb8fdfc-8f76-49c4-9756-538359a3f6cb.png\",\"userType\":1,\"userId\":1,\"email\":\"3296416743@qq.com\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiMTIzNCIsInVzZXJUeXBlIjoxLCJpYXQiOjE3NTEzNzY3ODksImV4cCI6MTc1MTM4MDM4OX0.9CbKdSpdVXi2d5gI6OJA86hbDfxlwmH_wG4FC73sLfY\",\"username\":\"1234\",\"status\":1},\"message\":\"登录成功\",\"success\":true,\"timestamp\":1751376789081}', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-01 21:33:09', '2025-07-01 21:33:09');
INSERT INTO `system_log` VALUES (13, 'INFO', '用户认证', '1234', NULL, 'com.cloudcare.controller.AuthController.logout()', '{\"code\":200,\"data\":true,\"message\":\"退出成功\",\"success\":true,\"timestamp\":1751377072127}', '/api/auth/logout', 'POST', '', 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-01 21:37:52', '2025-07-01 21:37:52');
INSERT INTO `system_log` VALUES (14, 'INFO', '用户认证', '1234', NULL, 'com.cloudcare.controller.AuthController.login()', '{\"code\":200,\"data\":{\"realName\":\"dzx\",\"phone\":\"15983660989\",\"avatar\":\"/api/files/avatar/3eb8fdfc-8f76-49c4-9756-538359a3f6cb.png\",\"userType\":1,\"userId\":1,\"email\":\"3296416743@qq.com\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiMTIzNCIsInVzZXJUeXBlIjoxLCJpYXQiOjE3NTEzNzcwNzMsImV4cCI6MTc1MTM4MDY3M30.2U25zwr1UA2WGnJv4yhU4k9nPTGNDE6bAoxbkVo-pcM\",\"username\":\"1234\",\"status\":1},\"message\":\"登录成功\",\"success\":true,\"timestamp\":1751377073192}', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-01 21:37:53', '2025-07-01 21:37:53');
INSERT INTO `system_log` VALUES (15, 'INFO', '用户认证', '1234', NULL, 'com.cloudcare.controller.AuthController.logout()', '{\"code\":200,\"data\":true,\"message\":\"退出成功\",\"success\":true,\"timestamp\":1751377075344}', '/api/auth/logout', 'POST', '', 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-01 21:37:55', '2025-07-01 21:37:55');
INSERT INTO `system_log` VALUES (16, 'INFO', '用户认证', '1234', NULL, 'com.cloudcare.controller.AuthController.login()', '{\"code\":200,\"data\":{\"realName\":\"dzx\",\"phone\":\"15983660989\",\"avatar\":\"/api/files/avatar/3eb8fdfc-8f76-49c4-9756-538359a3f6cb.png\",\"userType\":1,\"userId\":1,\"email\":\"3296416743@qq.com\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiMTIzNCIsInVzZXJUeXBlIjoxLCJpYXQiOjE3NTEzNzcwNzYsImV4cCI6MTc1MTM4MDY3Nn0.f9dBpSs17lSCC9uP4hnCif6rvpPYTrNXseeGfzoDTLw\",\"username\":\"1234\",\"status\":1},\"message\":\"登录成功\",\"success\":true,\"timestamp\":1751377076200}', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-01 21:37:56', '2025-07-01 21:37:56');
INSERT INTO `system_log` VALUES (17, 'INFO', '用户认证', '1234', NULL, 'com.cloudcare.controller.AuthController.login()', '{\"code\":200,\"data\":{\"realName\":\"dzx\",\"phone\":\"15983660989\",\"avatar\":\"/api/files/avatar/3eb8fdfc-8f76-49c4-9756-538359a3f6cb.png\",\"userType\":1,\"userId\":1,\"email\":\"3296416743@qq.com\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiMTIzNCIsInVzZXJUeXBlIjoxLCJpYXQiOjE3NTE0NDYyODAsImV4cCI6MTc1MTQ0OTg4MH0.W0xlBxdaoSaIn9zBTp7Bp-yXRxVL_Sv2Q5fULRrZpFo\",\"username\":\"1234\",\"status\":1},\"message\":\"登录成功\",\"success\":true,\"timestamp\":1751446280409}', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-02 16:51:20', '2025-07-02 16:51:20');
INSERT INTO `system_log` VALUES (18, 'INFO', '用户认证', '1234', NULL, 'com.cloudcare.controller.AuthController.login()', '{\"code\":200,\"data\":{\"realName\":\"dzx\",\"phone\":\"15983660989\",\"avatar\":\"/api/files/avatar/3eb8fdfc-8f76-49c4-9756-538359a3f6cb.png\",\"userType\":1,\"userId\":1,\"email\":\"3296416743@qq.com\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiMTIzNCIsInVzZXJUeXBlIjoxLCJpYXQiOjE3NTE0NTcxOTYsImV4cCI6MTc1MTQ2MDc5Nn0.AaNg_saOKU8QAo39X4zF90rxb77rlaFm_9dbpah2hxM\",\"username\":\"1234\",\"status\":1},\"message\":\"登录成功\",\"success\":true,\"timestamp\":1751457196836}', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-02 19:53:17', '2025-07-02 19:53:17');
INSERT INTO `system_log` VALUES (19, 'INFO', '用户认证', '1234', NULL, 'com.cloudcare.controller.AuthController.login()', '{\"code\":200,\"data\":{\"realName\":\"dzx\",\"phone\":\"15983660989\",\"avatar\":\"/api/files/avatar/3eb8fdfc-8f76-49c4-9756-538359a3f6cb.png\",\"userType\":1,\"userId\":1,\"email\":\"3296416743@qq.com\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiMTIzNCIsInVzZXJUeXBlIjoxLCJpYXQiOjE3NTE0NjA0NjksImV4cCI6MTc1MTQ2NDA2OX0.95z1M5d9FYGwgFXmkwsJP3zTrLBTHWIVbBwu-qxw1sY\",\"username\":\"1234\",\"status\":1},\"message\":\"登录成功\",\"success\":true,\"timestamp\":1751460469298}', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-02 20:47:49', '2025-07-02 20:47:49');
INSERT INTO `system_log` VALUES (20, 'INFO', '用户认证', '1234', NULL, 'com.cloudcare.controller.AuthController.logout()', '{\"code\":200,\"data\":true,\"message\":\"退出成功\",\"success\":true,\"timestamp\":1751460639438}', '/api/auth/logout', 'POST', '', 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-02 20:50:39', '2025-07-02 20:50:39');
INSERT INTO `system_log` VALUES (21, 'INFO', '用户认证', '1234', NULL, 'com.cloudcare.controller.AuthController.login()', '{\"code\":200,\"data\":{\"realName\":\"dzx\",\"phone\":\"15983660989\",\"avatar\":\"/api/files/avatar/3eb8fdfc-8f76-49c4-9756-538359a3f6cb.png\",\"userType\":1,\"userId\":1,\"email\":\"3296416743@qq.com\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiMTIzNCIsInVzZXJUeXBlIjoxLCJpYXQiOjE3NTE0NjA2NDAsImV4cCI6MTc1MTQ2NDI0MH0.suV4WDBROKPfHe8moDW4re2AaNMqxv_NVWbuNIDMVtU\",\"username\":\"1234\",\"status\":1},\"message\":\"登录成功\",\"success\":true,\"timestamp\":1751460640161}', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-02 20:50:40', '2025-07-02 20:50:40');
INSERT INTO `system_log` VALUES (22, 'INFO', '用户认证', '1234', NULL, 'com.cloudcare.controller.AuthController.login()', '{\"code\":200,\"data\":{\"realName\":\"dzx\",\"phone\":\"15983660989\",\"avatar\":\"/api/files/avatar/3eb8fdfc-8f76-49c4-9756-538359a3f6cb.png\",\"userType\":1,\"userId\":1,\"email\":\"3296416743@qq.com\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiMTIzNCIsInVzZXJUeXBlIjoxLCJpYXQiOjE3NTE0NjA4MDksImV4cCI6MTc1MTQ2NDQwOX0.XzBkVfon-F7RwmP-F6r42TSrjJtk_y6j5yc78dIh0x0\",\"username\":\"1234\",\"status\":1},\"message\":\"登录成功\",\"success\":true,\"timestamp\":1751460809131}', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-02 20:53:29', '2025-07-02 20:53:29');
INSERT INTO `system_log` VALUES (23, 'INFO', '用户认证', '1234', NULL, 'com.cloudcare.controller.AuthController.login()', '{\"code\":200,\"data\":{\"realName\":\"dzx\",\"phone\":\"15983660989\",\"avatar\":\"/api/files/avatar/3eb8fdfc-8f76-49c4-9756-538359a3f6cb.png\",\"userType\":1,\"userId\":1,\"email\":\"3296416743@qq.com\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiMTIzNCIsInVzZXJUeXBlIjoxLCJpYXQiOjE3NTE1MDE3NzEsImV4cCI6MTc1MTUwNTM3MX0.Ku8MT0RV-v3wyv7MfZsa0y5lwCCzaOYCEWP9WBjnbIM\",\"username\":\"1234\",\"status\":1},\"message\":\"登录成功\",\"success\":true,\"timestamp\":1751501771880}', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-03 08:16:12', '2025-07-03 08:16:12');
INSERT INTO `system_log` VALUES (24, 'INFO', '系统日志', '1234', NULL, '导出日志', '导出日志数据，条件: 级别=, 模块=, 用户=', NULL, NULL, NULL, NULL, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, NULL, '2025-07-03 08:19:37', '2025-07-03 08:19:37');
INSERT INTO `system_log` VALUES (25, 'INFO', '用户认证', '1234', NULL, 'com.cloudcare.controller.AuthController.login()', '{\"code\":200,\"data\":{\"realName\":\"dzx\",\"phone\":\"15983660989\",\"avatar\":\"/api/files/avatar/3eb8fdfc-8f76-49c4-9756-538359a3f6cb.png\",\"userType\":1,\"userId\":1,\"email\":\"3296416743@qq.com\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiMTIzNCIsInVzZXJUeXBlIjoxLCJpYXQiOjE3NTE1MDUzODEsImV4cCI6MTc1MTUwODk4MX0.63sU6Gi6WGGVJV5_cCydLYBXkWYbcOiKW78avHTnV6M\",\"username\":\"1234\",\"status\":1},\"message\":\"登录成功\",\"success\":true,\"timestamp\":1751505381676}', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-03 09:16:22', '2025-07-03 09:16:21');
INSERT INTO `system_log` VALUES (26, 'INFO', '用户认证', '1234', NULL, 'com.cloudcare.controller.AuthController.login()', '{\"code\":200,\"data\":{\"realName\":\"dzx\",\"phone\":\"15983660989\",\"avatar\":\"/api/files/avatar/3eb8fdfc-8f76-49c4-9756-538359a3f6cb.png\",\"userType\":1,\"userId\":1,\"email\":\"3296416743@qq.com\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiMTIzNCIsInVzZXJUeXBlIjoxLCJpYXQiOjE3NTE1MTAxNjksImV4cCI6MTc1MTUxMzc2OX0.ayMEzaB0Uo4_4lVacJ_cvLVUogBbK49KgrHkpOKJNHM\",\"username\":\"1234\",\"status\":1},\"message\":\"登录成功\",\"success\":true,\"timestamp\":1751510169375}', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-03 10:36:09', '2025-07-03 10:36:09');
INSERT INTO `system_log` VALUES (27, 'INFO', '用户认证', '1234', NULL, 'com.cloudcare.controller.AuthController.login()', '{\"code\":200,\"data\":{\"realName\":\"dzx\",\"phone\":\"15983660989\",\"avatar\":\"/api/files/avatar/3eb8fdfc-8f76-49c4-9756-538359a3f6cb.png\",\"userType\":1,\"userId\":1,\"email\":\"3296416743@qq.com\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiMTIzNCIsInVzZXJUeXBlIjoxLCJpYXQiOjE3NTE1MTczOTAsImV4cCI6MTc1MTUyMDk5MH0.8k1_5o2wu-RJSog1CvaEGvUtom3Y6-1o7XDkcU_wQcI\",\"username\":\"1234\",\"status\":1},\"message\":\"登录成功\",\"success\":true,\"timestamp\":1751517390998}', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-03 12:36:31', '2025-07-03 12:36:31');
INSERT INTO `system_log` VALUES (28, 'INFO', '用户认证', '1234', NULL, 'com.cloudcare.controller.AuthController.login()', '{\"code\":200,\"data\":{\"realName\":\"dzx\",\"phone\":\"15983660989\",\"avatar\":\"/api/files/avatar/3eb8fdfc-8f76-49c4-9756-538359a3f6cb.png\",\"userType\":1,\"userId\":1,\"email\":\"3296416743@qq.com\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiMTIzNCIsInVzZXJUeXBlIjoxLCJpYXQiOjE3NTE1MjA2NzMsImV4cCI6MTc1MTUyNDI3M30.IRhN6a0Mw0lAzMgnd1mIkroD7vYVSZrl9TI3hTFpejc\",\"username\":\"1234\",\"status\":1},\"message\":\"登录成功\",\"success\":true,\"timestamp\":1751520673479}', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-03 13:31:13', '2025-07-03 13:31:13');
INSERT INTO `system_log` VALUES (29, 'INFO', '用户认证', '1234', NULL, 'com.cloudcare.controller.AuthController.login()', '{\"code\":200,\"data\":{\"realName\":\"dzx\",\"phone\":\"15983660989\",\"avatar\":\"/api/files/avatar/3eb8fdfc-8f76-49c4-9756-538359a3f6cb.png\",\"userType\":1,\"userId\":1,\"email\":\"3296416743@qq.com\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiMTIzNCIsInVzZXJUeXBlIjoxLCJpYXQiOjE3NTE1MjA5OTgsImV4cCI6MTc1MTUyNDU5OH0.3ia6vepH_1WsAljsatfl4YeRsHJpIb7RsCgy5xZKS4c\",\"username\":\"1234\",\"status\":1},\"message\":\"登录成功\",\"success\":true,\"timestamp\":1751520998274}', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-03 13:36:38', '2025-07-03 13:36:38');
INSERT INTO `system_log` VALUES (30, 'INFO', 'USER', '1234', NULL, '用户登录', '登录成功，用户名: 1234', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-03 19:49:11', '2025-07-03 19:49:10');
INSERT INTO `system_log` VALUES (31, 'INFO', 'USER', '1234', NULL, '用户登录', '登录成功，用户名: 1234', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-03 20:58:04', '2025-07-03 20:58:04');
INSERT INTO `system_log` VALUES (32, 'INFO', 'USER', '1234', NULL, '用户登录', '登录成功，用户名: 1234', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-04 08:17:02', '2025-07-04 08:17:02');
INSERT INTO `system_log` VALUES (33, 'INFO', 'USER', '1234', NULL, '用户登录', '登录成功，用户名: 1234', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-04 09:53:59', '2025-07-04 09:53:58');
INSERT INTO `system_log` VALUES (34, 'INFO', 'USER', '1234', NULL, '用户登录', '登录成功，用户名: 1234', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-04 10:55:52', '2025-07-04 10:55:51');
INSERT INTO `system_log` VALUES (35, 'INFO', 'USER', '1234', NULL, '用户登录', '登录成功，用户名: 1234', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-04 14:11:44', '2025-07-04 14:11:43');
INSERT INTO `system_log` VALUES (36, 'INFO', 'USER', '1234', NULL, '用户登录', '登录成功，用户名: 1234', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-04 15:38:25', '2025-07-04 15:38:25');
INSERT INTO `system_log` VALUES (37, 'INFO', 'USER', '1234', NULL, '用户登录', '登录成功，用户名: 1234', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-04 16:11:12', '2025-07-04 16:11:11');
INSERT INTO `system_log` VALUES (38, 'INFO', 'USER', '1234', NULL, '用户登录', '登录成功，用户名: 1234', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-04 17:03:37', '2025-07-04 17:03:37');
INSERT INTO `system_log` VALUES (39, 'INFO', 'USER', '1234', NULL, '用户登录', '登录成功，用户名: 1234', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-04 17:19:33', '2025-07-04 17:19:33');
INSERT INTO `system_log` VALUES (40, 'INFO', 'USER', '1234', NULL, '用户登录', '登录成功，用户名: 1234', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-04 18:21:03', '2025-07-04 18:21:02');
INSERT INTO `system_log` VALUES (41, 'INFO', 'USER', '1234', NULL, '用户登录', '登录成功，用户名: 1234', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-04 19:36:08', '2025-07-04 19:36:08');
INSERT INTO `system_log` VALUES (42, 'INFO', 'USER', '1234', NULL, '用户登录', '登录成功，用户名: 1234', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-04 21:19:42', '2025-07-04 21:19:41');
INSERT INTO `system_log` VALUES (43, 'INFO', '系统日志', '1234', NULL, '导出日志', '导出日志数据，条件: 级别=, 模块=, 用户=, 导出记录数: 42', NULL, NULL, NULL, NULL, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, NULL, '2025-07-04 21:19:57', '2025-07-04 21:19:56');
INSERT INTO `system_log` VALUES (44, 'INFO', 'SYSTEM', '1234', NULL, 'exportLogs', 'exportLogs操作完成', '/api/system-log/export', 'GET', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'EXPORT', '2025-07-04 21:19:57', '2025-07-04 21:19:56');
INSERT INTO `system_log` VALUES (45, 'INFO', 'USER', '1234', NULL, '用户登录', '登录成功，用户名: 1234', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-05 08:14:56', '2025-07-05 08:14:56');
INSERT INTO `system_log` VALUES (46, 'INFO', 'USER', '1234', NULL, '用户登录', '登录成功，用户名: 1234', '/api/auth/login', 'POST', NULL, 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'OTHER', '2025-07-05 08:53:21', '2025-07-05 08:53:20');
INSERT INTO `system_log` VALUES (47, 'INFO', 'USER', '1234', NULL, 'updateUserInfo', '操作成功', '/api/user/info', 'PUT', '{\"avatar\":\"/api/files/avatar/6f510432-12f9-49b6-b67c-6cb98f35021e.png\",\"createBy\":\"system\",\"createTime\":\"2025-06-27 11:14:01\",\"deleted\":0,\"email\":\"3296416743@qq.com\",\"gender\":\"0\",\"loginIp\":\"0:0:0:0:0:0:0:1\",\"loginTime\":\"2025-07-05 08:53:21\",\"phone\":\"15983660989\",\"realName\":\"dzx\",\"status\":1,\"updateBy\":\"1234\",\"updateTime\":\"2025-07-01 20:36:43\",\"userId\":1,\"userType\":1,\"username\":\"1234\"}', 200, NULL, '0:0:0:0:0:0:0:1', NULL, '', NULL, NULL, 'UPDATE', '2025-07-05 09:05:56', '2025-07-05 09:05:55');

-- ----------------------------
-- View structure for system_log_stats
-- ----------------------------
DROP VIEW IF EXISTS `system_log_stats`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `system_log_stats` AS select cast(`system_log`.`create_time` as date) AS `log_date`,`system_log`.`level` AS `level`,`system_log`.`module` AS `module`,count(0) AS `log_count` from `system_log` where (`system_log`.`create_time` >= (now() - interval 30 day)) group by cast(`system_log`.`create_time` as date),`system_log`.`level`,`system_log`.`module` order by `log_date` desc,`log_count` desc;

-- ----------------------------
-- View structure for v_elderly_latest_location
-- ----------------------------
DROP VIEW IF EXISTS `v_elderly_latest_location`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_elderly_latest_location` AS select `gl1`.`elderly_id` AS `elderly_id`,`gl1`.`macid` AS `macid`,`gl1`.`lat` AS `lat`,`gl1`.`lon` AS `lon`,`gl1`.`speed` AS `speed`,`gl1`.`dir` AS `dir`,`gl1`.`gps_time` AS `gps_time`,`gl1`.`create_time` AS `create_time` from (`gps_location` `gl1` join (select `gps_location`.`elderly_id` AS `elderly_id`,max(`gps_location`.`gps_time`) AS `max_gps_time` from `gps_location` where (`gps_location`.`elderly_id` is not null) group by `gps_location`.`elderly_id`) `gl2` on(((`gl1`.`elderly_id` = `gl2`.`elderly_id`) and (`gl1`.`gps_time` = `gl2`.`max_gps_time`))));

-- ----------------------------
-- View structure for v_fence_event_statistics
-- ----------------------------
DROP VIEW IF EXISTS `v_fence_event_statistics`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_fence_event_statistics` AS select `geo_fence_event`.`elderly_id` AS `elderly_id`,`geo_fence_event`.`fence_id` AS `fence_id`,`geo_fence_event`.`fence_name` AS `fence_name`,`geo_fence_event`.`event_type` AS `event_type`,cast(`geo_fence_event`.`event_time` as date) AS `event_date`,count(0) AS `event_count` from `geo_fence_event` group by `geo_fence_event`.`elderly_id`,`geo_fence_event`.`fence_id`,`geo_fence_event`.`fence_name`,`geo_fence_event`.`event_type`,cast(`geo_fence_event`.`event_time` as date) order by `event_date` desc,`geo_fence_event`.`elderly_id`,`geo_fence_event`.`fence_id`;

-- ----------------------------
-- Procedure structure for CleanOldGpsData
-- ----------------------------
DROP PROCEDURE IF EXISTS `CleanOldGpsData`;
delimiter ;;
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
    
    -- 记录清理日志
    SELECT CONCAT('清理了 ', ROW_COUNT(), ' 条GPS定位记录') AS result;
    
    COMMIT;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for CleanOldLogs
-- ----------------------------
DROP PROCEDURE IF EXISTS `CleanOldLogs`;
delimiter ;;
CREATE PROCEDURE `CleanOldLogs`(IN days_to_keep INT)
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
END
;;
delimiter ;

-- ----------------------------
-- Function structure for GetLogCountByLevel
-- ----------------------------
DROP FUNCTION IF EXISTS `GetLogCountByLevel`;
delimiter ;;
CREATE FUNCTION `GetLogCountByLevel`(log_level VARCHAR(20), start_date DATE, end_date DATE)
 RETURNS int
  READS SQL DATA 
  DETERMINISTIC
BEGIN
    DECLARE log_count INT DEFAULT 0;
    
    SELECT COUNT(*) INTO log_count
    FROM system_log
    WHERE level = log_level
    AND DATE(create_time) BETWEEN start_date AND end_date;
    
    RETURN log_count;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
