-- GameLover 应用数据库 Schema
-- 数据库: gamelover

CREATE DATABASE IF NOT EXISTS gamelover DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE gamelover;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    user_uid BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户UID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(128) NOT NULL COMMENT '密码(BCrypt加密)',
    nickname VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    avatar_url VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    register_type VARCHAR(20) NOT NULL DEFAULT 'LOCAL' COMMENT '注册类型: LOCAL, GOOGLE, DISCORD等',
    last_login_time DATETIME DEFAULT NULL COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
    email_verified TINYINT(1) NOT NULL DEFAULT 0 COMMENT '邮箱验证: 0-未验证, 1-已验证',
    status TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-正常',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    PRIMARY KEY (user_uid),
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_email (email),
    UNIQUE KEY uk_phone (phone),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 用户游戏账号绑定表
CREATE TABLE IF NOT EXISTS user_game_account (
    bind_uid BIGINT NOT NULL AUTO_INCREMENT COMMENT '绑定UID',
    user_uid BIGINT NOT NULL COMMENT '用户UID',
    
    -- 游戏信息
    game_code VARCHAR(50) NOT NULL COMMENT '游戏编码: WUWA-鸣潮, HSR-星穹铁道, Genshin-原神',
    game_uid VARCHAR(50) NOT NULL COMMENT '游戏内UID',
    game_nickname VARCHAR(100) DEFAULT NULL COMMENT '游戏内昵称',
    server_id VARCHAR(50) DEFAULT NULL COMMENT '服务器ID',
    server_name VARCHAR(100) DEFAULT NULL COMMENT '服务器名称',
    
    -- API认证信息(加密存储)
    auth_token TEXT DEFAULT NULL COMMENT 'API认证Token',
    auth_cookie TEXT DEFAULT NULL COMMENT 'Cookie',
    refresh_token VARCHAR(512) DEFAULT NULL COMMENT '刷新Token',
    auth_expire_time DATETIME DEFAULT NULL COMMENT '认证过期时间',
    
    -- 状态
    is_main TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否主账号: 0-否, 1-是',
    sync_status TINYINT(1) NOT NULL DEFAULT 1 COMMENT '同步状态: 0-暂停, 1-正常',
    last_sync_time DATETIME DEFAULT NULL COMMENT '最后同步时间',
    
    -- 元数据
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除标记',
    
    PRIMARY KEY (bind_uid),
    UNIQUE KEY uk_user_game_uid (user_uid, game_code, game_uid),
    KEY idx_game_code (game_code),
    KEY idx_user_uid (user_uid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户游戏账号绑定表';
