USE java_web;

-- 操作日志表
CREATE TABLE IF NOT EXISTS sys_operation_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT COMMENT '操作用户ID',
    username VARCHAR(50) COMMENT '操作用户名',
    operation VARCHAR(100) COMMENT '操作类型',
    method VARCHAR(200) COMMENT '请求方法',
    params TEXT COMMENT '请求参数',
    time BIGINT COMMENT '执行时长(毫秒)',
    ip VARCHAR(50) COMMENT '操作IP',
    user_agent TEXT COMMENT '用户代理',
    status TINYINT COMMENT '操作状态：0-失败，1-成功',
    error_msg TEXT COMMENT '错误信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    remark TEXT COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- 登录日志表
CREATE TABLE IF NOT EXISTS sys_login_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    username VARCHAR(50) COMMENT '登录用户名',
    ip VARCHAR(50) COMMENT '登录IP',
    location VARCHAR(255) COMMENT '登录地点',
    browser VARCHAR(50) COMMENT '浏览器',
    os VARCHAR(50) COMMENT '操作系统',
    user_agent TEXT COMMENT '用户代理',
    status TINYINT COMMENT '登录状态：0-失败，1-成功',
    error_msg VARCHAR(255) COMMENT '错误信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    remark TEXT COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录日志表';

-- 文件信息表
CREATE TABLE IF NOT EXISTS sys_file_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    file_name VARCHAR(255) NOT NULL COMMENT '文件名',
    original_name VARCHAR(255) NOT NULL COMMENT '原始文件名',
    file_path VARCHAR(500) NOT NULL COMMENT '文件路径',
    file_size BIGINT COMMENT '文件大小(字节)',
    file_type VARCHAR(50) COMMENT '文件类型',
    mime_type VARCHAR(100) COMMENT 'MIME类型',
    file_hash VARCHAR(64) COMMENT '文件哈希值(SHA256)',
    upload_user_id BIGINT COMMENT '上传用户ID',
    upload_username VARCHAR(50) COMMENT '上传用户名',
    download_count INT DEFAULT 0 COMMENT '下载次数',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    remark TEXT COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件信息表';

-- 创建索引
-- 操作日志表索引
CREATE INDEX idx_operation_log_user_id ON sys_operation_log(user_id);
CREATE INDEX idx_operation_log_username ON sys_operation_log(username);
CREATE INDEX idx_operation_log_operation ON sys_operation_log(operation);
CREATE INDEX idx_operation_log_create_time ON sys_operation_log(create_time);
CREATE INDEX idx_operation_log_status ON sys_operation_log(status);

-- 登录日志表索引
CREATE INDEX idx_login_log_username ON sys_login_log(username);
CREATE INDEX idx_login_log_ip ON sys_login_log(ip);
CREATE INDEX idx_login_log_create_time ON sys_login_log(create_time);
CREATE INDEX idx_login_log_status ON sys_login_log(status);

-- 文件信息表索引
CREATE INDEX idx_file_info_upload_user_id ON sys_file_info(upload_user_id);
CREATE INDEX idx_file_info_file_name ON sys_file_info(file_name);
CREATE INDEX idx_file_info_file_type ON sys_file_info(file_type);
CREATE INDEX idx_file_info_create_time ON sys_file_info(create_time);
CREATE INDEX idx_file_info_status ON sys_file_info(status);
CREATE INDEX idx_file_info_file_hash ON sys_file_info(file_hash);