-- 用户权限系统数据库脚本
-- 创建时间: 2025-12-05
-- 数据库: java_web

-- ================================
-- 1. 用户表 (sys_user)
-- ================================
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    salt VARCHAR(32) COMMENT '密码盐值',
    real_name VARCHAR(50) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像URL',
    gender TINYINT DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    birthday DATE COMMENT '生日',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    last_login_time DATETIME COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    remark TEXT COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 用户表索引
CREATE INDEX idx_username ON sys_user(username);
CREATE INDEX idx_email ON sys_user(email);
CREATE INDEX idx_phone ON sys_user(phone);
CREATE INDEX idx_status ON sys_user(status);
CREATE INDEX idx_create_time ON sys_user(create_time);

-- ================================
-- 2. 角色表 (sys_role)
-- ================================
CREATE TABLE sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    description VARCHAR(200) COMMENT '角色描述',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 角色表索引
CREATE INDEX idx_role_code ON sys_role(role_code);
CREATE INDEX idx_status ON sys_role(status);
CREATE INDEX idx_sort_order ON sys_role(sort_order);

-- ================================
-- 3. 权限表 (sys_permission)
-- ================================
CREATE TABLE sys_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID',
    permission_name VARCHAR(100) NOT NULL COMMENT '权限名称',
    permission_code VARCHAR(100) NOT NULL UNIQUE COMMENT '权限编码',
    permission_type TINYINT NOT NULL COMMENT '权限类型：1-菜单，2-按钮，3-接口',
    parent_id BIGINT DEFAULT 0 COMMENT '父级权限ID',
    path VARCHAR(200) COMMENT '路径',
    component VARCHAR(200) COMMENT '组件路径',
    icon VARCHAR(100) COMMENT '图标',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    remark TEXT COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- 权限表索引
CREATE INDEX idx_permission_code ON sys_permission(permission_code);
CREATE INDEX idx_permission_type ON sys_permission(permission_type);
CREATE INDEX idx_parent_id ON sys_permission(parent_id);
CREATE INDEX idx_status ON sys_permission(status);
CREATE INDEX idx_sort_order ON sys_permission(sort_order);

-- ================================
-- 4. 用户角色关联表 (sys_user_role)
-- ================================
CREATE TABLE sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_role (user_id, role_id),
    KEY idx_user_id (user_id),
    KEY idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- ================================
-- 5. 角色权限关联表 (sys_role_permission)
-- ================================
CREATE TABLE sys_role_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_role_permission (role_id, permission_id),
    KEY idx_role_id (role_id),
    KEY idx_permission_id (permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- ================================
-- 6. 初始化数据
-- ================================

-- 初始化角色数据
INSERT INTO sys_role (role_name, role_code, description, sort_order) VALUES
('超级管理员', 'SUPER_ADMIN', '系统超级管理员，拥有所有权限', 1),
('管理员', 'ADMIN', '系统管理员，拥有大部分管理权限', 2),
('普通用户', 'USER', '普通用户，拥有基本权限', 3);

-- 初始化权限数据
INSERT INTO sys_permission (permission_name, permission_code, permission_type, parent_id, path, component, icon, sort_order) VALUES
-- 一级菜单
('系统管理', 'system', 1, 0, '/system', NULL, 'system', 1),
('用户管理', 'user', 1, 0, '/user', NULL, 'user', 2),
('权限管理', 'permission', 1, 0, '/permission', NULL, 'permission', 3),

-- 二级菜单 - 系统管理下
('角色管理', 'system:role', 1, 1, '/system/role', 'system/role/index', 'role', 1),
('菜单管理', 'system:menu', 1, 1, '/system/menu', 'system/menu/index', 'menu', 2),

-- 二级菜单 - 用户管理下
('用户列表', 'user:list', 1, 2, '/user/list', 'user/list/index', 'list', 1),
('用户新增', 'user:add', 2, 2, NULL, NULL, NULL, 2),
('用户编辑', 'user:edit', 2, 2, NULL, NULL, NULL, 3),
('用户删除', 'user:delete', 2, 2, NULL, NULL, NULL, 4),

-- 二级菜单 - 权限管理下
('权限列表', 'permission:list', 1, 3, '/permission/list', 'permission/list/index', 'list', 1),
('权限新增', 'permission:add', 2, 3, NULL, NULL, NULL, 2),
('权限编辑', 'permission:edit', 2, 3, NULL, NULL, NULL, 3),
('权限删除', 'permission:delete', 2, 3, NULL, NULL, NULL, 4),

-- 接口权限
('用户查询接口', 'user:query:api', 3, 0, NULL, NULL, NULL, 1),
('用户操作接口', 'user:operate:api', 3, 0, NULL, NULL, NULL, 2),
('权限查询接口', 'permission:query:api', 3, 0, NULL, NULL, NULL, 3),
('权限操作接口', 'permission:operate:api', 3, 0, NULL, NULL, NULL, 4);

-- 为超级管理员角色分配所有权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, id FROM sys_permission WHERE deleted = 0;

-- 为管理员角色分配大部分权限（除了删除权限）
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 2, id FROM sys_permission
WHERE deleted = 0
AND permission_code NOT IN ('user:delete', 'permission:delete');

-- 为普通用户角色分配基本权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 3, id FROM sys_permission
WHERE deleted = 0
AND permission_code IN ('user:list', 'user:query:api');

-- 初始化用户数据（密码为 123456，使用项目中的密码工具加密）
-- 注意：这里的密码需要使用 PasswordUtil.encryptPassword("123456") 生成
INSERT INTO sys_user (username, password, salt, real_name, email, phone, gender, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'admin_salt', '超级管理员', 'admin@example.com', '13800138000', 1, 1),
('manager', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'manager_salt', '管理员', 'manager@example.com', '13800138001', 1, 1),
('user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'user1_salt', '普通用户1', 'user1@example.com', '13800138002', 1, 1);

-- 为用户分配角色
INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1), -- admin -> 超级管理员
(2, 2), -- manager -> 管理员
(3, 3); -- user1 -> 普通用户

-- ================================
-- 7. 常用查询语句
-- ================================

-- 查询用户及其角色
SELECT
    u.id,
    u.username,
    u.real_name,
    u.email,
    u.status,
    r.role_name,
    r.role_code
FROM sys_user u
LEFT JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0
LEFT JOIN sys_role r ON ur.role_id = r.id AND r.deleted = 0
WHERE u.deleted = 0;

-- 查询用户的所有权限
SELECT DISTINCT
    u.id,
    u.username,
    p.permission_name,
    p.permission_code,
    p.permission_type
FROM sys_user u
JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0
JOIN sys_role_permission rp ON ur.role_id = rp.role_id AND rp.deleted = 0
JOIN sys_permission p ON rp.permission_id = p.id AND p.deleted = 0
WHERE u.deleted = 0 AND u.username = 'admin';

-- 查询角色的权限树
SELECT
    p1.id,
    p1.permission_name,
    p1.permission_code,
    p1.permission_type,
    p1.parent_id,
    p1.sort_order
FROM sys_permission p1
WHERE p1.deleted = 0
ORDER BY p1.parent_id, p1.sort_order;