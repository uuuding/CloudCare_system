-- 插入测试GPS轨迹数据
-- 为老人ID=1插入一些测试轨迹点，模拟在深圳市区的移动轨迹

INSERT INTO `gps_location` (
    `macid`, `elderly_id`, `gps_time`, `lat`, `lon`, `map_lat`, `map_lon`, 
    `speed`, `dir`, `stats`, `value`
) VALUES 
-- 起点：深圳市民中心附近
('TEST123456789', 1, UNIX_TIMESTAMP(DATE_SUB(NOW(), INTERVAL 2 HOUR)) * 1000, 
 22.54329, 114.05793, 22.54329, 114.05793, 0.0, 0.0, 'normal', 'test'),

-- 移动轨迹点1：向东移动
('TEST123456789', 1, UNIX_TIMESTAMP(DATE_SUB(NOW(), INTERVAL 110 MINUTE)) * 1000, 
 22.54350, 114.05850, 22.54350, 114.05850, 5.2, 90.0, 'normal', 'test'),

-- 移动轨迹点2：继续向东
('TEST123456789', 1, UNIX_TIMESTAMP(DATE_SUB(NOW(), INTERVAL 100 MINUTE)) * 1000, 
 22.54380, 114.05920, 22.54380, 114.05920, 8.1, 85.0, 'normal', 'test'),

-- 移动轨迹点3：向北转向
('TEST123456789', 1, UNIX_TIMESTAMP(DATE_SUB(NOW(), INTERVAL 90 MINUTE)) * 1000, 
 22.54420, 114.05950, 22.54420, 114.05950, 6.5, 45.0, 'normal', 'test'),

-- 移动轨迹点4：向北移动
('TEST123456789', 1, UNIX_TIMESTAMP(DATE_SUB(NOW(), INTERVAL 80 MINUTE)) * 1000, 
 22.54480, 114.05980, 22.54480, 114.05980, 7.3, 15.0, 'normal', 'test'),

-- 移动轨迹点5：停留点（速度为0）
('TEST123456789', 1, UNIX_TIMESTAMP(DATE_SUB(NOW(), INTERVAL 70 MINUTE)) * 1000, 
 22.54500, 114.06000, 22.54500, 114.06000, 0.0, 15.0, 'stop', 'test'),

-- 移动轨迹点6：继续向西北移动
('TEST123456789', 1, UNIX_TIMESTAMP(DATE_SUB(NOW(), INTERVAL 60 MINUTE)) * 1000, 
 22.54550, 114.05950, 22.54550, 114.05950, 9.2, 315.0, 'normal', 'test'),

-- 移动轨迹点7：向西移动
('TEST123456789', 1, UNIX_TIMESTAMP(DATE_SUB(NOW(), INTERVAL 50 MINUTE)) * 1000, 
 22.54580, 114.05880, 22.54580, 114.05880, 12.5, 270.0, 'normal', 'test'),

-- 移动轨迹点8：减速
('TEST123456789', 1, UNIX_TIMESTAMP(DATE_SUB(NOW(), INTERVAL 40 MINUTE)) * 1000, 
 22.54600, 114.05800, 22.54600, 114.05800, 4.8, 260.0, 'normal', 'test'),

-- 移动轨迹点9：向南转向
('TEST123456789', 1, UNIX_TIMESTAMP(DATE_SUB(NOW(), INTERVAL 30 MINUTE)) * 1000, 
 22.54580, 114.05750, 22.54580, 114.05750, 6.1, 225.0, 'normal', 'test'),

-- 移动轨迹点10：向南移动
('TEST123456789', 1, UNIX_TIMESTAMP(DATE_SUB(NOW(), INTERVAL 20 MINUTE)) * 1000, 
 22.54520, 114.05720, 22.54520, 114.05720, 8.7, 195.0, 'normal', 'test'),

-- 移动轨迹点11：向东南移动
('TEST123456789', 1, UNIX_TIMESTAMP(DATE_SUB(NOW(), INTERVAL 10 MINUTE)) * 1000, 
 22.54480, 114.05780, 22.54480, 114.05780, 5.9, 135.0, 'normal', 'test'),

-- 终点：回到起点附近
('TEST123456789', 1, UNIX_TIMESTAMP(NOW()) * 1000, 
 22.54350, 114.05800, 22.54350, 114.05800, 0.0, 90.0, 'arrived', 'test');

-- 验证插入的数据
SELECT 
    id, macid, elderly_id, 
    FROM_UNIXTIME(gps_time/1000) as gps_datetime,
    lat, lon, speed, dir, stats
FROM gps_location 
WHERE elderly_id = 1 
ORDER BY gps_time DESC;

-- 统计信息
SELECT 
    COUNT(*) as total_points,
    MIN(FROM_UNIXTIME(gps_time/1000)) as earliest_time,
    MAX(FROM_UNIXTIME(gps_time/1000)) as latest_time,
    AVG(speed) as avg_speed
FROM gps_location 
WHERE elderly_id = 1;