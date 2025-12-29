-- 添加用户端管理菜单
-- 执行此 SQL 为管理后台添加门户内容管理功能

-- 1. 添加门户管理父菜单
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_time`, `update_time`, `deleted`, `remark`) VALUES
(4, '门户管理', 0, 4, '/portal', NULL, NULL, 1, 0, 'M', 1, 1, NULL, 'Promotion', NOW(), NOW(), 0, '用户端内容管理');

-- 2. 添加子菜单
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_time`, `update_time`, `deleted`, `remark`) VALUES
(401, '景点管理', 4, 1, '/portal/scenic', 'ScenicManage', NULL, 1, 0, 'C', 1, 1, 'portal:scenic:view', 'Location', NOW(), NOW(), 0, '管理旅游景点'),
(402, '门票管理', 4, 2, '/portal/ticket', 'TicketManage', NULL, 1, 0, 'C', 1, 1, 'portal:ticket:view', 'Ticket', NOW(), NOW(), 0, '管理景点门票'),
(403, '攻略管理', 4, 3, '/portal/guide', 'GuideManage', NULL, 1, 0, 'C', 1, 1, 'portal:guide:view', 'Document', NOW(), NOW(), 0, '管理旅游攻略'),
(404, '订单管理', 4, 4, '/portal/order', 'OrderManage', NULL, 1, 0, 'C', 1, 1, 'portal:order:view', 'ShoppingCart', NOW(), NOW(), 0, '管理用户订单'),
(405, '轮播图管理', 4, 5, '/portal/banner', 'BannerManage', NULL, 1, 0, 'C', 1, 1, 'portal:banner:view', 'Picture', NOW(), NOW(), 0, '管理首页轮播图');

-- 3. 给超级管理员角色分配新菜单权限（假设角色ID=1是超级管理员）
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `create_time`) VALUES
(1, 4, NOW()),
(1, 401, NOW()),
(1, 402, NOW()),
(1, 403, NOW()),
(1, 404, NOW()),
(1, 405, NOW());

-- 注意：如果 ID 冲突，请先查询现有最大 ID：
-- SELECT MAX(id) FROM sys_menu;
-- 然后修改上面的 ID 值
