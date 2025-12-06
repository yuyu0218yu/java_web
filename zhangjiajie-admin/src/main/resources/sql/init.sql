/*
 Navicat Premium Dump SQL

 Source Server         : MYSQL
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : java_web

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 05/12/2025 23:11:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_file_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_info`;
CREATE TABLE `sys_file_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件名',
  `original_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '原始文件名',
  `file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件路径',
  `file_size` bigint NULL DEFAULT NULL COMMENT '文件大小(字节)',
  `file_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件类型',
  `mime_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'MIME类型',
  `file_hash` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件哈希值(SHA256)',
  `upload_user_id` bigint NULL DEFAULT NULL COMMENT '上传用户ID',
  `upload_username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '上传用户名',
  `download_count` int NULL DEFAULT 0 COMMENT '下载次数',
  `status` int NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_file_hash`(`file_hash` ASC) USING BTREE,
  INDEX `idx_upload_user_id`(`upload_user_id` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文件信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_file_info
-- ----------------------------

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录用户名',
  `ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录IP',
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录地点',
  `browser` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '浏览器',
  `os` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作系统',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户代理',
  `status` int NULL DEFAULT 1 COMMENT '登录状态：0-失败，1-成功',
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '错误信息',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '登录日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------
INSERT INTO `sys_login_log` VALUES (1, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 0, 'Bad credentials', '2025-12-05 18:01:22', '2025-12-05 18:01:22', 0, NULL);
INSERT INTO `sys_login_log` VALUES (2, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 0, 'Bad credentials', '2025-12-05 18:05:07', '2025-12-05 18:05:07', 0, NULL);
INSERT INTO `sys_login_log` VALUES (3, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Unknown Browser', 'Unknown OS', 'curl/8.16.0', 0, 'Bad credentials', '2025-12-05 18:11:15', '2025-12-05 18:11:15', 0, NULL);
INSERT INTO `sys_login_log` VALUES (4, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 0, '用户名或密码错误', '2025-12-05 18:18:03', '2025-12-05 18:18:03', 0, NULL);
INSERT INTO `sys_login_log` VALUES (5, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 0, '用户名或密码错误', '2025-12-05 19:28:07', '2025-12-05 19:28:07', 0, NULL);
INSERT INTO `sys_login_log` VALUES (6, 'yushuang', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 19:37:21', '2025-12-05 19:37:21', 0, NULL);
INSERT INTO `sys_login_log` VALUES (7, 'yushuang1', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 19:38:38', '2025-12-05 19:38:38', 0, NULL);
INSERT INTO `sys_login_log` VALUES (8, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 19:39:23', '2025-12-05 19:39:23', 0, NULL);
INSERT INTO `sys_login_log` VALUES (9, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 19:50:01', '2025-12-05 19:50:01', 0, NULL);
INSERT INTO `sys_login_log` VALUES (10, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 19:50:51', '2025-12-05 19:50:51', 0, NULL);
INSERT INTO `sys_login_log` VALUES (11, 'yushuang', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 20:14:05', '2025-12-05 20:14:05', 0, NULL);
INSERT INTO `sys_login_log` VALUES (12, 'yushuang', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 20:50:28', '2025-12-05 20:50:28', 0, NULL);

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '操作用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作用户名',
  `operation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作类型',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '请求参数',
  `time` bigint NULL DEFAULT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作IP',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户代理',
  `status` int NULL DEFAULT 1 COMMENT '操作状态：0-失败，1-成功',
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '错误信息',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------
INSERT INTO `sys_operation_log` VALUES (1, NULL, 'anonymousUser', '用户登录', 'com.yushuang.demo.controller.AuthController.login', NULL, 311, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 18:01:22', '2025-12-05 18:01:22', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (2, NULL, 'anonymousUser', '用户登录', 'com.yushuang.demo.controller.AuthController.login', NULL, 56, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 18:05:07', '2025-12-05 18:05:07', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (3, NULL, 'anonymousUser', '用户登录', 'com.yushuang.demo.controller.AuthController.login', NULL, 57, '0:0:0:0:0:0:0:1', 'curl/8.16.0', 1, NULL, '2025-12-05 18:11:15', '2025-12-05 18:11:15', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (4, NULL, 'anonymousUser', '用户登录', 'com.yushuang.demo.controller.AuthController.login', NULL, 250, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 18:18:03', '2025-12-05 18:18:03', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (5, NULL, 'anonymousUser', '用户登录', 'com.yushuang.demo.controller.AuthController.login', NULL, 246, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 19:28:07', '2025-12-05 19:28:07', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (6, NULL, 'anonymousUser', '用户登录', 'com.yushuang.demo.controller.AuthController.login', NULL, 102, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 19:37:21', '2025-12-05 19:37:21', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (7, NULL, 'anonymousUser', '用户登录', 'com.yushuang.demo.controller.AuthController.login', NULL, 58, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 19:38:38', '2025-12-05 19:38:38', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (8, NULL, 'anonymousUser', '用户登录', 'com.yushuang.demo.controller.AuthController.login', NULL, 58, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 19:39:23', '2025-12-05 19:39:23', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (9, NULL, 'anonymousUser', '用户登录', 'com.yushuang.demo.controller.AuthController.login', NULL, 60, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 19:50:01', '2025-12-05 19:50:01', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (10, NULL, 'anonymousUser', '用户登录', 'com.yushuang.demo.controller.AuthController.login', NULL, 58, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 19:50:51', '2025-12-05 19:50:51', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (11, NULL, 'anonymousUser', '用户登录', 'com.yushuang.demo.controller.AuthController.login', NULL, 260, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 20:14:05', '2025-12-05 20:14:05', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (12, NULL, 'anonymousUser', '用户登录', 'com.yushuang.demo.controller.AuthController.login', NULL, 129, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 20:50:28', '2025-12-05 20:50:28', 0, NULL);

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `permission_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限名称',
  `permission_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限编码',
  `resource_type` tinyint NULL DEFAULT 1 COMMENT '资源类型：1-菜单，2-按钮，3-接口',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父级ID',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '路径',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组件',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图标',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `permission_code`(`permission_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1, '系统管理', 'system', 1, 0, '/system', NULL, NULL, 1, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0, NULL);
INSERT INTO `sys_permission` VALUES (2, '用户管理', 'user', 1, 1, '/system/user', 'system/user/index', NULL, 2, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0, NULL);
INSERT INTO `sys_permission` VALUES (3, '角色管理', 'role', 1, 1, '/system/role', 'system/role/index', NULL, 3, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0, NULL);
INSERT INTO `sys_permission` VALUES (4, '权限管理', 'permission', 1, 1, '/system/permission', 'system/permission/index', NULL, 4, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0, NULL);
INSERT INTO `sys_permission` VALUES (5, '用户查看', 'user:view', 2, 2, NULL, NULL, NULL, 5, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0, NULL);
INSERT INTO `sys_permission` VALUES (6, '用户创建', 'user:create', 2, 2, NULL, NULL, NULL, 6, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0, NULL);
INSERT INTO `sys_permission` VALUES (7, '用户编辑', 'user:edit', 2, 2, NULL, NULL, NULL, 7, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0, NULL);
INSERT INTO `sys_permission` VALUES (8, '用户删除', 'user:delete', 2, 2, NULL, NULL, NULL, 8, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0, NULL);
INSERT INTO `sys_permission` VALUES (9, '角色查看', 'role:view', 2, 3, NULL, NULL, NULL, 9, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0, NULL);
INSERT INTO `sys_permission` VALUES (10, '角色创建', 'role:create', 2, 3, NULL, NULL, NULL, 10, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0, NULL);
INSERT INTO `sys_permission` VALUES (11, '角色编辑', 'role:edit', 2, 3, NULL, NULL, NULL, 11, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0, NULL);
INSERT INTO `sys_permission` VALUES (12, '角色删除', 'role:delete', 2, 3, NULL, NULL, NULL, 12, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0, NULL);
INSERT INTO `sys_permission` VALUES (13, '权限查看', 'permission:view', 2, 4, NULL, NULL, NULL, 13, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0, NULL);
INSERT INTO `sys_permission` VALUES (14, '权限创建', 'permission:create', 2, 4, NULL, NULL, NULL, 14, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0, NULL);
INSERT INTO `sys_permission` VALUES (15, '权限编辑', 'permission:edit', 2, 4, NULL, NULL, NULL, 15, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0, NULL);
INSERT INTO `sys_permission` VALUES (16, '权限删除', 'permission:delete', 2, 4, NULL, NULL, NULL, 16, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0, NULL);
INSERT INTO `sys_permission` VALUES (17, '全部权限', '*', 3, 0, NULL, NULL, NULL, 99, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0, NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色编码',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '角色描述',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_code`(`role_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'Super Admin', 'SUPER_ADMIN', 'System super administrator with all permissions', 1, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0, NULL);
INSERT INTO `sys_role` VALUES (2, 'Admin', 'ADMIN', 'System administrator with most permissions', 2, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0, NULL);
INSERT INTO `sys_role` VALUES (3, 'User', 'USER', 'Regular user with basic permissions', 3, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0, NULL);

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `permission_id` bigint NOT NULL COMMENT '权限ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_permission`(`role_id` ASC, `permission_id` ASC, `deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色权限关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (1, 1, 17, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (2, 1, 4, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (3, 1, 14, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (4, 1, 16, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (5, 1, 15, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (6, 1, 13, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (7, 1, 3, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (8, 1, 10, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (9, 1, 12, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (10, 1, 11, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (11, 1, 9, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (12, 1, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (13, 1, 2, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (14, 1, 6, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (15, 1, 8, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (16, 1, 7, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (17, 1, 5, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (32, 2, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (33, 2, 2, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (34, 2, 3, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (35, 2, 5, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (36, 2, 6, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (37, 2, 7, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (38, 2, 9, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (39, 2, 10, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (40, 2, 11, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_role_permission` VALUES (41, 3, 5, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（加密）',
  `salt` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码盐值',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像URL',
  `gender` tinyint NULL DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$jeHWtKljDN1X.HLBbexeFuUmfx/T0Ea6TjtAxicJ6my8HF3Frl83m', NULL, '系统管理员', '超级管理员', 'admin@example.com', '13800138000', NULL, 0, NULL, 1, NULL, NULL, '2025-12-05 17:59:19', '2025-12-05 20:00:47', 0, NULL);
INSERT INTO `sys_user` VALUES (6, 'yushuang', '$2a$10$fXzw5k5bJGkiB7357Mk0Geey1pVBvN2ehHePLEBL9tGfsBoBPoWy2', NULL, NULL, NULL, '18888888@qq.com', NULL, NULL, 0, NULL, 0, NULL, NULL, '2025-12-05 20:50:23', '2025-12-05 21:06:49', 0, NULL);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_role`(`user_id` ASC, `role_id` ASC, `deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_user_role` VALUES (2, 4, 3, NULL, '2025-12-05 19:35:53', 0);
INSERT INTO `sys_user_role` VALUES (3, 5, 3, NULL, '2025-12-05 19:38:34', 0);
INSERT INTO `sys_user_role` VALUES (4, 6, 3, NULL, '2025-12-05 20:14:00', 0);

SET FOREIGN_KEY_CHECKS = 1;
