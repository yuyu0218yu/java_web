/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80043 (8.0.43)
 Source Host           : localhost:3306
 Source Schema         : java_web

 Target Server Type    : MySQL
 Target Server Version : 80043 (8.0.43)
 File Encoding         : 65001

 Date: 09/12/2025 14:33:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典类型ID',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '字典类型（唯一标识）',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '组件类型', 'sys_component', 1, '系统可用前端组件', '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0);
INSERT INTO `sys_dict_type` VALUES (2, '常用图标', 'sys_icon', 1, 'Element Plus 常用图标', '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0);
INSERT INTO `sys_dict_type` VALUES (3, '权限标识', 'sys_permission', 1, '按钮与接口权限', '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典数据ID',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典类型',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典值',
  `dict_sort` int NULL DEFAULT 0 COMMENT '排序',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'CSS样式',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '表格样式',
  `is_default` tinyint NULL DEFAULT 0 COMMENT '是否默认：0-否，1-是',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 'sys_component', '仪表盘', 'Dashboard', 1, NULL, NULL, 1, 1, '首页仪表盘组件', '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0);
INSERT INTO `sys_dict_data` VALUES (2, 'sys_component', '角色管理', 'Roles', 2, NULL, NULL, 0, 1, '角色管理页面组件', '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0);
INSERT INTO `sys_dict_data` VALUES (3, 'sys_component', '用户管理', 'Users', 3, NULL, NULL, 0, 1, '用户管理页面组件', '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0);
INSERT INTO `sys_dict_data` VALUES (4, 'sys_icon', '房屋', 'House', 1, 'text-primary', NULL, 1, 1, '仪表盘图标', '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0);
INSERT INTO `sys_dict_data` VALUES (5, 'sys_icon', '用户', 'User', 2, 'text-info', NULL, 0, 1, '用户类页面图标', '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0);
INSERT INTO `sys_dict_data` VALUES (6, 'sys_icon', '设置', 'Setting', 3, 'text-warning', NULL, 0, 1, '系统设置图标', '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0);
INSERT INTO `sys_dict_data` VALUES (7, 'sys_icon', '角色', 'Avatar', 4, NULL, NULL, 0, 1, '角色管理图标', '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0);
INSERT INTO `sys_dict_data` VALUES (8, 'sys_permission', '用户查看', 'user:view', 1, 'success', NULL, 1, 1, '查看用户列表权限', '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0);
INSERT INTO `sys_dict_data` VALUES (9, 'sys_permission', '用户新增', 'user:create', 2, 'primary', NULL, 0, 1, '新增用户权限', '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0);
INSERT INTO `sys_dict_data` VALUES (10, 'sys_permission', '角色查看', 'role:view', 3, 'success', NULL, 0, 1, '查看角色列表权限', '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0);
INSERT INTO `sys_dict_data` VALUES (11, 'sys_permission', '角色编辑', 'role:update', 4, 'warning', NULL, 0, 1, '编辑角色权限', '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0);
INSERT INTO `sys_dict_data` VALUES (12, 'sys_permission', '菜单查看', 'menu:view', 5, 'info', NULL, 0, 1, '查看菜单权限', '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0);
INSERT INTO `sys_dict_data` VALUES (13, 'sys_permission', '菜单新增', 'menu:create', 6, 'primary', NULL, 0, 1, '新增菜单权限', '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0);
INSERT INTO `sys_dict_data` VALUES (14, 'sys_permission', '菜单删除', 'menu:delete', 7, 'danger', NULL, 0, 1, '删除菜单权限', '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0);
INSERT INTO `sys_dict_data` VALUES (15, 'sys_permission', '代码生成查看', 'generator:view', 8, 'info', NULL, 0, 1, '查看代码生成器权限', '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0);

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表 tree树表 sub主子表）',
  `tpl_web_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '前端模板类型（element-ui element-plus）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '其它生成选项（JSON格式）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE,
  INDEX `idx_gen_table_name`(`table_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '代码生成业务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table
-- ----------------------------
INSERT INTO `gen_table` VALUES (1, 'sys_dept', '结构表', NULL, NULL, 'Dept', 'crud', 'element-plus', 'com.zhangjiajie.system', 'sys', 'dept', '结构表', 'admin', '0', '/', NULL, 'admin', '2025-12-08 20:18:40', 'admin', '2025-12-08 20:18:40', NULL);

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '字典类型',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE,
  INDEX `idx_gen_column_table_id`(`table_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '代码生成业务表字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
INSERT INTO `gen_table_column` VALUES (1, 1, 'id', '结构ID', 'bigint', 'Long', 'id', '1', '1', '0', '1', '0', '0', '0', 'EQ', 'input', '', 0, 'admin', '2025-12-08 20:18:40', NULL, '2025-12-08 20:18:40');
INSERT INTO `gen_table_column` VALUES (2, 1, 'parent_id', '父结构ID (0=根节点)', 'bigint', 'Long', 'parentId', '0', '0', '0', '1', '1', '1', '0', 'EQ', 'input', '', 1, 'admin', '2025-12-08 20:18:40', NULL, '2025-12-08 20:18:40');
INSERT INTO `gen_table_column` VALUES (3, 1, 'ancestors', '祖先节点列表 (如: 0,1,2)', 'varchar(500)', 'String', 'ancestors', '0', '0', '0', '1', '1', '1', '0', 'EQ', 'input', '', 2, 'admin', '2025-12-08 20:18:40', NULL, '2025-12-08 20:18:40');
INSERT INTO `gen_table_column` VALUES (4, 1, 'dept_name', '结构名称', 'varchar(50)', 'String', 'deptName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 3, 'admin', '2025-12-08 20:18:40', NULL, '2025-12-08 20:18:40');
INSERT INTO `gen_table_column` VALUES (5, 1, 'dept_code', '结构编码', 'varchar(50)', 'String', 'deptCode', '0', '0', '0', '1', '1', '1', '0', 'EQ', 'input', '', 4, 'admin', '2025-12-08 20:18:40', NULL, '2025-12-08 20:18:40');
INSERT INTO `gen_table_column` VALUES (6, 1, 'leader', '负责人', 'varchar(50)', 'String', 'leader', '0', '0', '0', '1', '1', '1', '0', 'EQ', 'input', '', 5, 'admin', '2025-12-08 20:18:40', NULL, '2025-12-08 20:18:40');
INSERT INTO `gen_table_column` VALUES (7, 1, 'phone', '联系电话', 'varchar(20)', 'String', 'phone', '0', '0', '0', '1', '1', '1', '0', 'EQ', 'input', '', 6, 'admin', '2025-12-08 20:18:40', NULL, '2025-12-08 20:18:40');
INSERT INTO `gen_table_column` VALUES (8, 1, 'email', '邮箱', 'varchar(100)', 'String', 'email', '0', '0', '0', '1', '1', '1', '0', 'EQ', 'input', '', 7, 'admin', '2025-12-08 20:18:40', NULL, '2025-12-08 20:18:40');
INSERT INTO `gen_table_column` VALUES (9, 1, 'sort_order', '排序', 'int', 'Integer', 'sortOrder', '0', '0', '0', '1', '1', '1', '0', 'EQ', 'input', '', 8, 'admin', '2025-12-08 20:18:40', NULL, '2025-12-08 20:18:40');
INSERT INTO `gen_table_column` VALUES (10, 1, 'status', '状态：0-禁用，1-启用', 'tinyint', 'Integer', 'status', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'radio', '', 9, 'admin', '2025-12-08 20:18:40', NULL, '2025-12-08 20:18:40');
INSERT INTO `gen_table_column` VALUES (11, 1, 'create_time', '创建时间', 'datetime', 'LocalDateTime', 'createTime', '0', '0', '0', '1', '0', '1', '0', 'BETWEEN', 'datetime', '', 10, 'admin', '2025-12-08 20:18:40', NULL, '2025-12-08 20:18:40');
INSERT INTO `gen_table_column` VALUES (12, 1, 'update_time', '更新时间', 'datetime', 'LocalDateTime', 'updateTime', '0', '0', '0', '1', '0', '1', '0', 'BETWEEN', 'datetime', '', 11, 'admin', '2025-12-08 20:18:40', NULL, '2025-12-08 20:18:40');
INSERT INTO `gen_table_column` VALUES (13, 1, 'deleted', '逻辑删除：0-未删除，1-已删除', 'tinyint', 'Integer', 'deleted', '0', '0', '0', '1', '0', '0', '0', 'EQ', 'input', '', 12, 'admin', '2025-12-08 20:18:40', NULL, '2025-12-08 20:18:40');
INSERT INTO `gen_table_column` VALUES (14, 1, 'remark', '备注', 'varchar(500)', 'String', 'remark', '0', '0', '0', '1', '1', '1', '0', 'EQ', 'textarea', '', 13, 'admin', '2025-12-08 20:18:40', NULL, '2025-12-08 20:18:40');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '结构ID',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父结构ID (0=根节点)',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '祖先节点列表 (如: 0,1,2)',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '结构名称',
  `dept_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '结构编码',
  `leader` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_dept_code`(`dept_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '结构表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, 0, '0', '张家界旅游公司', 'HQ', '总经理', '0744-8888888', 'admin@zjj.com', 1, 1, '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0, '总公司');
INSERT INTO `sys_dept` VALUES (2, 1, '0,1', '技术部', 'TECH', '技术总监', NULL, NULL, 1, 1, '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0, NULL);
INSERT INTO `sys_dept` VALUES (3, 1, '0,1', '产品部', 'PRODUCT', '产品总监', NULL, NULL, 2, 1, '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0, NULL);
INSERT INTO `sys_dept` VALUES (4, 1, '0,1', '运营部', 'OPERATION', '运营总监', NULL, NULL, 3, 1, '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0, NULL);
INSERT INTO `sys_dept` VALUES (5, 2, '0,1,2', '后端组', 'TECH_BE', '后端负责人', NULL, NULL, 1, 1, '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0, NULL);
INSERT INTO `sys_dept` VALUES (6, 2, '0,1,2', '前端组', 'TECH_FE', '前端负责人', NULL, NULL, 2, 1, '2025-12-06 00:00:00', '2025-12-06 00:00:00', 0, NULL);
INSERT INTO `sys_dept` VALUES (7, 1, '0,1', '默认用户部', 'DEFAULT', '系统管理员', NULL, NULL, 99, 1, '2025-12-08 00:00:00', '2025-12-08 00:00:00', 0, '新注册用户默认归属部门');

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文件信息表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '登录日志表' ROW_FORMAT = DYNAMIC;

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
INSERT INTO `sys_login_log` VALUES (13, 'admin', '127.0.0.1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Windsurf/1.105.0 Chrome/138.0.7204.251 Electron/37.6.0 Safari/537.36', 1, NULL, '2025-12-07 14:32:03', '2025-12-07 14:32:03', 0, NULL);
INSERT INTO `sys_login_log` VALUES (14, 'yushuang', '127.0.0.1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Windsurf/1.105.0 Chrome/138.0.7204.251 Electron/37.6.0 Safari/537.36', 0, '账号已被禁用，请联系管理员', '2025-12-07 14:49:22', '2025-12-07 14:49:22', 0, NULL);
INSERT INTO `sys_login_log` VALUES (15, 'yushuang', '127.0.0.1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Windsurf/1.105.0 Chrome/138.0.7204.251 Electron/37.6.0 Safari/537.36', 0, '账号已被禁用，请联系管理员', '2025-12-07 14:49:30', '2025-12-07 14:49:30', 0, NULL);
INSERT INTO `sys_login_log` VALUES (16, 'admin', '127.0.0.1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Windsurf/1.105.0 Chrome/138.0.7204.251 Electron/37.6.0 Safari/537.36', 1, NULL, '2025-12-07 14:49:36', '2025-12-07 14:49:36', 0, NULL);
INSERT INTO `sys_login_log` VALUES (17, 'yushuang', '127.0.0.1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Windsurf/1.105.0 Chrome/138.0.7204.251 Electron/37.6.0 Safari/537.36', 0, '账号已被禁用，请联系管理员', '2025-12-07 14:50:15', '2025-12-07 14:50:15', 0, NULL);
INSERT INTO `sys_login_log` VALUES (18, 'yushuang', '127.0.0.1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Windsurf/1.105.0 Chrome/138.0.7204.251 Electron/37.6.0 Safari/537.36', 0, '账号已被禁用，请联系管理员', '2025-12-07 14:50:21', '2025-12-07 14:50:21', 0, NULL);
INSERT INTO `sys_login_log` VALUES (19, 'yushuang', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 0, '用户名或密码错误', '2025-12-08 20:17:00', '2025-12-08 20:17:00', 0, NULL);
INSERT INTO `sys_login_log` VALUES (20, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:17:08', '2025-12-08 20:17:08', 0, NULL);
INSERT INTO `sys_login_log` VALUES (21, 'yushuang', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 0, '用户名或密码错误', '2025-12-08 20:38:03', '2025-12-08 20:38:03', 0, NULL);
INSERT INTO `sys_login_log` VALUES (22, 'yushuang', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:38:08', '2025-12-08 20:38:08', 0, NULL);
INSERT INTO `sys_login_log` VALUES (23, 'yushuang', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:38:26', '2025-12-08 20:38:26', 0, NULL);
INSERT INTO `sys_login_log` VALUES (24, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:39:17', '2025-12-08 20:39:17', 0, NULL);
INSERT INTO `sys_login_log` VALUES (25, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 21:05:51', '2025-12-08 21:05:51', 0, NULL);
INSERT INTO `sys_login_log` VALUES (26, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 21:09:31', '2025-12-08 21:09:31', 0, NULL);
INSERT INTO `sys_login_log` VALUES (27, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 0, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'r.data_scope\' in \'field list\'\r\n### The error may exist in com/zhangjiajie/system/mapper/UserMapper.java (best guess)\r\n### The error may involve com.zhangjiajie.system.mapper.UserMapper.selectUserWithRoleByUsername-Inline\r\n### The error occurred while setting parameters\r\n### SQL: SELECT u.id, u.username, u.password, u.salt, u.real_name, u.nickname, u.email, u.phone, u.avatar, u.dept_id, u.gender, u.birthday, u.status, u.last_login_time, u.last_login_ip, u.create_time, u.update_time, u.deleted, u.remark, r.id as role_id, r.role_name, r.role_code, r.data_scope, d.dept_name FROM sys_user u LEFT JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 LEFT JOIN sys_role r ON ur.role_id = r.id AND r.deleted = 0 LEFT JOIN sys_dept d ON u.dept_id = d.id AND d.deleted = 0 WHERE u.deleted = 0 AND u.username = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'r.data_scope\' in \'field list\'\n; bad SQL grammar []', '2025-12-08 23:35:24', '2025-12-08 23:35:24', 0, NULL);
INSERT INTO `sys_login_log` VALUES (28, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 0, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'r.data_scope\' in \'field list\'\r\n### The error may exist in com/zhangjiajie/system/mapper/UserMapper.java (best guess)\r\n### The error may involve com.zhangjiajie.system.mapper.UserMapper.selectUserWithRoleByUsername-Inline\r\n### The error occurred while setting parameters\r\n### SQL: SELECT u.id, u.username, u.password, u.salt, u.real_name, u.nickname, u.email, u.phone, u.avatar, u.dept_id, u.gender, u.birthday, u.status, u.last_login_time, u.last_login_ip, u.create_time, u.update_time, u.deleted, u.remark, r.id as role_id, r.role_name, r.role_code, r.data_scope, d.dept_name FROM sys_user u LEFT JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 LEFT JOIN sys_role r ON ur.role_id = r.id AND r.deleted = 0 LEFT JOIN sys_dept d ON u.dept_id = d.id AND d.deleted = 0 WHERE u.deleted = 0 AND u.username = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'r.data_scope\' in \'field list\'\n; bad SQL grammar []', '2025-12-08 23:35:28', '2025-12-08 23:35:28', 0, NULL);
INSERT INTO `sys_login_log` VALUES (29, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 0, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'r.data_scope\' in \'field list\'\r\n### The error may exist in com/zhangjiajie/system/mapper/UserMapper.java (best guess)\r\n### The error may involve com.zhangjiajie.system.mapper.UserMapper.selectUserWithRoleByUsername-Inline\r\n### The error occurred while setting parameters\r\n### SQL: SELECT u.id, u.username, u.password, u.salt, u.real_name, u.nickname, u.email, u.phone, u.avatar, u.dept_id, u.gender, u.birthday, u.status, u.last_login_time, u.last_login_ip, u.create_time, u.update_time, u.deleted, u.remark, r.id as role_id, r.role_name, r.role_code, r.data_scope, d.dept_name FROM sys_user u LEFT JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 LEFT JOIN sys_role r ON ur.role_id = r.id AND r.deleted = 0 LEFT JOIN sys_dept d ON u.dept_id = d.id AND d.deleted = 0 WHERE u.deleted = 0 AND u.username = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'r.data_scope\' in \'field list\'\n; bad SQL grammar []', '2025-12-08 23:35:35', '2025-12-08 23:35:35', 0, NULL);
INSERT INTO `sys_login_log` VALUES (30, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 0, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'r.data_scope\' in \'field list\'\r\n### The error may exist in com/zhangjiajie/system/mapper/UserMapper.java (best guess)\r\n### The error may involve com.zhangjiajie.system.mapper.UserMapper.selectUserWithRoleByUsername-Inline\r\n### The error occurred while setting parameters\r\n### SQL: SELECT u.id, u.username, u.password, u.salt, u.real_name, u.nickname, u.email, u.phone, u.avatar, u.dept_id, u.gender, u.birthday, u.status, u.last_login_time, u.last_login_ip, u.create_time, u.update_time, u.deleted, u.remark, r.id as role_id, r.role_name, r.role_code, r.data_scope, d.dept_name FROM sys_user u LEFT JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 LEFT JOIN sys_role r ON ur.role_id = r.id AND r.deleted = 0 LEFT JOIN sys_dept d ON u.dept_id = d.id AND d.deleted = 0 WHERE u.deleted = 0 AND u.username = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'r.data_scope\' in \'field list\'\n; bad SQL grammar []', '2025-12-08 23:35:38', '2025-12-08 23:35:38', 0, NULL);
INSERT INTO `sys_login_log` VALUES (31, 'yushuang', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 0, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'r.data_scope\' in \'field list\'\r\n### The error may exist in com/zhangjiajie/system/mapper/UserMapper.java (best guess)\r\n### The error may involve com.zhangjiajie.system.mapper.UserMapper.selectUserWithRoleByUsername-Inline\r\n### The error occurred while setting parameters\r\n### SQL: SELECT u.id, u.username, u.password, u.salt, u.real_name, u.nickname, u.email, u.phone, u.avatar, u.dept_id, u.gender, u.birthday, u.status, u.last_login_time, u.last_login_ip, u.create_time, u.update_time, u.deleted, u.remark, r.id as role_id, r.role_name, r.role_code, r.data_scope, d.dept_name FROM sys_user u LEFT JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 LEFT JOIN sys_role r ON ur.role_id = r.id AND r.deleted = 0 LEFT JOIN sys_dept d ON u.dept_id = d.id AND d.deleted = 0 WHERE u.deleted = 0 AND u.username = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'r.data_scope\' in \'field list\'\n; bad SQL grammar []', '2025-12-08 23:35:52', '2025-12-08 23:35:52', 0, NULL);
INSERT INTO `sys_login_log` VALUES (32, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 23:39:34', '2025-12-08 23:39:34', 0, NULL);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '路由参数',
  `is_frame` tinyint NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` tinyint NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` tinyint NULL DEFAULT 1 COMMENT '菜单状态（0隐藏 1显示）',
  `status` tinyint NULL DEFAULT 1 COMMENT '菜单状态（0停用 1正常）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1055 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '仪表盘', 0, 1, '/dashboard', 'Dashboard', NULL, 1, 0, 'C', 1, 1, NULL, 'House', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (2, '个人中心', 0, 2, '/profile', 'Profile', NULL, 1, 0, 'C', 1, 1, NULL, 'User', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (3, '系统管理', 0, 3, '/system', NULL, NULL, 1, 0, 'M', 1, 1, NULL, 'Setting', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 3, 1, '/users', 'Users', NULL, 1, 0, 'C', 1, 1, 'user:view', 'UserFilled', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 3, 2, '/roles', 'Roles', NULL, 1, 0, 'C', 1, 1, 'role:view', 'Avatar', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 3, 3, '/menus', 'Menus', NULL, 1, 0, 'C', 1, 1, 'menu:view', 'Menu', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (104, '代码生成器', 3, 4, '/generator', 'Generator', NULL, 1, 0, 'C', 1, 1, 'generator:view', 'Cpu', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (105, '组织结构', 3, 5, '/depts', 'Depts', NULL, 1, 0, 'C', 1, 1, 'dept:view', 'OfficeBuilding', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (1001, '用户查询', 100, 1, '', NULL, NULL, 1, 0, 'F', 1, 1, 'user:view', '#', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (1002, '用户新增', 100, 2, '', NULL, NULL, 1, 0, 'F', 1, 1, 'user:create', '#', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (1003, '用户修改', 100, 3, '', NULL, NULL, 1, 0, 'F', 1, 1, 'user:update', '#', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (1004, '用户删除', 100, 4, '', NULL, NULL, 1, 0, 'F', 1, 1, 'user:delete', '#', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (1011, '角色查询', 101, 1, '', NULL, NULL, 1, 0, 'F', 1, 1, 'role:view', '#', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (1012, '角色新增', 101, 2, '', NULL, NULL, 1, 0, 'F', 1, 1, 'role:create', '#', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (1013, '角色修改', 101, 3, '', NULL, NULL, 1, 0, 'F', 1, 1, 'role:update', '#', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (1014, '角色删除', 101, 4, '', NULL, NULL, 1, 0, 'F', 1, 1, 'role:delete', '#', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (1021, '菜单查询', 102, 1, '', NULL, NULL, 1, 0, 'F', 1, 1, 'menu:view', '#', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (1022, '菜单新增', 102, 2, '', NULL, NULL, 1, 0, 'F', 1, 1, 'menu:create', '#', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (1023, '菜单修改', 102, 3, '', NULL, NULL, 1, 0, 'F', 1, 1, 'menu:update', '#', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (1024, '菜单删除', 102, 4, '', NULL, NULL, 1, 0, 'F', 1, 1, 'menu:delete', '#', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (1051, '组织查询', 105, 1, '', NULL, NULL, 1, 0, 'F', 1, 1, 'dept:view', '#', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (1052, '组织新增', 105, 2, '', NULL, NULL, 1, 0, 'F', 1, 1, 'dept:create', '#', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (1053, '组织修改', 105, 3, '', NULL, NULL, 1, 0, 'F', 1, 1, 'dept:edit', '#', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (1054, '组织删除', 105, 4, '', NULL, NULL, 1, 0, 'F', 1, 1, 'dept:delete', '#', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '操作用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作用户名',
  `operation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作类型',
  `module` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模块名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求方法(GET/POST/PUT/DELETE)',
  `request_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求URL',
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
) ENGINE = InnoDB AUTO_INCREMENT = 54 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '操作日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------
INSERT INTO `sys_operation_log` VALUES (1, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.yushuang.demo.controller.AuthController.login', NULL, 311, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 18:01:22', '2025-12-05 18:01:22', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (2, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.yushuang.demo.controller.AuthController.login', NULL, 56, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 18:05:07', '2025-12-05 18:05:07', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (3, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.yushuang.demo.controller.AuthController.login', NULL, 57, '0:0:0:0:0:0:0:1', 'curl/8.16.0', 1, NULL, '2025-12-05 18:11:15', '2025-12-05 18:11:15', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (4, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.yushuang.demo.controller.AuthController.login', NULL, 250, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 18:18:03', '2025-12-05 18:18:03', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (5, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.yushuang.demo.controller.AuthController.login', NULL, 246, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 19:28:07', '2025-12-05 19:28:07', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (6, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.yushuang.demo.controller.AuthController.login', NULL, 102, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 19:37:21', '2025-12-05 19:37:21', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (7, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.yushuang.demo.controller.AuthController.login', NULL, 58, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 19:38:38', '2025-12-05 19:38:38', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (8, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.yushuang.demo.controller.AuthController.login', NULL, 58, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 19:39:23', '2025-12-05 19:39:23', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (9, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.yushuang.demo.controller.AuthController.login', NULL, 60, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 19:50:01', '2025-12-05 19:50:01', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (10, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.yushuang.demo.controller.AuthController.login', NULL, 58, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 19:50:51', '2025-12-05 19:50:51', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (11, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.yushuang.demo.controller.AuthController.login', NULL, 260, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 20:14:05', '2025-12-05 20:14:05', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (12, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.yushuang.demo.controller.AuthController.login', NULL, 129, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-05 20:50:28', '2025-12-05 20:50:28', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (13, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.login', NULL, 141, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Windsurf/1.105.0 Chrome/138.0.7204.251 Electron/37.6.0 Safari/537.36', 1, NULL, '2025-12-07 14:32:03', '2025-12-07 14:32:03', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (14, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.login', NULL, 245, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Windsurf/1.105.0 Chrome/138.0.7204.251 Electron/37.6.0 Safari/537.36', 1, NULL, '2025-12-07 14:49:22', '2025-12-07 14:49:22', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (15, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.login', NULL, 56, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Windsurf/1.105.0 Chrome/138.0.7204.251 Electron/37.6.0 Safari/537.36', 1, NULL, '2025-12-07 14:49:30', '2025-12-07 14:49:30', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (16, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.login', NULL, 112, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Windsurf/1.105.0 Chrome/138.0.7204.251 Electron/37.6.0 Safari/537.36', 1, NULL, '2025-12-07 14:49:36', '2025-12-07 14:49:36', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (17, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.login', NULL, 54, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Windsurf/1.105.0 Chrome/138.0.7204.251 Electron/37.6.0 Safari/537.36', 1, NULL, '2025-12-07 14:50:15', '2025-12-07 14:50:15', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (18, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.login', NULL, 54, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Windsurf/1.105.0 Chrome/138.0.7204.251 Electron/37.6.0 Safari/537.36', 1, NULL, '2025-12-07 14:50:21', '2025-12-07 14:50:21', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (19, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.login', NULL, 363, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:17:00', '2025-12-08 20:17:00', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (20, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.login', NULL, 78, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:17:08', '2025-12-08 20:17:08', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (21, NULL, 'admin', '获取用户信息', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 3, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:17:08', '2025-12-08 20:17:08', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (22, NULL, 'admin', '获取用户信息', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 4, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:18:34', '2025-12-08 20:18:34', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (23, NULL, 'admin', '获取用户信息', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 2, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:20:27', '2025-12-08 20:20:27', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (24, NULL, 'admin', '获取用户信息', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 2, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:23:21', '2025-12-08 20:23:21', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (25, NULL, 'admin', '获取用户信息', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 2, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:24:51', '2025-12-08 20:24:51', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (26, NULL, 'admin', '获取用户信息', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 7, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:34:41', '2025-12-08 20:34:41', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (27, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.login', NULL, 68, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:38:03', '2025-12-08 20:38:03', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (28, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.login', NULL, 73, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:38:08', '2025-12-08 20:38:08', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (29, NULL, 'yushuang', '获取用户信息', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 3, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:38:08', '2025-12-08 20:38:08', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (30, NULL, 'yushuang', '获取用户信息', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 2, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:38:19', '2025-12-08 20:38:19', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (31, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.login', NULL, 61, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:38:26', '2025-12-08 20:38:26', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (32, NULL, 'yushuang', '获取用户信息', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 3, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:38:26', '2025-12-08 20:38:26', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (33, NULL, 'yushuang', '获取用户信息', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 2, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:39:00', '2025-12-08 20:39:00', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (34, NULL, 'anonymousUser', '用户登录', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.login', NULL, 62, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:39:17', '2025-12-08 20:39:17', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (35, NULL, 'admin', '获取用户信息', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 1, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:39:17', '2025-12-08 20:39:17', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (36, NULL, 'admin', '获取用户信息', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 2, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:39:38', '2025-12-08 20:39:38', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (37, NULL, 'admin', '获取用户信息', NULL, NULL, NULL, 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 3, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 20:41:16', '2025-12-08 20:41:16', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (38, NULL, 'admin', '获取用户信息', '认证管理', 'GET', '/api/auth/userinfo', 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 43, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 21:05:41', '2025-12-08 21:05:41', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (39, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 110, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 21:05:51', '2025-12-08 21:05:51', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (40, NULL, 'admin', '获取用户信息', '认证管理', 'GET', '/api/auth/userinfo', 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 8, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 21:05:51', '2025-12-08 21:05:51', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (41, NULL, 'admin', '获取用户信息', '认证管理', 'GET', '/api/auth/userinfo', 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 7, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 21:07:33', '2025-12-08 21:07:33', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (42, NULL, 'admin', '获取用户信息', '认证管理', 'GET', '/api/auth/userinfo', 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 4, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 21:07:33', '2025-12-08 21:07:33', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (43, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 82, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 21:09:31', '2025-12-08 21:09:31', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (44, NULL, 'admin', '获取用户信息', '认证管理', 'GET', '/api/auth/userinfo', 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 2, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 21:09:31', '2025-12-08 21:09:31', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (45, NULL, 'admin', '获取用户信息', '认证管理', 'GET', '/api/auth/userinfo', 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 1, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 21:10:35', '2025-12-08 21:10:35', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (46, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 549, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 23:35:24', '2025-12-08 23:35:24', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (47, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 17, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 23:35:28', '2025-12-08 23:35:28', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (48, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 3, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 23:35:35', '2025-12-08 23:35:35', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (49, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 3, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 23:35:38', '2025-12-08 23:35:38', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (50, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 4, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 23:35:52', '2025-12-08 23:35:52', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (51, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 449, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 23:39:34', '2025-12-08 23:39:34', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (52, NULL, 'admin', '获取用户信息', '认证管理', 'GET', '/api/auth/userinfo', 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 4, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 23:39:34', '2025-12-08 23:39:34', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (53, NULL, 'admin', '获取用户信息', '认证管理', 'GET', '/api/auth/userinfo', 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 8, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36', 1, NULL, '2025-12-08 23:42:54', '2025-12-08 23:42:54', 0, NULL);


-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色编码',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '角色描述',
  `data_scope` tinyint NULL DEFAULT 1 COMMENT '数据范围',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_code`(`role_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'Super Admin', 'SUPER_ADMIN', 'System super administrator with all permissions', 1, 1, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0, NULL);
INSERT INTO `sys_role` VALUES (2, 'Admin', 'ADMIN', 'System administrator with most permissions', 2, 2, 1, '2025-12-05 17:59:19', '2025-12-08 23:38:13', 0, NULL);
INSERT INTO `sys_role` VALUES (3, 'User', 'USER', 'Regular user with basic permissions', 4, 3, 1, '2025-12-05 17:59:19', '2025-12-08 23:38:13', 0, NULL);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_menu`(`role_id` ASC, `menu_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1, 1, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (2, 1, 2, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (3, 1, 3, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (4, 1, 100, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (5, 1, 101, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (6, 1, 102, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (7, 1, 104, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (9, 1, 1001, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (10, 1, 1002, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (11, 1, 1003, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (12, 1, 1004, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (13, 1, 1011, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (14, 1, 1012, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (15, 1, 1013, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (16, 1, 1014, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (17, 1, 1021, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (18, 1, 1022, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (19, 1, 1023, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (20, 1, 1024, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (32, 1, 105, '2025-12-08 21:10:31');
INSERT INTO `sys_role_menu` VALUES (33, 1, 1051, '2025-12-08 21:10:31');
INSERT INTO `sys_role_menu` VALUES (34, 1, 1052, '2025-12-08 21:10:31');
INSERT INTO `sys_role_menu` VALUES (35, 1, 1053, '2025-12-08 21:10:31');
INSERT INTO `sys_role_menu` VALUES (36, 1, 1054, '2025-12-08 21:10:31');


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
  `dept_id` bigint NULL DEFAULT NULL COMMENT '结构ID',
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
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  INDEX `idx_dept_id`(`dept_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$jeHWtKljDN1X.HLBbexeFuUmfx/T0Ea6TjtAxicJ6my8HF3Frl83m', NULL, '系统管理员', '超级管理员', 'admin@example.com', '13800138000', NULL, 1, 0, NULL, 1, NULL, NULL, '2025-12-05 17:59:19', '2025-12-05 20:00:47', 0, NULL);
INSERT INTO `sys_user` VALUES (6, 'yushuang', '$2a$10$QkJUrmOjNl1bCtDSMDonYuFfk/Gb7NxSsswy2RSJp04xZ4GJQ1Y4.', NULL, NULL, NULL, '18888888@qq.com', NULL, NULL, NULL, 0, NULL, 1, NULL, NULL, '2025-12-05 20:50:23', '2025-12-08 20:38:00', 0, NULL);

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
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_user_role` VALUES (2, 4, 3, NULL, '2025-12-05 19:35:53', 0);
INSERT INTO `sys_user_role` VALUES (3, 5, 3, NULL, '2025-12-05 19:38:34', 0);
INSERT INTO `sys_user_role` VALUES (4, 6, 3, NULL, '2025-12-05 20:14:00', 0);

SET FOREIGN_KEY_CHECKS = 1;


-- ===========================================
-- 新增模块数据库表结构
-- 包含：通知公告、定时任务、登录日志增强
-- ===========================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_notice (通知公告表)
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` varchar(100) NOT NULL COMMENT '公告标题',
  `content` text COMMENT '公告内容',
  `notice_type` tinyint NOT NULL DEFAULT 1 COMMENT '公告类型：1-通知，2-公告',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-关闭，1-正常',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_notice_type` (`notice_type`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知公告表';

-- ----------------------------
-- Table structure for sys_job (定时任务表)
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(100) NOT NULL COMMENT '任务名称',
  `job_group` varchar(50) NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(100) DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` tinyint DEFAULT 1 COMMENT '计划执行错误策略：1-立即执行，2-执行一次，3-放弃执行',
  `concurrent` tinyint DEFAULT 1 COMMENT '是否并发执行：0-允许，1-禁止',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-暂停，1-正常',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_job_group` (`job_group`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='定时任务表';

-- ----------------------------
-- Table structure for sys_job_log (定时任务执行日志表)
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `job_id` bigint NOT NULL COMMENT '任务ID',
  `job_name` varchar(100) NOT NULL COMMENT '任务名称',
  `job_group` varchar(50) NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) DEFAULT '' COMMENT '调用目标字符串',
  `job_message` varchar(500) DEFAULT '' COMMENT '日志信息',
  `status` tinyint DEFAULT 0 COMMENT '执行状态：0-失败，1-成功',
  `exception_info` text COMMENT '异常信息',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `elapsed_time` bigint DEFAULT 0 COMMENT '耗时（毫秒）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_job_id` (`job_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='定时任务执行日志表';

-- ----------------------------
-- 初始化示例数据
-- ----------------------------

-- 示例通知公告
INSERT INTO `sys_notice` (`title`, `content`, `notice_type`, `status`, `create_by`, `remark`) VALUES
('系统更新通知', '系统将于今晚22:00-23:00进行例行维护升级，届时服务将暂时不可用，请各位用户提前做好准备。', 1, 1, 'admin', '系统维护通知'),
('新功能上线公告', '本次更新新增了以下功能：1.字典管理 2.操作日志 3.登录日志 4.定时任务管理 5.数据导入导出。欢迎大家使用体验！', 2, 1, 'admin', '功能更新公告');

-- 示例定时任务
INSERT INTO `sys_job` (`job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`, `concurrent`, `status`, `create_by`, `remark`) VALUES
('系统健康检查', 'SYSTEM', 'systemHealthTask.check()', '0 0/30 * * * ?', 1, 1, 1, 'admin', '每30分钟检查一次系统健康状态'),
('日志清理任务', 'SYSTEM', 'logCleanTask.clean(30)', '0 0 2 * * ?', 1, 1, 1, 'admin', '每天凌晨2点清理30天前的日志'),
('数据备份任务', 'SYSTEM', 'dataBackupTask.backup()', '0 0 3 * * ?', 1, 1, 0, 'admin', '每天凌晨3点备份数据（暂停状态）');

-- ===========================================
-- 新增模块菜单权限配置
-- 包含：字典管理、日志管理、通知公告、定时任务
-- ===========================================

-- ----------------------------
-- 1. 日志管理目录
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_time`, `update_time`, `deleted`, `remark`) VALUES
(4, '日志管理', 0, 4, '/logs', NULL, NULL, 1, 0, 'M', 1, 1, NULL, 'Document', NOW(), NOW(), 0, '日志管理目录');

-- 操作日志菜单
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_time`, `update_time`, `deleted`, `remark`) VALUES
(106, '操作日志', 4, 1, '/logs/operation', 'OperationLog', NULL, 1, 0, 'C', 1, 1, 'log:operation:view', 'List', NOW(), NOW(), 0, '操作日志菜单');

-- 操作日志按钮权限
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_time`, `update_time`, `deleted`, `remark`) VALUES
(1061, '操作日志查询', 106, 1, '', NULL, NULL, 1, 0, 'F', 1, 1, 'log:operation:view', '#', NOW(), NOW(), 0, ''),
(1062, '操作日志删除', 106, 2, '', NULL, NULL, 1, 0, 'F', 1, 1, 'log:operation:delete', '#', NOW(), NOW(), 0, ''),
(1063, '操作日志清空', 106, 3, '', NULL, NULL, 1, 0, 'F', 1, 1, 'log:operation:clean', '#', NOW(), NOW(), 0, '');

-- 登录日志菜单
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_time`, `update_time`, `deleted`, `remark`) VALUES
(107, '登录日志', 4, 2, '/logs/login', 'LoginLog', NULL, 1, 0, 'C', 1, 1, 'log:login:view', 'UserFilled', NOW(), NOW(), 0, '登录日志菜单');

-- 登录日志按钮权限
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_time`, `update_time`, `deleted`, `remark`) VALUES
(1071, '登录日志查询', 107, 1, '', NULL, NULL, 1, 0, 'F', 1, 1, 'log:login:view', '#', NOW(), NOW(), 0, ''),
(1072, '登录日志删除', 107, 2, '', NULL, NULL, 1, 0, 'F', 1, 1, 'log:login:delete', '#', NOW(), NOW(), 0, ''),
(1073, '登录日志清空', 107, 3, '', NULL, NULL, 1, 0, 'F', 1, 1, 'log:login:clean', '#', NOW(), NOW(), 0, '');

-- ----------------------------
-- 2. 字典管理菜单 (挂在系统管理下)
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_time`, `update_time`, `deleted`, `remark`) VALUES
(108, '字典管理', 3, 6, '/dicts', 'DictType', NULL, 1, 0, 'C', 1, 1, 'dict:view', 'Collection', NOW(), NOW(), 0, '字典类型菜单');

-- 字典类型按钮权限
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_time`, `update_time`, `deleted`, `remark`) VALUES
(1081, '字典查询', 108, 1, '', NULL, NULL, 1, 0, 'F', 1, 1, 'dict:view', '#', NOW(), NOW(), 0, ''),
(1082, '字典新增', 108, 2, '', NULL, NULL, 1, 0, 'F', 1, 1, 'dict:create', '#', NOW(), NOW(), 0, ''),
(1083, '字典修改', 108, 3, '', NULL, NULL, 1, 0, 'F', 1, 1, 'dict:update', '#', NOW(), NOW(), 0, ''),
(1084, '字典删除', 108, 4, '', NULL, NULL, 1, 0, 'F', 1, 1, 'dict:delete', '#', NOW(), NOW(), 0, '');

-- ----------------------------
-- 3. 通知公告菜单 (挂在系统管理下)
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_time`, `update_time`, `deleted`, `remark`) VALUES
(109, '通知公告', 3, 7, '/notices', 'Notice', NULL, 1, 0, 'C', 1, 1, 'notice:view', 'Bell', NOW(), NOW(), 0, '通知公告菜单');

-- 通知公告按钮权限
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_time`, `update_time`, `deleted`, `remark`) VALUES
(1091, '公告查询', 109, 1, '', NULL, NULL, 1, 0, 'F', 1, 1, 'notice:view', '#', NOW(), NOW(), 0, ''),
(1092, '公告新增', 109, 2, '', NULL, NULL, 1, 0, 'F', 1, 1, 'notice:create', '#', NOW(), NOW(), 0, ''),
(1093, '公告修改', 109, 3, '', NULL, NULL, 1, 0, 'F', 1, 1, 'notice:update', '#', NOW(), NOW(), 0, ''),
(1094, '公告删除', 109, 4, '', NULL, NULL, 1, 0, 'F', 1, 1, 'notice:delete', '#', NOW(), NOW(), 0, '');

-- ----------------------------
-- 4. 定时任务菜单 (挂在系统管理下)
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_time`, `update_time`, `deleted`, `remark`) VALUES
(110, '定时任务', 3, 8, '/jobs', 'Job', NULL, 1, 0, 'C', 1, 1, 'job:view', 'Timer', NOW(), NOW(), 0, '定时任务菜单');

-- 定时任务按钮权限
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_time`, `update_time`, `deleted`, `remark`) VALUES
(1101, '任务查询', 110, 1, '', NULL, NULL, 1, 0, 'F', 1, 1, 'job:view', '#', NOW(), NOW(), 0, ''),
(1102, '任务新增', 110, 2, '', NULL, NULL, 1, 0, 'F', 1, 1, 'job:create', '#', NOW(), NOW(), 0, ''),
(1103, '任务修改', 110, 3, '', NULL, NULL, 1, 0, 'F', 1, 1, 'job:update', '#', NOW(), NOW(), 0, ''),
(1104, '任务删除', 110, 4, '', NULL, NULL, 1, 0, 'F', 1, 1, 'job:delete', '#', NOW(), NOW(), 0, ''),
(1105, '任务执行', 110, 5, '', NULL, NULL, 1, 0, 'F', 1, 1, 'job:run', '#', NOW(), NOW(), 0, '');

-- 任务日志菜单
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_time`, `update_time`, `deleted`, `remark`) VALUES
(111, '任务日志', 3, 9, '/jobs/logs', 'JobLog', NULL, 1, 0, 'C', 1, 1, 'job:view', 'Tickets', NOW(), NOW(), 0, '定时任务日志菜单');

-- ----------------------------
-- 5. 给超级管理员(role_id=1)分配新菜单权限
-- ----------------------------
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `create_time`) VALUES
-- 日志管理目录
(1, 4, NOW()),
-- 操作日志
(1, 106, NOW()),
(1, 1061, NOW()),
(1, 1062, NOW()),
(1, 1063, NOW()),
-- 登录日志
(1, 107, NOW()),
(1, 1071, NOW()),
(1, 1072, NOW()),
(1, 1073, NOW()),
-- 字典管理
(1, 108, NOW()),
(1, 1081, NOW()),
(1, 1082, NOW()),
(1, 1083, NOW()),
(1, 1084, NOW()),
-- 通知公告
(1, 109, NOW()),
(1, 1091, NOW()),
(1, 1092, NOW()),
(1, 1093, NOW()),
(1, 1094, NOW()),
-- 定时任务
(1, 110, NOW()),
(1, 1101, NOW()),
(1, 1102, NOW()),
(1, 1103, NOW()),
(1, 1104, NOW()),
(1, 1105, NOW()),
-- 任务日志
(1, 111, NOW());

SET FOREIGN_KEY_CHECKS = 1;
