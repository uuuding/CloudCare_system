-- 为geo_fence_event表添加已读状态字段的迁移脚本
-- 执行日期：2024年

-- 添加is_read字段
ALTER TABLE `geo_fence_event` 
ADD COLUMN `is_read` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已读：1-已读，0-未读' AFTER `alert_content`;

-- 添加read_time字段
ALTER TABLE `geo_fence_event` 
ADD COLUMN `read_time` DATETIME NULL COMMENT '已读时间' AFTER `is_read`;

-- 添加索引以优化查询性能
ALTER TABLE `geo_fence_event` 
ADD INDEX `idx_is_read` (`is_read`);

-- 验证字段是否添加成功
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT, COLUMN_COMMENT 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = DATABASE() 
AND TABLE_NAME = 'geo_fence_event' 
AND COLUMN_NAME IN ('is_read', 'read_time')
ORDER BY ORDINAL_POSITION;

COMMIT;