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

 Date: 29/12/2025 13:07:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
-- Table structure for portal_banner
-- ----------------------------
DROP TABLE IF EXISTS `portal_banner`;
CREATE TABLE `portal_banner`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片URL',
  `link_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '跳转链接',
  `link_type` tinyint NULL DEFAULT 1 COMMENT '链接类型：1景点 2攻略 3外链',
  `target_id` bigint NULL DEFAULT NULL COMMENT '目标ID',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '轮播图表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of portal_banner
-- ----------------------------

-- ----------------------------
-- Table structure for portal_comment
-- ----------------------------
DROP TABLE IF EXISTS `portal_comment`;
CREATE TABLE `portal_comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `target_type` tinyint NOT NULL COMMENT '目标类型：1景点 2攻略',
  `target_id` bigint NOT NULL COMMENT '目标ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `user_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '评论内容',
  `images` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片（JSON数组）',
  `rating` tinyint NULL DEFAULT NULL COMMENT '评分1-5',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父评论ID',
  `like_count` int NULL DEFAULT 0 COMMENT '点赞数',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0待审核 1已发布',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_target`(`target_type` ASC, `target_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of portal_comment
-- ----------------------------

-- ----------------------------
-- Table structure for portal_favorite
-- ----------------------------
DROP TABLE IF EXISTS `portal_favorite`;
CREATE TABLE `portal_favorite`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `target_type` tinyint NOT NULL COMMENT '目标类型：1景点 2攻略',
  `target_id` bigint NOT NULL COMMENT '目标ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_target`(`user_id` ASC, `target_type` ASC, `target_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of portal_favorite
-- ----------------------------

