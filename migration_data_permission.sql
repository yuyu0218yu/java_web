-- ============================================================================
-- 数据权限系统升级脚本
-- 版本: 1.0.0
-- 日期: 2025-12-08
-- 说明: 为现有系统添加数据权限功能
-- ============================================================================

-- 1. 为 sys_role 表添加 data_scope 字段
ALTER TABLE sys_role ADD COLUMN IF NOT EXISTS data_scope TINYINT DEFAULT 1 COMMENT '数据范围：1-全部数据 2-本部门及下级 3-本部门 4-仅本人';

-- 2. 更新现有角色的数据范围
-- 超级管理员：全部数据
UPDATE sys_role SET data_scope = 1 WHERE role_code = 'SUPER_ADMIN' AND data_scope IS NULL;

-- 管理员：本部门及下级
UPDATE sys_role SET data_scope = 2 WHERE role_code = 'ADMIN' AND data_scope IS NULL;

-- 普通用户：仅本人
UPDATE sys_role SET data_scope = 4 WHERE role_code = 'USER' AND data_scope IS NULL;

-- 其他未设置的角色：默认为全部数据
UPDATE sys_role SET data_scope = 1 WHERE data_scope IS NULL;

-- 3. 验证数据
SELECT id, role_name, role_code, data_scope,
       CASE data_scope
           WHEN 1 THEN '全部数据'
           WHEN 2 THEN '本部门及下级'
           WHEN 3 THEN '本部门'
           WHEN 4 THEN '仅本人'
           ELSE '未知'
       END AS data_scope_desc
FROM sys_role
WHERE deleted = 0
ORDER BY sort_order;

-- 4. 验证用户-部门关联
SELECT 
    u.id,
    u.username,
    u.dept_id,
    d.dept_name,
    GROUP_CONCAT(r.role_name) AS roles,
    MIN(r.data_scope) AS min_data_scope
FROM sys_user u
LEFT JOIN sys_dept d ON u.dept_id = d.id
LEFT JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0
LEFT JOIN sys_role r ON ur.role_id = r.id AND r.deleted = 0
WHERE u.deleted = 0
GROUP BY u.id, u.username, u.dept_id, d.dept_name
ORDER BY u.id;

-- ============================================================================
-- 升级完成！
-- 
-- 下一步：
-- 1. 重启应用服务器
-- 2. 测试数据权限是否生效
-- 3. 在前端为用户分配部门
-- 4. 为角色配置合适的数据范围
-- ============================================================================
