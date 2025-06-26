-- 修复用户密码脚本
-- 将明文密码更新为BCrypt加密的密码

USE cloudcare;

-- 更新用户ID为1的密码为BCrypt加密的'123456'
UPDATE sys_user 
SET password = '$2a$10$WI6SNQoDHCPuVBOE3fIBVewVNc.cNu/58yYHMdC6E9Wy07CsS.mqW' 
WHERE user_id = 1;

-- 验证更新结果
SELECT user_id, username, password, status FROM sys_user WHERE user_id = 1;