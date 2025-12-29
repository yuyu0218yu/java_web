-- =====================================================
-- 张家界旅游用户端数据库表
-- =====================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 景点分类表
-- ----------------------------
DROP TABLE IF EXISTS `portal_scenic_category`;
CREATE TABLE `portal_scenic_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_name` varchar(50) NOT NULL COMMENT '分类名称',
  `category_code` varchar(50) DEFAULT NULL COMMENT '分类编码',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `parent_id` bigint DEFAULT 0 COMMENT '父分类ID',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `status` tinyint DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_category_code` (`category_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景点分类表';

-- ----------------------------
-- 2. 景点表
-- ----------------------------
DROP TABLE IF EXISTS `portal_scenic`;
CREATE TABLE `portal_scenic` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `scenic_name` varchar(100) NOT NULL COMMENT '景点名称',
  `scenic_code` varchar(50) DEFAULT NULL COMMENT '景点编码',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `cover_image` varchar(500) DEFAULT NULL COMMENT '封面图片',
  `images` text COMMENT '图片集（JSON数组）',
  `description` text COMMENT '景点描述',
  `address` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `longitude` decimal(10,7) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,7) DEFAULT NULL COMMENT '纬度',
  `open_time` varchar(100) DEFAULT NULL COMMENT '开放时间',
  `tips` text COMMENT '游玩贴士',
  `phone` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `view_count` int DEFAULT 0 COMMENT '浏览量',
  `like_count` int DEFAULT 0 COMMENT '点赞数',
  `rating` decimal(2,1) DEFAULT 5.0 COMMENT '评分',
  `is_hot` tinyint DEFAULT 0 COMMENT '是否热门',
  `is_recommend` tinyint DEFAULT 0 COMMENT '是否推荐',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `status` tinyint DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_scenic_code` (`scenic_code`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_is_hot` (`is_hot`),
  KEY `idx_is_recommend` (`is_recommend`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景点表';

-- ----------------------------
-- 3. 门票表
-- ----------------------------
DROP TABLE IF EXISTS `portal_ticket`;
CREATE TABLE `portal_ticket` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `scenic_id` bigint NOT NULL COMMENT '景点ID',
  `ticket_name` varchar(100) NOT NULL COMMENT '门票名称',
  `ticket_type` tinyint DEFAULT 1 COMMENT '门票类型：1成人票 2儿童票 3老年票 4学生票 5套票',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '原价',
  `selling_price` decimal(10,2) NOT NULL COMMENT '售价',
  `stock` int DEFAULT 9999 COMMENT '库存',
  `daily_limit` int DEFAULT NULL COMMENT '每日限购',
  `valid_days` int DEFAULT 1 COMMENT '有效天数',
  `use_rules` text COMMENT '使用规则',
  `refund_rules` text COMMENT '退款规则',
  `notice` text COMMENT '购票须知',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `status` tinyint DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_scenic_id` (`scenic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='门票表';

-- ----------------------------
-- 4. 订单表
-- ----------------------------
DROP TABLE IF EXISTS `portal_order`;
CREATE TABLE `portal_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(32) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `scenic_id` bigint NOT NULL COMMENT '景点ID',
  `scenic_name` varchar(100) DEFAULT NULL COMMENT '景点名称',
  `ticket_id` bigint NOT NULL COMMENT '门票ID',
  `ticket_name` varchar(100) DEFAULT NULL COMMENT '门票名称',
  `quantity` int DEFAULT 1 COMMENT '数量',
  `unit_price` decimal(10,2) DEFAULT NULL COMMENT '单价',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '总金额',
  `contact_name` varchar(50) DEFAULT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系人电话',
  `contact_id_card` varchar(20) DEFAULT NULL COMMENT '联系人身份证',
  `visit_date` date DEFAULT NULL COMMENT '游玩日期',
  `order_status` tinyint DEFAULT 1 COMMENT '订单状态：0待支付 1已支付 2已使用 3已取消 4已退款',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `pay_type` tinyint DEFAULT NULL COMMENT '支付方式：1微信 2支付宝',
  `qr_code` varchar(500) DEFAULT NULL COMMENT '电子票二维码',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ----------------------------
-- 5. 攻略表
-- ----------------------------
DROP TABLE IF EXISTS `portal_guide`;
CREATE TABLE `portal_guide` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL COMMENT '标题',
  `cover_image` varchar(500) DEFAULT NULL COMMENT '封面图',
  `content` text COMMENT '内容（富文本）',
  `author_id` bigint DEFAULT NULL COMMENT '作者ID',
  `author_name` varchar(50) DEFAULT NULL COMMENT '作者名称',
  `author_avatar` varchar(255) DEFAULT NULL COMMENT '作者头像',
  `guide_type` tinyint DEFAULT 1 COMMENT '类型：1攻略 2游记',
  `scenic_ids` varchar(500) DEFAULT NULL COMMENT '关联景点ID（逗号分隔）',
  `tags` varchar(255) DEFAULT NULL COMMENT '标签（逗号分隔）',
  `view_count` int DEFAULT 0 COMMENT '浏览量',
  `like_count` int DEFAULT 0 COMMENT '点赞数',
  `comment_count` int DEFAULT 0 COMMENT '评论数',
  `is_official` tinyint DEFAULT 0 COMMENT '是否官方',
  `is_recommend` tinyint DEFAULT 0 COMMENT '是否推荐',
  `status` tinyint DEFAULT 1 COMMENT '状态：0待审核 1已发布 2已下架',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_author_id` (`author_id`),
  KEY `idx_guide_type` (`guide_type`),
  KEY `idx_is_recommend` (`is_recommend`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='攻略表';

-- ----------------------------
-- 6. 评论表
-- ----------------------------
DROP TABLE IF EXISTS `portal_comment`;
CREATE TABLE `portal_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `target_type` tinyint NOT NULL COMMENT '目标类型：1景点 2攻略',
  `target_id` bigint NOT NULL COMMENT '目标ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `user_avatar` varchar(255) DEFAULT NULL COMMENT '用户头像',
  `content` text COMMENT '评论内容',
  `images` varchar(1000) DEFAULT NULL COMMENT '图片（JSON数组）',
  `rating` tinyint DEFAULT NULL COMMENT '评分1-5',
  `parent_id` bigint DEFAULT 0 COMMENT '父评论ID',
  `like_count` int DEFAULT 0 COMMENT '点赞数',
  `status` tinyint DEFAULT 1 COMMENT '状态：0待审核 1已发布',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_target` (`target_type`, `target_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- ----------------------------
-- 7. 收藏表
-- ----------------------------
DROP TABLE IF EXISTS `portal_favorite`;
CREATE TABLE `portal_favorite` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `target_type` tinyint NOT NULL COMMENT '目标类型：1景点 2攻略',
  `target_id` bigint NOT NULL COMMENT '目标ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_target` (`user_id`, `target_type`, `target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- ----------------------------
-- 8. 轮播图表
-- ----------------------------
DROP TABLE IF EXISTS `portal_banner`;
CREATE TABLE `portal_banner` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `image_url` varchar(500) NOT NULL COMMENT '图片URL',
  `link_url` varchar(500) DEFAULT NULL COMMENT '跳转链接',
  `link_type` tinyint DEFAULT 1 COMMENT '链接类型：1景点 2攻略 3外链',
  `target_id` bigint DEFAULT NULL COMMENT '目标ID',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `status` tinyint DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮播图表';

-- =====================================================
-- 初始化数据
-- =====================================================

-- 景点分类初始数据
INSERT INTO `portal_scenic_category` (`id`, `category_name`, `category_code`, `icon`, `parent_id`, `sort_order`) VALUES
(1, '自然风光', 'NATURE', 'Sunrise', 0, 1),
(2, '人文景观', 'CULTURE', 'OfficeBuilding', 0, 2),
(3, '网红打卡', 'HOT_SPOT', 'Camera', 0, 3),
(4, '山峰峡谷', 'MOUNTAIN', 'Mountain', 1, 1),
(5, '湖泊溪流', 'WATER', 'Ship', 1, 2),
(6, '古镇村落', 'ANCIENT', 'House', 2, 1),
(7, '寺庙道观', 'TEMPLE', 'Bell', 2, 2);

-- 示例景点数据
INSERT INTO `portal_scenic` (`scenic_name`, `scenic_code`, `category_id`, `cover_image`, `description`, `address`, `open_time`, `phone`, `rating`, `is_hot`, `is_recommend`) VALUES
('天门山国家森林公园', 'TIANMENSHAN', 4, '/uploads/scenic/tianmenshan.jpg', '天门山是张家界永定区海拔最高的山，因自然奇观天门洞而得名。天门洞是罕见的高海拔穿山溶洞，高131.5米，宽57米，深60米，终年吐云吞雾，充满神秘色彩。', '湖南省张家界市永定区官黎坪', '08:00-18:00', '0744-8369999', 4.8, 1, 1),
('张家界国家森林公园', 'ZHANGJIAJIE_PARK', 4, '/uploads/scenic/zhangjiajie.jpg', '张家界国家森林公园是中国第一个国家森林公园，以独特的石英砂岩峰林地貌著称，集奇、险、秀、幽于一体。', '湖南省张家界市武陵源区', '07:00-18:00', '0744-5712189', 4.9, 1, 1),
('玻璃桥', 'GLASS_BRIDGE', 3, '/uploads/scenic/glass_bridge.jpg', '张家界大峡谷玻璃桥是世界上最高、最长的玻璃桥，桥面长375米，宽6米，距谷底相对高度约300米。', '湖南省张家界市慈利县三官寺乡', '07:00-18:00', '0744-3559999', 4.7, 1, 1),
('凤凰古城', 'FENGHUANG', 6, '/uploads/scenic/fenghuang.jpg', '凤凰古城是国家历史文化名城，曾被新西兰作家路易·艾黎称赞为中国最美丽的小城。', '湖南省湘西土家族苗族自治州凤凰县', '全天开放', '0743-3502059', 4.6, 1, 1);

-- 示例门票数据
INSERT INTO `portal_ticket` (`scenic_id`, `ticket_name`, `ticket_type`, `original_price`, `selling_price`, `use_rules`, `notice`) VALUES
(1, '天门山成人票', 1, 278.00, 258.00, '凭身份证入园，当日有效', '请携带有效身份证件'),
(1, '天门山学生票', 4, 278.00, 158.00, '凭学生证和身份证入园', '需出示有效学生证'),
(2, '张家界森林公园成人票', 1, 228.00, 218.00, '四日内有效，可多次入园', '请携带有效身份证件'),
(2, '张家界森林公园学生票', 4, 228.00, 115.00, '凭学生证入园', '需出示有效学生证'),
(3, '玻璃桥成人票', 1, 141.00, 128.00, '当日有效，单次入园', '恐高者慎入'),
(4, '凤凰古城通票', 5, 148.00, 128.00, '两日内有效', '包含九景点门票');

-- 示例攻略数据
INSERT INTO `portal_guide` (`title`, `cover_image`, `content`, `author_id`, `author_name`, `guide_type`, `scenic_ids`, `tags`, `is_official`, `is_recommend`) VALUES
('张家界三日游完美攻略', '/uploads/guide/guide1.jpg', '<p>张家界三日游最佳路线规划，带你玩转天门山、森林公园、玻璃桥...</p>', 1, '官方小编', 1, '1,2,3', '三日游,经典路线,必看', 1, 1),
('凤凰古城两日深度游', '/uploads/guide/guide2.jpg', '<p>感受湘西风情，探索凤凰古城的历史与文化...</p>', 1, '官方小编', 1, '4', '古城,两日游,人文', 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