-- ----------------------------
-- Table structure for portal_guide
-- ----------------------------
DROP TABLE IF EXISTS `portal_guide`;
CREATE TABLE `portal_guide`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `cover_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面图',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '内容（富文本）',
  `author_id` bigint NULL DEFAULT NULL COMMENT '作者ID',
  `author_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '作者名称',
  `author_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '作者头像',
  `guide_type` tinyint NULL DEFAULT 1 COMMENT '类型：1攻略 2游记',
  `scenic_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联景点ID（逗号分隔）',
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签（逗号分隔）',
  `view_count` int NULL DEFAULT 0 COMMENT '浏览量',
  `like_count` int NULL DEFAULT 0 COMMENT '点赞数',
  `comment_count` int NULL DEFAULT 0 COMMENT '评论数',
  `is_official` tinyint NULL DEFAULT 0 COMMENT '是否官方',
  `is_recommend` tinyint NULL DEFAULT 0 COMMENT '是否推荐',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0待审核 1已发布 2已下架',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_author_id`(`author_id` ASC) USING BTREE,
  INDEX `idx_guide_type`(`guide_type` ASC) USING BTREE,
  INDEX `idx_is_recommend`(`is_recommend` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '攻略表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of portal_guide
-- ----------------------------
INSERT INTO `portal_guide` VALUES (1, '张家界三日游完美攻略', '/uploads/guide/guide1.jpg', '<p>张家界三日游最佳路线规划，带你玩转天门山、森林公园、玻璃桥...</p>', 1, '官方小编', NULL, 1, '1,2,3', '三日游,经典路线,必看', 1, 1, 0, 1, 1, 1, '2025-12-29 10:47:26', '2025-12-29 12:00:23', 0);
INSERT INTO `portal_guide` VALUES (2, '凤凰古城两日深度游', '/uploads/guide/guide2.jpg', '<p>感受湘西风情，探索凤凰古城的历史与文化...</p>', 1, '官方小编', NULL, 1, '4', '古城,两日游,人文', 0, 0, 0, 1, 1, 1, '2025-12-29 10:47:26', '2025-12-29 10:47:26', 0);

-- ----------------------------
-- Table structure for portal_order
-- ----------------------------
DROP TABLE IF EXISTS `portal_order`;
CREATE TABLE `portal_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `scenic_id` bigint NOT NULL COMMENT '景点ID',
  `scenic_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '景点名称',
  `ticket_id` bigint NOT NULL COMMENT '门票ID',
  `ticket_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '门票名称',
  `quantity` int NULL DEFAULT 1 COMMENT '数量',
  `unit_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '单价',
  `total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '总金额',
  `contact_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人电话',
  `contact_id_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人身份证',
  `visit_date` date NULL DEFAULT NULL COMMENT '游玩日期',
  `order_status` tinyint NULL DEFAULT 1 COMMENT '订单状态：0待支付 1已支付 2已使用 3已取消 4已退款',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `pay_type` tinyint NULL DEFAULT NULL COMMENT '支付方式：1微信 2支付宝',
  `qr_code` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电子票二维码',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_order_status`(`order_status` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of portal_order
-- ----------------------------

-- ----------------------------
-- Table structure for portal_scenic
-- ----------------------------
DROP TABLE IF EXISTS `portal_scenic`;
CREATE TABLE `portal_scenic`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `scenic_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '景点名称',
  `scenic_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '景点编码',
  `category_id` bigint NULL DEFAULT NULL COMMENT '分类ID',
  `cover_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面图片',
  `images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '图片集（JSON数组）',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '景点描述',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详细地址',
  `longitude` decimal(10, 7) NULL DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10, 7) NULL DEFAULT NULL COMMENT '纬度',
  `open_time` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开放时间',
  `tips` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '游玩贴士',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `view_count` int NULL DEFAULT 0 COMMENT '浏览量',
  `like_count` int NULL DEFAULT 0 COMMENT '点赞数',
  `rating` decimal(2, 1) NULL DEFAULT 5.0 COMMENT '评分',
  `is_hot` tinyint NULL DEFAULT 0 COMMENT '是否热门',
  `is_recommend` tinyint NULL DEFAULT 0 COMMENT '是否推荐',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_scenic_code`(`scenic_code` ASC) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE,
  INDEX `idx_is_hot`(`is_hot` ASC) USING BTREE,
  INDEX `idx_is_recommend`(`is_recommend` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '景点表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of portal_scenic
-- ----------------------------
INSERT INTO `portal_scenic` VALUES (1, '天门山国家森林公园', 'TIANMENSHAN', 4, '/api/files/scenic/门山国家森林公园.jpg', NULL, '天门山是张家界永定区海拔最高的山，因自然奇观天门洞而得名。天门洞是罕见的高海拔穿山溶洞，高131.5米，宽57米，深60米，终年吐云吞雾，充满神秘色彩。', '湖南省张家界市永定区官黎坪', NULL, NULL, '08:00-18:00', NULL, '0744-8369999', 0, 0, 4.8, 1, 1, 0, 1, '2025-12-29 10:47:26', '2025-12-29 12:57:30', 0);
INSERT INTO `portal_scenic` VALUES (2, '张家界国家森林公园', 'ZHANGJIAJIE_PARK', 4, '/api/files/scenic/森林公园大峡谷.jpg', NULL, '张家界国家森林公园是中国第一个国家森林公园，以独特的石英砂岩峰林地貌著称，集奇、险、秀、幽于一体。', '湖南省张家界市武陵源区', NULL, NULL, '07:00-18:00', NULL, '0744-5712189', 2, 0, 4.9, 1, 1, 0, 1, '2025-12-29 10:47:26', '2025-12-29 12:57:30', 0);
INSERT INTO `portal_scenic` VALUES (3, '玻璃桥', 'GLASS_BRIDGE', 3, '/api/files/scenic/玻璃桥.jpg', NULL, '张家界大峡谷玻璃桥是世界上最高、最长的玻璃桥，桥面长375米，宽6米，距谷底相对高度约300米。', '湖南省张家界市慈利县三官寺乡', NULL, NULL, '07:00-18:00', NULL, '0744-3559999', 0, 0, 4.7, 1, 1, 0, 1, '2025-12-29 10:47:26', '2025-12-29 12:57:30', 0);
INSERT INTO `portal_scenic` VALUES (4, '凤凰古城', 'FENGHUANG', 6, '/api/files/scenic/凤凰古城.jpg', NULL, '凤凰古城是国家历史文化名城，曾被新西兰作家路易·艾黎称赞为中国最美丽的小城。', '湖南省湘西土家族苗族自治州凤凰县', NULL, NULL, '全天开放', NULL, '0743-3502059', 0, 0, 4.6, 1, 1, 0, 1, '2025-12-29 10:47:26', '2025-12-29 12:57:30', 0);

-- ----------------------------
-- Table structure for portal_scenic_category
-- ----------------------------
DROP TABLE IF EXISTS `portal_scenic_category`;
CREATE TABLE `portal_scenic_category`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `category_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类编码',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父分类ID',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_category_code`(`category_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '景点分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of portal_scenic_category
-- ----------------------------
INSERT INTO `portal_scenic_category` VALUES (1, '自然风光', 'NATURE', 'Sunrise', 0, 1, 1, '2025-12-29 10:47:26', '2025-12-29 10:47:26', 0);
INSERT INTO `portal_scenic_category` VALUES (2, '人文景观', 'CULTURE', 'OfficeBuilding', 0, 2, 1, '2025-12-29 10:47:26', '2025-12-29 10:47:26', 0);
INSERT INTO `portal_scenic_category` VALUES (3, '网红打卡', 'HOT_SPOT', 'Camera', 0, 3, 1, '2025-12-29 10:47:26', '2025-12-29 10:47:26', 0);
INSERT INTO `portal_scenic_category` VALUES (4, '山峰峡谷', 'MOUNTAIN', 'Mountain', 1, 1, 1, '2025-12-29 10:47:26', '2025-12-29 10:47:26', 0);
INSERT INTO `portal_scenic_category` VALUES (5, '湖泊溪流', 'WATER', 'Ship', 1, 2, 1, '2025-12-29 10:47:26', '2025-12-29 10:47:26', 0);
INSERT INTO `portal_scenic_category` VALUES (6, '古镇村落', 'ANCIENT', 'House', 2, 1, 1, '2025-12-29 10:47:26', '2025-12-29 10:47:26', 0);
INSERT INTO `portal_scenic_category` VALUES (7, '寺庙道观', 'TEMPLE', 'Bell', 2, 2, 1, '2025-12-29 10:47:26', '2025-12-29 10:47:26', 0);

-- ----------------------------
-- Table structure for portal_ticket
-- ----------------------------
DROP TABLE IF EXISTS `portal_ticket`;
CREATE TABLE `portal_ticket`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `scenic_id` bigint NOT NULL COMMENT '景点ID',
  `ticket_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '门票名称',
  `ticket_type` tinyint NULL DEFAULT 1 COMMENT '门票类型：1成人票 2儿童票 3老年票 4学生票 5套票',
  `original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '原价',
  `selling_price` decimal(10, 2) NOT NULL COMMENT '售价',
  `stock` int NULL DEFAULT 9999 COMMENT '库存',
  `daily_limit` int NULL DEFAULT NULL COMMENT '每日限购',
  `valid_days` int NULL DEFAULT 1 COMMENT '有效天数',
  `use_rules` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '使用规则',
  `refund_rules` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '退款规则',
  `notice` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '购票须知',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_scenic_id`(`scenic_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '门票表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of portal_ticket
-- ----------------------------
INSERT INTO `portal_ticket` VALUES (1, 1, '天门山成人票', 1, 278.00, 258.00, 9999, NULL, 1, '凭身份证入园，当日有效', NULL, '请携带有效身份证件', 0, 1, '2025-12-29 10:47:26', '2025-12-29 10:47:26', 0);
INSERT INTO `portal_ticket` VALUES (2, 1, '天门山学生票', 4, 278.00, 158.00, 9999, NULL, 1, '凭学生证和身份证入园', NULL, '需出示有效学生证', 0, 1, '2025-12-29 10:47:26', '2025-12-29 10:47:26', 0);
INSERT INTO `portal_ticket` VALUES (3, 2, '张家界森林公园成人票', 1, 228.00, 218.00, 9999, NULL, 1, '四日内有效，可多次入园', NULL, '请携带有效身份证件', 0, 1, '2025-12-29 10:47:26', '2025-12-29 10:47:26', 0);
INSERT INTO `portal_ticket` VALUES (4, 2, '张家界森林公园学生票', 4, 228.00, 115.00, 9999, NULL, 1, '凭学生证入园', NULL, '需出示有效学生证', 0, 1, '2025-12-29 10:47:26', '2025-12-29 10:47:26', 0);
INSERT INTO `portal_ticket` VALUES (5, 3, '玻璃桥成人票', 1, 141.00, 128.00, 9999, NULL, 1, '当日有效，单次入园', NULL, '恐高者慎入', 0, 1, '2025-12-29 10:47:26', '2025-12-29 10:47:26', 0);
INSERT INTO `portal_ticket` VALUES (6, 4, '凤凰古城通票', 5, 148.00, 128.00, 9999, NULL, 1, '两日内有效', NULL, '包含九景点门票', 0, 1, '2025-12-29 10:47:26', '2025-12-29 10:47:26', 0);

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
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典数据ID',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典类型',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典标签（显示名称）',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典值',
  `dict_sort` int NULL DEFAULT 0 COMMENT '排序',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'CSS样式',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` tinyint NULL DEFAULT 0 COMMENT '是否默认：0-否，1-是',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 88 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 'sys_component', 'Dashboard - 仪表盘', 'Dashboard', 1, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (2, 'sys_component', 'Welcome - 欢迎页', 'Welcome', 2, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (3, 'sys_component', 'Users - 用户管理', 'Users', 10, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (4, 'sys_component', 'Roles - 角色管理', 'Roles', 11, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (5, 'sys_component', 'Permissions - 权限管理', 'Permissions', 12, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (6, 'sys_component', 'Menus - 菜单管理', 'Menus', 13, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (7, 'sys_component', 'Depts - 部门管理', 'Depts', 14, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (8, 'sys_component', 'Profile - 个人中心', 'Profile', 20, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (9, 'sys_component', 'Generator - 代码生成器', 'Generator', 30, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (10, 'sys_icon', 'House - 首页', 'House', 1, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (11, 'sys_icon', 'User - 用户', 'User', 2, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (12, 'sys_icon', 'UserFilled - 用户填充', 'UserFilled', 3, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (13, 'sys_icon', 'Avatar - 头像', 'Avatar', 4, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (14, 'sys_icon', 'Setting - 设置', 'Setting', 5, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (15, 'sys_icon', 'Menu - 菜单', 'Menu', 6, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (16, 'sys_icon', 'Key - 钥匙', 'Key', 7, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (17, 'sys_icon', 'Lock - 锁', 'Lock', 8, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (18, 'sys_icon', 'OfficeBuilding - 组织', 'OfficeBuilding', 9, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (19, 'sys_icon', 'Cpu - 处理器', 'Cpu', 10, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (20, 'sys_icon', 'Document - 文档', 'Document', 20, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (21, 'sys_icon', 'Files - 文件', 'Files', 21, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (22, 'sys_icon', 'Folder - 文件夹', 'Folder', 22, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (23, 'sys_icon', 'FolderOpened - 打开文件夹', 'FolderOpened', 23, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (24, 'sys_icon', 'DataLine - 数据', 'DataLine', 30, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (25, 'sys_icon', 'TrendCharts - 图表', 'TrendCharts', 31, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (26, 'sys_icon', 'PieChart - 饼图', 'PieChart', 32, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (27, 'sys_icon', 'Monitor - 监控', 'Monitor', 33, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (28, 'sys_icon', 'Tools - 工具', 'Tools', 40, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (29, 'sys_icon', 'Operation - 操作', 'Operation', 41, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (30, 'sys_icon', 'Grid - 网格', 'Grid', 42, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (31, 'sys_icon', 'List - 列表', 'List', 43, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (32, 'sys_icon', 'Search - 搜索', 'Search', 44, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (33, 'sys_icon', 'Edit - 编辑', 'Edit', 45, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (34, 'sys_icon', 'Delete - 删除', 'Delete', 46, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (35, 'sys_icon', 'Plus - 新增', 'Plus', 47, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (36, 'sys_icon', 'View - 查看', 'View', 48, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (37, 'sys_icon', 'Message - 消息', 'Message', 50, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (38, 'sys_icon', 'Bell - 通知', 'Bell', 51, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (39, 'sys_icon', 'ChatDotRound - 聊天', 'ChatDotRound', 52, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (40, 'sys_icon', 'Headset - 客服', 'Headset', 53, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (41, 'sys_icon', 'Calendar - 日历', 'Calendar', 60, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (42, 'sys_icon', 'Clock - 时钟', 'Clock', 61, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (43, 'sys_icon', 'Location - 位置', 'Location', 62, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (44, 'sys_icon', 'MapLocation - 地图', 'MapLocation', 63, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (45, 'sys_icon', 'Link - 链接', 'Link', 70, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (46, 'sys_icon', 'Share - 分享', 'Share', 71, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (47, 'sys_icon', 'Star - 星标', 'Star', 72, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (48, 'sys_icon', 'Warning - 警告', 'Warning', 73, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (49, 'sys_icon', 'InfoFilled - 信息', 'InfoFilled', 74, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (50, 'sys_icon', 'ShoppingCart - 购物车', 'ShoppingCart', 80, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (51, 'sys_icon', 'Goods - 商品', 'Goods', 81, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (52, 'sys_icon', 'Money - 金额', 'Money', 82, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (53, 'sys_icon', 'Ticket - 票务', 'Ticket', 83, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (54, 'sys_icon', 'Picture - 图片', 'Picture', 84, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (55, 'sys_icon', 'VideoCamera - 视频', 'VideoCamera', 85, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (56, 'sys_menu_type', '目录', 'M', 1, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (57, 'sys_menu_type', '菜单', 'C', 2, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (58, 'sys_menu_type', '按钮', 'F', 3, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (59, 'sys_data_scope', '全部数据', '1', 1, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (60, 'sys_data_scope', '本部门及下级', '2', 2, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (61, 'sys_data_scope', '本部门', '3', 3, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (62, 'sys_data_scope', '仅本人', '4', 4, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (63, 'sys_common_status', '启用', '1', 1, NULL, 'success', 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (64, 'sys_common_status', '禁用', '0', 2, NULL, 'danger', 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (65, 'sys_permission', '用户查看', 'user:view', 10, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (66, 'sys_permission', '用户新增', 'user:create', 11, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (67, 'sys_permission', '用户编辑', 'user:update', 12, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (68, 'sys_permission', '用户删除', 'user:delete', 13, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (69, 'sys_permission', '角色查看', 'role:view', 20, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (70, 'sys_permission', '角色新增', 'role:create', 21, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (71, 'sys_permission', '角色编辑', 'role:update', 22, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (72, 'sys_permission', '角色删除', 'role:delete', 23, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (73, 'sys_permission', '菜单查看', 'menu:view', 30, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (74, 'sys_permission', '菜单新增', 'menu:create', 31, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (75, 'sys_permission', '菜单编辑', 'menu:update', 32, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (76, 'sys_permission', '菜单删除', 'menu:delete', 33, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (77, 'sys_permission', '权限查看', 'permission:view', 40, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (78, 'sys_permission', '权限新增', 'permission:create', 41, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (79, 'sys_permission', '权限编辑', 'permission:update', 42, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (80, 'sys_permission', '权限删除', 'permission:delete', 43, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (81, 'sys_permission', '部门查看', 'dept:view', 50, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (82, 'sys_permission', '部门新增', 'dept:create', 51, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (83, 'sys_permission', '部门编辑', 'dept:update', 52, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (84, 'sys_permission', '部门删除', 'dept:delete', 53, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (85, 'sys_permission', '仪表盘查看', 'dashboard:view', 60, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (86, 'sys_permission', '生成器查看', 'generator:view', 70, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_data` VALUES (87, 'sys_permission', '生成器使用', 'generator:use', 71, NULL, NULL, 0, 1, NULL, '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典类型ID',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典类型（唯一标识）',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '系统组件', 'sys_component', 1, '前端页面组件列表', '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_type` VALUES (2, '系统图标', 'sys_icon', 1, '前端图标列表', '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_type` VALUES (3, '菜单类型', 'sys_menu_type', 1, '菜单类型：目录、菜单、按钮', '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_type` VALUES (4, '数据范围', 'sys_data_scope', 1, '数据权限范围', '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_type` VALUES (5, '通用状态', 'sys_common_status', 1, '通用状态：启用、禁用', '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);
INSERT INTO `sys_dict_type` VALUES (6, '权限标识', 'sys_permission', 1, '系统权限标识列表', '2025-12-09 14:53:11', '2025-12-09 14:53:11', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 63 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '登录日志表' ROW_FORMAT = DYNAMIC;

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
INSERT INTO `sys_login_log` VALUES (33, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 0, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_role_permission\' doesn\'t exist\r\n### The error may exist in com/zhangjiajie/system/mapper/UserMapper.java (best guess)\r\n### The error may involve com.zhangjiajie.system.mapper.UserMapper.selectUserPermissions-Inline\r\n### The error occurred while setting parameters\r\n### SQL: SELECT DISTINCT p.permission_code FROM sys_user u JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 JOIN sys_role_permission rp ON ur.role_id = rp.role_id AND rp.deleted = 0 JOIN sys_permission p ON rp.permission_id = p.id AND p.deleted = 0 WHERE u.deleted = 0 AND u.id = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_role_permission\' doesn\'t exist\n; bad SQL grammar []', '2025-12-19 11:27:46', '2025-12-19 11:27:46', 0, NULL);
INSERT INTO `sys_login_log` VALUES (34, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 0, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_role_permission\' doesn\'t exist\r\n### The error may exist in com/zhangjiajie/system/mapper/UserMapper.java (best guess)\r\n### The error may involve com.zhangjiajie.system.mapper.UserMapper.selectUserPermissions-Inline\r\n### The error occurred while setting parameters\r\n### SQL: SELECT DISTINCT p.permission_code FROM sys_user u JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 JOIN sys_role_permission rp ON ur.role_id = rp.role_id AND rp.deleted = 0 JOIN sys_permission p ON rp.permission_id = p.id AND p.deleted = 0 WHERE u.deleted = 0 AND u.id = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_role_permission\' doesn\'t exist\n; bad SQL grammar []', '2025-12-19 11:27:50', '2025-12-19 11:27:50', 0, NULL);
INSERT INTO `sys_login_log` VALUES (35, 'yushuang', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 0, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_role_permission\' doesn\'t exist\r\n### The error may exist in com/zhangjiajie/system/mapper/UserMapper.java (best guess)\r\n### The error may involve com.zhangjiajie.system.mapper.UserMapper.selectUserPermissions-Inline\r\n### The error occurred while setting parameters\r\n### SQL: SELECT DISTINCT p.permission_code FROM sys_user u JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 JOIN sys_role_permission rp ON ur.role_id = rp.role_id AND rp.deleted = 0 JOIN sys_permission p ON rp.permission_id = p.id AND p.deleted = 0 WHERE u.deleted = 0 AND u.id = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_role_permission\' doesn\'t exist\n; bad SQL grammar []', '2025-12-19 11:27:54', '2025-12-19 11:27:54', 0, NULL);
INSERT INTO `sys_login_log` VALUES (36, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 0, '用户名或密码错误', '2025-12-19 11:27:59', '2025-12-19 11:27:59', 0, NULL);
INSERT INTO `sys_login_log` VALUES (37, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 0, '用户名或密码错误', '2025-12-19 11:30:56', '2025-12-19 11:30:56', 0, NULL);
INSERT INTO `sys_login_log` VALUES (38, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 0, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_role_permission\' doesn\'t exist\r\n### The error may exist in com/zhangjiajie/system/mapper/UserMapper.java (best guess)\r\n### The error may involve com.zhangjiajie.system.mapper.UserMapper.selectUserPermissions-Inline\r\n### The error occurred while setting parameters\r\n### SQL: SELECT DISTINCT p.permission_code FROM sys_user u JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 JOIN sys_role_permission rp ON ur.role_id = rp.role_id AND rp.deleted = 0 JOIN sys_permission p ON rp.permission_id = p.id AND p.deleted = 0 WHERE u.deleted = 0 AND u.id = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_role_permission\' doesn\'t exist\n; bad SQL grammar []', '2025-12-19 11:31:00', '2025-12-19 11:31:00', 0, NULL);
INSERT INTO `sys_login_log` VALUES (39, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 0, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_role_permission\' doesn\'t exist\r\n### The error may exist in com/zhangjiajie/system/mapper/UserMapper.java (best guess)\r\n### The error may involve com.zhangjiajie.system.mapper.UserMapper.selectUserPermissions-Inline\r\n### The error occurred while setting parameters\r\n### SQL: SELECT DISTINCT p.permission_code FROM sys_user u JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 JOIN sys_role_permission rp ON ur.role_id = rp.role_id AND rp.deleted = 0 JOIN sys_permission p ON rp.permission_id = p.id AND p.deleted = 0 WHERE u.deleted = 0 AND u.id = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_role_permission\' doesn\'t exist\n; bad SQL grammar []', '2025-12-19 11:31:26', '2025-12-19 11:31:26', 0, NULL);
INSERT INTO `sys_login_log` VALUES (40, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 0, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_role_permission\' doesn\'t exist\r\n### The error may exist in com/zhangjiajie/system/mapper/UserMapper.java (best guess)\r\n### The error may involve com.zhangjiajie.system.mapper.UserMapper.selectUserPermissions-Inline\r\n### The error occurred while setting parameters\r\n### SQL: SELECT DISTINCT p.permission_code FROM sys_user u JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 JOIN sys_role_permission rp ON ur.role_id = rp.role_id AND rp.deleted = 0 JOIN sys_permission p ON rp.permission_id = p.id AND p.deleted = 0 WHERE u.deleted = 0 AND u.id = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_role_permission\' doesn\'t exist\n; bad SQL grammar []', '2025-12-19 11:31:43', '2025-12-19 11:31:43', 0, NULL);
INSERT INTO `sys_login_log` VALUES (41, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 0, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_role_permission\' doesn\'t exist\r\n### The error may exist in com/zhangjiajie/system/mapper/UserMapper.java (best guess)\r\n### The error may involve com.zhangjiajie.system.mapper.UserMapper.selectUserPermissions-Inline\r\n### The error occurred while setting parameters\r\n### SQL: SELECT DISTINCT p.permission_code FROM sys_user u JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 JOIN sys_role_permission rp ON ur.role_id = rp.role_id AND rp.deleted = 0 JOIN sys_permission p ON rp.permission_id = p.id AND p.deleted = 0 WHERE u.deleted = 0 AND u.id = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_role_permission\' doesn\'t exist\n; bad SQL grammar []', '2025-12-19 11:31:47', '2025-12-19 11:31:47', 0, NULL);
INSERT INTO `sys_login_log` VALUES (42, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 0, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_permission\' doesn\'t exist\r\n### The error may exist in com/zhangjiajie/system/mapper/UserMapper.java (best guess)\r\n### The error may involve com.zhangjiajie.system.mapper.UserMapper.selectUserPermissions-Inline\r\n### The error occurred while setting parameters\r\n### SQL: SELECT DISTINCT p.permission_code FROM sys_user u JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 JOIN sys_role_permission rp ON ur.role_id = rp.role_id AND rp.deleted = 0 JOIN sys_permission p ON rp.permission_id = p.id AND p.deleted = 0 WHERE u.deleted = 0 AND u.id = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_permission\' doesn\'t exist\n; bad SQL grammar []', '2025-12-29 09:29:44', '2025-12-29 09:29:44', 0, NULL);
INSERT INTO `sys_login_log` VALUES (43, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 0, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_permission\' doesn\'t exist\r\n### The error may exist in com/zhangjiajie/system/mapper/UserMapper.java (best guess)\r\n### The error may involve com.zhangjiajie.system.mapper.UserMapper.selectUserPermissions-Inline\r\n### The error occurred while setting parameters\r\n### SQL: SELECT DISTINCT p.permission_code FROM sys_user u JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 JOIN sys_role_permission rp ON ur.role_id = rp.role_id AND rp.deleted = 0 JOIN sys_permission p ON rp.permission_id = p.id AND p.deleted = 0 WHERE u.deleted = 0 AND u.id = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_permission\' doesn\'t exist\n; bad SQL grammar []', '2025-12-29 09:29:49', '2025-12-29 09:29:49', 0, NULL);
INSERT INTO `sys_login_log` VALUES (44, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 0, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_permission\' doesn\'t exist\r\n### The error may exist in com/zhangjiajie/system/mapper/UserMapper.java (best guess)\r\n### The error may involve com.zhangjiajie.system.mapper.UserMapper.selectUserPermissions-Inline\r\n### The error occurred while setting parameters\r\n### SQL: SELECT DISTINCT p.permission_code FROM sys_user u JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 JOIN sys_role_permission rp ON ur.role_id = rp.role_id AND rp.deleted = 0 JOIN sys_permission p ON rp.permission_id = p.id AND p.deleted = 0 WHERE u.deleted = 0 AND u.id = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_permission\' doesn\'t exist\n; bad SQL grammar []', '2025-12-29 09:30:04', '2025-12-29 09:30:04', 0, NULL);
INSERT INTO `sys_login_log` VALUES (45, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 0, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_permission\' doesn\'t exist\r\n### The error may exist in com/zhangjiajie/system/mapper/UserMapper.java (best guess)\r\n### The error may involve com.zhangjiajie.system.mapper.UserMapper.selectUserPermissions-Inline\r\n### The error occurred while setting parameters\r\n### SQL: SELECT DISTINCT p.permission_code FROM sys_user u JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 JOIN sys_role_permission rp ON ur.role_id = rp.role_id AND rp.deleted = 0 JOIN sys_permission p ON rp.permission_id = p.id AND p.deleted = 0 WHERE u.deleted = 0 AND u.id = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_permission\' doesn\'t exist\n; bad SQL grammar []', '2025-12-29 09:33:33', '2025-12-29 09:33:33', 0, NULL);
INSERT INTO `sys_login_log` VALUES (46, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Unknown Browser', 'Unknown OS', 'curl/8.16.0', 0, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_permission\' doesn\'t exist\r\n### The error may exist in com/zhangjiajie/system/mapper/UserMapper.java (best guess)\r\n### The error may involve com.zhangjiajie.system.mapper.UserMapper.selectUserPermissions-Inline\r\n### The error occurred while setting parameters\r\n### SQL: SELECT DISTINCT p.permission_code FROM sys_user u JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 JOIN sys_role_permission rp ON ur.role_id = rp.role_id AND rp.deleted = 0 JOIN sys_permission p ON rp.permission_id = p.id AND p.deleted = 0 WHERE u.deleted = 0 AND u.id = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_permission\' doesn\'t exist\n; bad SQL grammar []', '2025-12-29 09:36:12', '2025-12-29 09:36:12', 0, NULL);
INSERT INTO `sys_login_log` VALUES (47, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Unknown Browser', 'Unknown OS', 'curl/8.16.0', 0, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_permission\' doesn\'t exist\r\n### The error may exist in com/zhangjiajie/system/mapper/UserMapper.java (best guess)\r\n### The error may involve com.zhangjiajie.system.mapper.UserMapper.selectUserPermissions-Inline\r\n### The error occurred while setting parameters\r\n### SQL: SELECT DISTINCT p.permission_code FROM sys_user u JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 JOIN sys_role_permission rp ON ur.role_id = rp.role_id AND rp.deleted = 0 JOIN sys_permission p ON rp.permission_id = p.id AND p.deleted = 0 WHERE u.deleted = 0 AND u.id = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_permission\' doesn\'t exist\n; bad SQL grammar []', '2025-12-29 09:36:43', '2025-12-29 09:36:43', 0, NULL);
INSERT INTO `sys_login_log` VALUES (48, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Unknown Browser', 'Unknown OS', 'curl/8.16.0', 0, '用户名或密码错误', '2025-12-29 09:37:49', '2025-12-29 09:37:49', 0, NULL);
INSERT INTO `sys_login_log` VALUES (49, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Unknown Browser', 'Unknown OS', 'curl/8.16.0', 0, '用户名或密码错误', '2025-12-29 09:39:21', '2025-12-29 09:39:21', 0, NULL);
INSERT INTO `sys_login_log` VALUES (50, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 0, '用户名或密码错误', '2025-12-29 09:40:13', '2025-12-29 09:40:13', 0, NULL);
INSERT INTO `sys_login_log` VALUES (51, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 0, '用户名或密码错误', '2025-12-29 09:41:01', '2025-12-29 09:41:01', 0, NULL);
INSERT INTO `sys_login_log` VALUES (52, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Unknown Browser', 'Unknown OS', 'curl/8.16.0', 0, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_permission\' doesn\'t exist\r\n### The error may exist in com/zhangjiajie/system/mapper/UserMapper.java (best guess)\r\n### The error may involve com.zhangjiajie.system.mapper.UserMapper.selectUserPermissions-Inline\r\n### The error occurred while setting parameters\r\n### SQL: SELECT DISTINCT p.permission_code FROM sys_user u JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 JOIN sys_role_permission rp ON ur.role_id = rp.role_id AND rp.deleted = 0 JOIN sys_permission p ON rp.permission_id = p.id AND p.deleted = 0 WHERE u.deleted = 0 AND u.id = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_permission\' doesn\'t exist\n; bad SQL grammar []', '2025-12-29 09:41:02', '2025-12-29 09:41:02', 0, NULL);
INSERT INTO `sys_login_log` VALUES (53, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 0, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_permission\' doesn\'t exist\r\n### The error may exist in com/zhangjiajie/system/mapper/UserMapper.java (best guess)\r\n### The error may involve com.zhangjiajie.system.mapper.UserMapper.selectUserPermissions-Inline\r\n### The error occurred while setting parameters\r\n### SQL: SELECT DISTINCT p.permission_code FROM sys_user u JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 JOIN sys_role_permission rp ON ur.role_id = rp.role_id AND rp.deleted = 0 JOIN sys_permission p ON rp.permission_id = p.id AND p.deleted = 0 WHERE u.deleted = 0 AND u.id = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_permission\' doesn\'t exist\n; bad SQL grammar []', '2025-12-29 09:41:10', '2025-12-29 09:41:10', 0, NULL);
INSERT INTO `sys_login_log` VALUES (54, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Unknown Browser', 'Unknown OS', 'curl/8.16.0', 0, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_permission\' doesn\'t exist\r\n### The error may exist in com/zhangjiajie/system/mapper/UserMapper.java (best guess)\r\n### The error may involve com.zhangjiajie.system.mapper.UserMapper.selectUserPermissions-Inline\r\n### The error occurred while setting parameters\r\n### SQL: SELECT DISTINCT p.permission_code FROM sys_user u JOIN sys_user_role ur ON u.id = ur.user_id AND ur.deleted = 0 JOIN sys_role_permission rp ON ur.role_id = rp.role_id AND rp.deleted = 0 JOIN sys_permission p ON rp.permission_id = p.id AND p.deleted = 0 WHERE u.deleted = 0 AND u.id = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Table \'java_web.sys_permission\' doesn\'t exist\n; bad SQL grammar []', '2025-12-29 09:41:20', '2025-12-29 09:41:20', 0, NULL);
INSERT INTO `sys_login_log` VALUES (55, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Unknown Browser', 'Unknown OS', 'curl/8.16.0', 1, NULL, '2025-12-29 09:42:42', '2025-12-29 09:42:42', 0, NULL);
INSERT INTO `sys_login_log` VALUES (56, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 09:43:18', '2025-12-29 09:43:18', 0, NULL);
INSERT INTO `sys_login_log` VALUES (57, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 11:38:45', '2025-12-29 11:38:45', 0, NULL);
INSERT INTO `sys_login_log` VALUES (58, '16721555396', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 0, '用户不存在', '2025-12-29 11:40:12', '2025-12-29 11:40:12', 0, NULL);
INSERT INTO `sys_login_log` VALUES (59, ' 123', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 0, '用户不存在', '2025-12-29 11:40:16', '2025-12-29 11:40:16', 0, NULL);
INSERT INTO `sys_login_log` VALUES (60, ' 123', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 0, '用户不存在', '2025-12-29 11:40:35', '2025-12-29 11:40:35', 0, NULL);
INSERT INTO `sys_login_log` VALUES (61, '123', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 11:59:34', '2025-12-29 11:59:34', 0, NULL);
INSERT INTO `sys_login_log` VALUES (62, 'admin', '0:0:0:0:0:0:0:1', 'Unknown Location', 'Google Chrome', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 12:53:57', '2025-12-29 12:53:57', 0, NULL);

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
INSERT INTO `sys_menu` VALUES (4, '门户管理', 0, 4, '/portal', NULL, NULL, 1, 0, 'M', 1, 1, NULL, 'Promotion', '2025-12-29 11:47:35', '2025-12-29 11:47:35', 0, '用户端内容管理');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 3, 1, '/users', 'Users', NULL, 1, 0, 'C', 1, 1, 'user:view', 'UserFilled', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 3, 2, '/roles', 'Roles', NULL, 1, 0, 'C', 1, 1, 'role:view', 'Avatar', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 3, 3, '/menus', 'Menus', NULL, 1, 0, 'C', 1, 1, 'menu:view', 'Menu', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (103, '权限管理', 3, 4, '/permissions', 'Permissions', NULL, 1, 0, 'C', 1, 1, 'permission:view', 'Key', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (104, '代码生成器', 3, 5, '/generator', 'Generator', NULL, 1, 0, 'C', 1, 1, 'generator:view', 'Cpu', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (105, '组织结构', 3, 6, '/depts', 'Depts', NULL, 1, 0, 'C', 1, 1, 'dept:view', 'OfficeBuilding', '2025-12-07 14:31:39', '2025-12-07 14:31:39', 0, '');
INSERT INTO `sys_menu` VALUES (401, '景点管理', 4, 1, '/portal/scenic', 'ScenicManage', NULL, 1, 0, 'C', 1, 1, 'portal:scenic:view', 'Location', '2025-12-29 11:47:35', '2025-12-29 11:47:35', 0, '管理旅游景点');
INSERT INTO `sys_menu` VALUES (402, '门票管理', 4, 2, '/portal/ticket', 'TicketManage', NULL, 1, 0, 'C', 1, 1, 'portal:ticket:view', 'Ticket', '2025-12-29 11:47:35', '2025-12-29 11:47:35', 0, '管理景点门票');
INSERT INTO `sys_menu` VALUES (403, '攻略管理', 4, 3, '/portal/guide', 'GuideManage', NULL, 1, 0, 'C', 1, 1, 'portal:guide:view', 'Document', '2025-12-29 11:47:35', '2025-12-29 11:47:35', 0, '管理旅游攻略');
INSERT INTO `sys_menu` VALUES (404, '订单管理', 4, 4, '/portal/order', 'OrderManage', NULL, 1, 0, 'C', 1, 1, 'portal:order:view', 'ShoppingCart', '2025-12-29 11:47:35', '2025-12-29 11:47:35', 0, '管理用户订单');
INSERT INTO `sys_menu` VALUES (405, '轮播图管理', 4, 5, '/portal/banner', 'BannerManage', NULL, 1, 0, 'C', 1, 1, 'portal:banner:view', 'Picture', '2025-12-29 11:47:35', '2025-12-29 11:47:35', 0, '管理首页轮播图');
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
) ENGINE = InnoDB AUTO_INCREMENT = 97 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '操作日志表' ROW_FORMAT = DYNAMIC;

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
INSERT INTO `sys_operation_log` VALUES (54, NULL, 'admin', '获取用户信息', '认证管理', 'GET', '/api/auth/userinfo', 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 3, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-09 14:35:15', '2025-12-09 14:35:15', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (55, NULL, 'admin', '获取用户信息', '认证管理', 'GET', '/api/auth/userinfo', 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 8, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-09 15:07:03', '2025-12-09 15:07:03', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (56, NULL, 'admin', '获取用户信息', '认证管理', 'GET', '/api/auth/userinfo', 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 2, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36', 1, NULL, '2025-12-09 15:07:13', '2025-12-09 15:07:13', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (57, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 553, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-19 11:27:46', '2025-12-19 11:27:46', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (58, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 79, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-19 11:27:50', '2025-12-19 11:27:50', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (59, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 77, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-19 11:27:54', '2025-12-19 11:27:54', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (60, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 69, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-19 11:27:59', '2025-12-19 11:27:59', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (61, NULL, 'admin', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 274, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-19 11:30:56', '2025-12-19 11:30:56', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (62, NULL, 'admin', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 181, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-19 11:31:00', '2025-12-19 11:31:00', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (63, NULL, 'admin', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 75, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-19 11:31:26', '2025-12-19 11:31:26', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (64, NULL, 'admin', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 79, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-19 11:31:43', '2025-12-19 11:31:43', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (65, NULL, 'admin', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 70, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-19 11:31:47', '2025-12-19 11:31:47', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (66, NULL, 'admin', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 425, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 09:29:44', '2025-12-29 09:29:44', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (67, NULL, 'admin', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 69, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 09:29:49', '2025-12-29 09:29:49', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (68, NULL, 'admin', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 70, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 09:30:04', '2025-12-29 09:30:04', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (69, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 395, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 09:33:33', '2025-12-29 09:33:33', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (70, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 60, '0:0:0:0:0:0:0:1', 'curl/8.16.0', 1, NULL, '2025-12-29 09:36:12', '2025-12-29 09:36:12', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (71, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 62, '0:0:0:0:0:0:0:1', 'curl/8.16.0', 1, NULL, '2025-12-29 09:36:43', '2025-12-29 09:36:43', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (72, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 55, '0:0:0:0:0:0:0:1', 'curl/8.16.0', 1, NULL, '2025-12-29 09:37:49', '2025-12-29 09:37:49', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (73, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 55, '0:0:0:0:0:0:0:1', 'curl/8.16.0', 1, NULL, '2025-12-29 09:39:21', '2025-12-29 09:39:21', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (74, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 58, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 09:40:13', '2025-12-29 09:40:13', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (75, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 56, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 09:41:01', '2025-12-29 09:41:01', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (76, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 62, '0:0:0:0:0:0:0:1', 'curl/8.16.0', 1, NULL, '2025-12-29 09:41:02', '2025-12-29 09:41:02', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (77, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 61, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 09:41:10', '2025-12-29 09:41:10', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (78, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 61, '0:0:0:0:0:0:0:1', 'curl/8.16.0', 1, NULL, '2025-12-29 09:41:20', '2025-12-29 09:41:20', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (79, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 116, '0:0:0:0:0:0:0:1', 'curl/8.16.0', 1, NULL, '2025-12-29 09:42:42', '2025-12-29 09:42:42', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (80, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 63, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 09:43:18', '2025-12-29 09:43:18', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (81, NULL, 'admin', '获取用户信息', '认证管理', 'GET', '/api/auth/userinfo', 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 3, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 09:43:18', '2025-12-29 09:43:18', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (82, NULL, 'admin', '获取用户信息', '认证管理', 'GET', '/api/auth/userinfo', 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 11, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 11:27:31', '2025-12-29 11:27:31', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (83, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 134, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 11:38:45', '2025-12-29 11:38:45', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (84, NULL, 'admin', '获取用户信息', '认证管理', 'GET', '/api/auth/userinfo', 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 3, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 11:38:45', '2025-12-29 11:38:45', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (85, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 3, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 11:40:12', '2025-12-29 11:40:12', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (86, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 3, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 11:40:16', '2025-12-29 11:40:16', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (87, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 3, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 11:40:35', '2025-12-29 11:40:35', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (88, NULL, 'admin', '获取用户信息', '认证管理', 'GET', '/api/auth/userinfo', 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 2, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 11:48:05', '2025-12-29 11:48:05', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (89, NULL, 'admin', '获取用户信息', '认证管理', 'GET', '/api/auth/userinfo', 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 2, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 11:49:35', '2025-12-29 11:49:35', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (90, NULL, 'admin', '获取用户信息', '认证管理', 'GET', '/api/auth/userinfo', 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 3, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 11:50:01', '2025-12-29 11:50:01', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (91, NULL, 'admin', '获取用户信息', '认证管理', 'GET', '/api/auth/userinfo', 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 3, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 11:50:12', '2025-12-29 11:50:12', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (92, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 67, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 11:59:34', '2025-12-29 11:59:34', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (93, NULL, 'admin', '获取用户信息', '认证管理', 'GET', '/api/auth/userinfo', 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 6, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 12:53:50', '2025-12-29 12:53:50', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (94, NULL, 'anonymousUser', '用户登录', '认证管理', 'POST', '/api/auth/login', 'com.zhangjiajie.system.controller.AuthController.login', NULL, 116, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 12:53:57', '2025-12-29 12:53:57', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (95, NULL, 'admin', '获取用户信息', '认证管理', 'GET', '/api/auth/userinfo', 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 4, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 12:53:57', '2025-12-29 12:53:57', 0, NULL);
INSERT INTO `sys_operation_log` VALUES (96, NULL, 'admin', '获取用户信息', '认证管理', 'GET', '/api/auth/userinfo', 'com.zhangjiajie.system.controller.AuthController.getUserInfo', '', 9, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', 1, NULL, '2025-12-29 12:57:09', '2025-12-29 12:57:09', 0, NULL);

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '鏉冮檺鍚嶇О',
  `permission_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '鏉冮檺缂栫爜',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '鐖舵潈闄怚D',
  `type` tinyint NULL DEFAULT 1 COMMENT '绫诲瀷锛?鑿滃崟 2鎸夐挳',
  `sort` int NULL DEFAULT 0 COMMENT '鎺掑簭',
  `status` tinyint NULL DEFAULT 1 COMMENT '鐘舵?锛?绂佺敤 1鍚?敤',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '鍒犻櫎鏍囪?锛?鏈?垹闄?1宸插垹闄',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_permission_code`(`permission_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '鏉冮檺琛' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1, '系统管理', 'system', 0, 1, 1, 1, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');
INSERT INTO `sys_permission` VALUES (2, '用户管理', 'system:user', 1, 1, 1, 1, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');
INSERT INTO `sys_permission` VALUES (3, '用户查询', 'system:user:list', 2, 2, 1, 1, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');
INSERT INTO `sys_permission` VALUES (4, '用户新增', 'system:user:add', 2, 2, 2, 1, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');
INSERT INTO `sys_permission` VALUES (5, '用户编辑', 'system:user:edit', 2, 2, 3, 1, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');
INSERT INTO `sys_permission` VALUES (6, '用户删除', 'system:user:delete', 2, 2, 4, 1, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');
INSERT INTO `sys_permission` VALUES (7, '角色管理', 'system:role', 1, 1, 2, 1, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');
INSERT INTO `sys_permission` VALUES (8, '角色查询', 'system:role:list', 7, 2, 1, 1, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');
INSERT INTO `sys_permission` VALUES (9, '角色新增', 'system:role:add', 7, 2, 2, 1, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');
INSERT INTO `sys_permission` VALUES (10, '角色编辑', 'system:role:edit', 7, 2, 3, 1, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');
INSERT INTO `sys_permission` VALUES (11, '角色删除', 'system:role:delete', 7, 2, 4, 1, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');

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
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1, 1, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (2, 1, 2, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (3, 1, 3, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (4, 1, 100, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (5, 1, 101, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (6, 1, 102, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (7, 1, 103, '2025-12-07 14:31:39');
INSERT INTO `sys_role_menu` VALUES (8, 1, 104, '2025-12-07 14:31:39');
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
INSERT INTO `sys_role_menu` VALUES (37, 1, 4, '2025-12-29 11:47:35');
INSERT INTO `sys_role_menu` VALUES (38, 1, 401, '2025-12-29 11:47:35');
INSERT INTO `sys_role_menu` VALUES (39, 1, 402, '2025-12-29 11:47:35');
INSERT INTO `sys_role_menu` VALUES (40, 1, 403, '2025-12-29 11:47:35');
INSERT INTO `sys_role_menu` VALUES (41, 1, 404, '2025-12-29 11:47:35');
INSERT INTO `sys_role_menu` VALUES (42, 1, 405, '2025-12-29 11:47:35');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL,
  `permission_id` bigint NOT NULL,
  `deleted` int NULL DEFAULT 0,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_perm`(`role_id` ASC, `permission_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (1, 1, 1, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');
INSERT INTO `sys_role_permission` VALUES (2, 1, 2, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');
INSERT INTO `sys_role_permission` VALUES (3, 1, 3, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');
INSERT INTO `sys_role_permission` VALUES (4, 1, 4, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');
INSERT INTO `sys_role_permission` VALUES (5, 1, 5, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');
INSERT INTO `sys_role_permission` VALUES (6, 1, 6, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');
INSERT INTO `sys_role_permission` VALUES (7, 1, 7, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');
INSERT INTO `sys_role_permission` VALUES (8, 1, 8, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');
INSERT INTO `sys_role_permission` VALUES (9, 1, 9, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');
INSERT INTO `sys_role_permission` VALUES (10, 1, 10, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');
INSERT INTO `sys_role_permission` VALUES (11, 1, 11, 0, '2025-12-29 09:42:34', '2025-12-29 09:42:34');

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
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$59T3wuoIq4kdzIdyW08v6ehaeKxRW6DV8Uq5POhXWfdahSczQ6Swi', NULL, '系统管理员', '超级管理员', 'admin@example.com', '13800138000', NULL, 1, 0, NULL, 1, NULL, NULL, '2025-12-05 17:59:19', '2025-12-29 09:41:20', 0, NULL);
INSERT INTO `sys_user` VALUES (6, 'yushuang', '$2a$10$QkJUrmOjNl1bCtDSMDonYuFfk/Gb7NxSsswy2RSJp04xZ4GJQ1Y4.', NULL, NULL, NULL, '18888888@qq.com', NULL, NULL, NULL, 0, NULL, 1, NULL, NULL, '2025-12-05 20:50:23', '2025-12-08 20:38:00', 0, NULL);
INSERT INTO `sys_user` VALUES (7, '123', '$2a$10$dNwwKqL9DsUk9f0umtIn1uO280W1jUhrdXVnDd1NXah3JR.0yUwry', NULL, NULL, NULL, '1524901804@qq.com', '16721555396', NULL, 7, 0, NULL, 1, NULL, NULL, '2025-12-29 11:39:16', '2025-12-29 11:40:32', 0, NULL);

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
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1, '2025-12-05 17:59:19', '2025-12-05 17:59:19', 0);
INSERT INTO `sys_user_role` VALUES (2, 4, 3, NULL, '2025-12-05 19:35:53', 0);
INSERT INTO `sys_user_role` VALUES (3, 5, 3, NULL, '2025-12-05 19:38:34', 0);
INSERT INTO `sys_user_role` VALUES (4, 6, 3, NULL, '2025-12-05 20:14:00', 0);
INSERT INTO `sys_user_role` VALUES (5, 7, 3, '2025-12-29 11:39:16', '2025-12-29 11:39:16', 0);

SET FOREIGN_KEY_CHECKS = 1;
