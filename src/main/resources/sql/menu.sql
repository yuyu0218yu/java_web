-- 菜单表
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `is_frame` tinyint DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` tinyint DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` tinyint DEFAULT 1 COMMENT '菜单状态（0隐藏 1显示）',
  `status` tinyint DEFAULT 1 COMMENT '菜单状态（0停用 1正常）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '逻辑删除',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表';

-- 角色和菜单关联表
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色和菜单关联表';

-- 初始化菜单数据
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`) VALUES
-- 一级菜单
(1, '仪表盘', 0, 1, '/dashboard', 'Dashboard', 'C', 1, 1, NULL, 'House'),
(2, '个人中心', 0, 2, '/profile', 'Profile', 'C', 1, 1, NULL, 'User'),
(3, '系统管理', 0, 3, '/system', NULL, 'M', 1, 1, NULL, 'Setting'),

-- 系统管理子菜单
(100, '用户管理', 3, 1, '/users', 'Users', 'C', 1, 1, 'user:view', 'UserFilled'),
(101, '角色管理', 3, 2, '/roles', 'Roles', 'C', 1, 1, 'role:view', 'Avatar'),
(102, '菜单管理', 3, 3, '/menus', 'Menus', 'C', 1, 1, 'menu:view', 'Menu'),
(103, '权限管理', 3, 4, '/permissions', 'Permissions', 'C', 1, 1, 'permission:view', 'Key'),

-- 用户管理按钮
(1001, '用户查询', 100, 1, '', NULL, 'F', 1, 1, 'user:view', '#'),
(1002, '用户新增', 100, 2, '', NULL, 'F', 1, 1, 'user:create', '#'),
(1003, '用户修改', 100, 3, '', NULL, 'F', 1, 1, 'user:update', '#'),
(1004, '用户删除', 100, 4, '', NULL, 'F', 1, 1, 'user:delete', '#'),

-- 角色管理按钮
(1011, '角色查询', 101, 1, '', NULL, 'F', 1, 1, 'role:view', '#'),
(1012, '角色新增', 101, 2, '', NULL, 'F', 1, 1, 'role:create', '#'),
(1013, '角色修改', 101, 3, '', NULL, 'F', 1, 1, 'role:update', '#'),
(1014, '角色删除', 101, 4, '', NULL, 'F', 1, 1, 'role:delete', '#'),

-- 菜单管理按钮
(1021, '菜单查询', 102, 1, '', NULL, 'F', 1, 1, 'menu:view', '#'),
(1022, '菜单新增', 102, 2, '', NULL, 'F', 1, 1, 'menu:create', '#'),
(1023, '菜单修改', 102, 3, '', NULL, 'F', 1, 1, 'menu:update', '#'),
(1024, '菜单删除', 102, 4, '', NULL, 'F', 1, 1, 'menu:delete', '#');

-- 只给超级管理员分配所有菜单（其他角色由超级管理员在系统中分配）
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) 
SELECT 1, id FROM `sys_menu` WHERE deleted = 0;
