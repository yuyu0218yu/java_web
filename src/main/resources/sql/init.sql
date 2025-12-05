-- 设置MySQL连接字符集
SET NAMES utf8mb4;
SET character_set_client = utf8mb4;
SET character_set_connection = utf8mb4;

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS java_web DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE java_web;

-- 删除已存在的表，确保重新创建
DROP TABLE IF EXISTS sys_user_role;
DROP TABLE IF EXISTS sys_role_permission;
DROP TABLE IF EXISTS sys_operation_log;
DROP TABLE IF EXISTS sys_login_log;
DROP TABLE IF EXISTS sys_file_info;
DROP TABLE IF EXISTS sys_user;
DROP TABLE IF EXISTS sys_role;
DROP TABLE IF EXISTS sys_permission;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    salt VARCHAR(32) COMMENT '密码盐值',
    real_name VARCHAR(50) COMMENT '真实姓名',
    nickname VARCHAR(50) COMMENT '昵称',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像URL',
    gender TINYINT DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    birthday DATE COMMENT '生日',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    last_login_time DATETIME COMMENT '最后登录时间',
    last_login_ip VARCHAR(45) COMMENT '最后登录IP',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    remark TEXT COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
    role_name VARCHAR(100) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    description TEXT COMMENT '角色描述',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    remark TEXT COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 权限表
CREATE TABLE IF NOT EXISTS sys_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '权限ID',
    permission_name VARCHAR(50) NOT NULL COMMENT '权限名称',
    permission_code VARCHAR(100) NOT NULL UNIQUE COMMENT '权限编码',
    resource_type TINYINT DEFAULT 1 COMMENT '资源类型：1-菜单，2-按钮，3-接口',
    parent_id BIGINT DEFAULT 0 COMMENT '父级ID',
    path VARCHAR(255) COMMENT '路径',
    component VARCHAR(255) COMMENT '组件',
    icon VARCHAR(100) COMMENT '图标',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    remark TEXT COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    UNIQUE KEY uk_user_role (user_id, role_id, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS sys_role_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    UNIQUE KEY uk_role_permission (role_id, permission_id, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- 插入初始数据

-- 插入系统角色
INSERT INTO sys_role (role_name, role_code, description, sort_order) VALUES
('Super Admin', 'SUPER_ADMIN', 'System super administrator with all permissions', 1),
('Admin', 'ADMIN', 'System administrator with most permissions', 2),
('User', 'USER', 'Regular user with basic permissions', 3)
ON DUPLICATE KEY UPDATE role_name = VALUES(role_name);

-- 插入系统权限
INSERT INTO sys_permission (permission_name, permission_code, resource_type, parent_id, path, component, sort_order) VALUES
('系统管理', 'system', 1, 0, '/system', NULL, 1),
('用户管理', 'user', 1, 1, '/system/user', 'system/user/index', 2),
('角色管理', 'role', 1, 1, '/system/role', 'system/role/index', 3),
('权限管理', 'permission', 1, 1, '/system/permission', 'system/permission/index', 4),
('用户查看', 'user:view', 2, 2, NULL, NULL, 5),
('用户创建', 'user:create', 2, 2, NULL, NULL, 6),
('用户编辑', 'user:edit', 2, 2, NULL, NULL, 7),
('用户删除', 'user:delete', 2, 2, NULL, NULL, 8),
('角色查看', 'role:view', 2, 3, NULL, NULL, 9),
('角色创建', 'role:create', 2, 3, NULL, NULL, 10),
('角色编辑', 'role:edit', 2, 3, NULL, NULL, 11),
('角色删除', 'role:delete', 2, 3, NULL, NULL, 12),
('权限查看', 'permission:view', 2, 4, NULL, NULL, 13),
('权限创建', 'permission:create', 2, 4, NULL, NULL, 14),
('权限编辑', 'permission:edit', 2, 4, NULL, NULL, 15),
('权限删除', 'permission:delete', 2, 4, NULL, NULL, 16),
('全部权限', '*', 3, 0, NULL, NULL, 99)
ON DUPLICATE KEY UPDATE permission_name = VALUES(permission_name);

-- 插入默认管理员用户（密码：123456）
-- 使用BCrypt加密的密码：123456
INSERT INTO sys_user (username, password, real_name, nickname, email, phone, status) VALUES
('admin', '$2a$10$7JB720yubVSOfvVWbfZuCOYzBJTQrUyE3VEc.Y0mBJoEE0V7DX4Ca', '系统管理员', '超级管理员', 'admin@example.com', '13800138000', 1)
ON DUPLICATE KEY UPDATE password = VALUES(password);

-- 获取插入的用户ID和角色ID
SET @admin_user_id = (SELECT id FROM sys_user WHERE username = 'admin' LIMIT 1);
SET @super_admin_role_id = (SELECT id FROM sys_role WHERE role_code = 'SUPER_ADMIN' LIMIT 1);
SET @admin_role_id = (SELECT id FROM sys_role WHERE role_code = 'ADMIN' LIMIT 1);
SET @user_role_id = (SELECT id FROM sys_role WHERE role_code = 'USER' LIMIT 1);

-- 为管理员分配超级管理员角色
INSERT INTO sys_user_role (user_id, role_id) VALUES
(@admin_user_id, @super_admin_role_id)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- 为超级管理员角色分配所有权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT @super_admin_role_id, id FROM sys_permission
WHERE id NOT IN (
    SELECT permission_id FROM sys_role_permission WHERE role_id = @super_admin_role_id AND deleted = 0
)
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id);

-- 为管理员角色分配部分权限
INSERT INTO sys_role_permission (role_id, permission_id) VALUES
(@admin_role_id, (SELECT id FROM sys_permission WHERE permission_code = 'system' LIMIT 1)),
(@admin_role_id, (SELECT id FROM sys_permission WHERE permission_code = 'user' LIMIT 1)),
(@admin_role_id, (SELECT id FROM sys_permission WHERE permission_code = 'role' LIMIT 1)),
(@admin_role_id, (SELECT id FROM sys_permission WHERE permission_code = 'user:view' LIMIT 1)),
(@admin_role_id, (SELECT id FROM sys_permission WHERE permission_code = 'user:create' LIMIT 1)),
(@admin_role_id, (SELECT id FROM sys_permission WHERE permission_code = 'user:edit' LIMIT 1)),
(@admin_role_id, (SELECT id FROM sys_permission WHERE permission_code = 'role:view' LIMIT 1)),
(@admin_role_id, (SELECT id FROM sys_permission WHERE permission_code = 'role:create' LIMIT 1)),
(@admin_role_id, (SELECT id FROM sys_permission WHERE permission_code = 'role:edit' LIMIT 1))
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id);

-- 为普通用户角色分配基本权限
INSERT INTO sys_role_permission (role_id, permission_id) VALUES
(@user_role_id, (SELECT id FROM sys_permission WHERE permission_code = 'user:view' LIMIT 1))
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id);

-- ================================================================================
-- 操作日志表
-- ================================================================================
CREATE TABLE IF NOT EXISTS sys_operation_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT COMMENT '操作用户ID',
    username VARCHAR(50) COMMENT '操作用户名',
    operation VARCHAR(100) COMMENT '操作类型',
    method VARCHAR(255) COMMENT '请求方法',
    params TEXT COMMENT '请求参数',
    time BIGINT COMMENT '执行时长(毫秒)',
    ip VARCHAR(45) COMMENT '操作IP',
    user_agent VARCHAR(500) COMMENT '用户代理',
    status INT DEFAULT 1 COMMENT '操作状态：0-失败，1-成功',
    error_msg TEXT COMMENT '错误信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    remark TEXT COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- ================================================================================
-- 登录日志表
-- ================================================================================
CREATE TABLE IF NOT EXISTS sys_login_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    username VARCHAR(50) COMMENT '登录用户名',
    ip VARCHAR(45) COMMENT '登录IP',
    location VARCHAR(100) COMMENT '登录地点',
    browser VARCHAR(100) COMMENT '浏览器',
    os VARCHAR(100) COMMENT '操作系统',
    user_agent VARCHAR(500) COMMENT '用户代理',
    status INT DEFAULT 1 COMMENT '登录状态：0-失败，1-成功',
    error_msg TEXT COMMENT '错误信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    remark TEXT COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录日志表';

-- ================================================================================
-- 文件信息表
-- ================================================================================
CREATE TABLE IF NOT EXISTS sys_file_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    file_name VARCHAR(255) NOT NULL COMMENT '文件名',
    original_name VARCHAR(255) COMMENT '原始文件名',
    file_path VARCHAR(500) NOT NULL COMMENT '文件路径',
    file_size BIGINT COMMENT '文件大小(字节)',
    file_type VARCHAR(50) COMMENT '文件类型',
    mime_type VARCHAR(100) COMMENT 'MIME类型',
    file_hash VARCHAR(64) COMMENT '文件哈希值(SHA256)',
    upload_user_id BIGINT COMMENT '上传用户ID',
    upload_username VARCHAR(50) COMMENT '上传用户名',
    download_count INT DEFAULT 0 COMMENT '下载次数',
    status INT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    remark TEXT COMMENT '备注',
    KEY idx_file_hash (file_hash),
    KEY idx_upload_user_id (upload_user_id),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件信息表';