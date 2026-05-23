-- Nacos 2.x MySQL Schema
-- 数据库: nacos_config

CREATE DATABASE IF NOT EXISTS nacos_config DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE nacos_config;

-- 配置信息表
CREATE TABLE IF NOT EXISTS config_info (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
    data_id VARCHAR(255) NOT NULL COMMENT 'data_id',
    group_id VARCHAR(128) NOT NULL COMMENT 'group_id',
    content LONGTEXT NOT NULL COMMENT 'content',
    md5 VARCHAR(32) DEFAULT NULL COMMENT 'md5',
    gmt_create DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    gmt_modified DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    src_user TEXT COMMENT 'source user',
    src_ip VARCHAR(50) DEFAULT NULL COMMENT 'source ip',
    app_name VARCHAR(128) DEFAULT NULL COMMENT 'app_name',
    tenant_id VARCHAR(128) DEFAULT '' COMMENT '租户字段',
    c_desc VARCHAR(256) DEFAULT NULL COMMENT 'description',
    c_use VARCHAR(64) DEFAULT NULL COMMENT 'use',
    effect VARCHAR(64) DEFAULT NULL COMMENT 'effect',
    type VARCHAR(64) DEFAULT NULL COMMENT 'type',
    c_schema TEXT COMMENT 'schema',
    PRIMARY KEY (id),
    UNIQUE KEY uk_configinfo_datagrouptenant (data_id, group_id, tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='config_info';

-- 配置信息聚合表
CREATE TABLE IF NOT EXISTS config_info_aggr (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
    data_id VARCHAR(255) NOT NULL COMMENT 'data_id',
    group_id VARCHAR(128) NOT NULL COMMENT 'group_id',
    datum_id VARCHAR(128) NOT NULL COMMENT 'datum_id',
    content LONGTEXT NOT NULL COMMENT 'content',
    gmt_modified DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    app_name VARCHAR(128) DEFAULT NULL COMMENT 'app_name',
    tenant_id VARCHAR(128) DEFAULT '' COMMENT '租户字段',
    PRIMARY KEY (id),
    KEY idx_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='config_info_aggr';

-- 配置信息Beta表
CREATE TABLE IF NOT EXISTS config_info_beta (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
    data_id VARCHAR(255) NOT NULL COMMENT 'data_id',
    group_id VARCHAR(128) NOT NULL COMMENT 'group_id',
    app_name VARCHAR(128) DEFAULT NULL COMMENT 'app_name',
    content LONGTEXT NOT NULL COMMENT 'content',
    beta_ips VARCHAR(1024) DEFAULT NULL COMMENT 'betaIps',
    md5 VARCHAR(32) DEFAULT NULL COMMENT 'md5',
    gmt_create DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    gmt_modified DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    src_user TEXT COMMENT 'source user',
    src_ip VARCHAR(50) DEFAULT NULL COMMENT 'source ip',
    tenant_id VARCHAR(128) DEFAULT '' COMMENT '租户字段',
    PRIMARY KEY (id),
    UNIQUE KEY uk_configinfobeta_datagrouptenant (data_id, group_id, tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='config_info_beta';

-- 配置信息标签表
CREATE TABLE IF NOT EXISTS config_info_tag (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
    data_id VARCHAR(255) NOT NULL COMMENT 'data_id',
    group_id VARCHAR(128) NOT NULL COMMENT 'group_id',
    tenant_id VARCHAR(128) DEFAULT '' COMMENT '租户字段',
    tag_id VARCHAR(128) NOT NULL COMMENT 'tag_id',
    app_name VARCHAR(128) DEFAULT NULL COMMENT 'app_name',
    content LONGTEXT NOT NULL COMMENT 'content',
    md5 VARCHAR(32) DEFAULT NULL COMMENT 'md5',
    gmt_create DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    gmt_modified DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    src_user TEXT COMMENT 'source user',
    src_ip VARCHAR(50) DEFAULT NULL COMMENT 'source ip',
    PRIMARY KEY (id),
    UNIQUE KEY uk_configinfotag_datagrouptenant (data_id, group_id, tenant_id, tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='config_info_tag';

-- 配置标签关系表
CREATE TABLE IF NOT EXISTS config_tags_relation (
    id BIGINT NOT NULL COMMENT 'id',
    tag_name VARCHAR(128) NOT NULL COMMENT 'tag_name',
    tag_type VARCHAR(64) DEFAULT NULL COMMENT 'tag_type',
    data_id VARCHAR(255) NOT NULL COMMENT 'data_id',
    group_id VARCHAR(128) NOT NULL COMMENT 'group_id',
    tenant_id VARCHAR(128) DEFAULT '' COMMENT 'tenant_id',
    nid BIGINT NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (nid),
    UNIQUE KEY uk_configtagrelation_configidtag (id, tag_name, tag_type),
    KEY idx_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='config_tag_relation';

-- 分组容量表
CREATE TABLE IF NOT EXISTS group_capacity (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'primary key id',
    group_id VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'Group ID',
    quota INT UNSIGNED NOT NULL DEFAULT '0' COMMENT 'quota',
    `usage` INT UNSIGNED NOT NULL DEFAULT '0' COMMENT 'usage',
    max_size INT UNSIGNED NOT NULL DEFAULT '0' COMMENT 'max size of config',
    max_aggr_count INT UNSIGNED NOT NULL DEFAULT '0' COMMENT 'max count of aggregated config data',
    max_aggr_size INT UNSIGNED NOT NULL DEFAULT '0' COMMENT 'max size of aggregated config data',
    max_history_count INT UNSIGNED NOT NULL DEFAULT '0' COMMENT 'max count of history records',
    gmt_create DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'created time',
    gmt_modified DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modified time',
    PRIMARY KEY (id),
    UNIQUE KEY uk_group_id (group_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='group_capacity';

-- 配置信息历史表
CREATE TABLE IF NOT EXISTS his_config_info (
    id BIGINT UNSIGNED NOT NULL,
    nid BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    data_id VARCHAR(255) NOT NULL,
    group_id VARCHAR(128) NOT NULL,
    app_name VARCHAR(128) DEFAULT NULL COMMENT 'app_name',
    content LONGTEXT NOT NULL,
    md5 VARCHAR(32) DEFAULT NULL,
    gmt_create DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gmt_modified DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    src_user TEXT,
    src_ip VARCHAR(50) DEFAULT NULL,
    op_type CHAR(10) DEFAULT NULL,
    tenant_id VARCHAR(128) DEFAULT '' COMMENT 'tenant_id',
    PRIMARY KEY (nid),
    KEY idx_gmt_create (gmt_create),
    KEY idx_gmt_modified (gmt_modified),
    KEY idx_did (data_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='his_config_info';

-- 租户容量表
CREATE TABLE IF NOT EXISTS tenant_capacity (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'primary key id',
    tenant_id VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'Tenant ID',
    quota INT UNSIGNED NOT NULL DEFAULT '0' COMMENT 'quota',
    `usage` INT UNSIGNED NOT NULL DEFAULT '0' COMMENT 'usage',
    max_size INT UNSIGNED NOT NULL DEFAULT '0' COMMENT 'max size of config',
    max_aggr_count INT UNSIGNED NOT NULL DEFAULT '0' COMMENT 'max count of aggregated config data',
    max_aggr_size INT UNSIGNED NOT NULL DEFAULT '0' COMMENT 'max size of aggregated config data',
    max_history_count INT UNSIGNED NOT NULL DEFAULT '0' COMMENT 'max count of history records',
    gmt_create DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'created time',
    gmt_modified DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modified time',
    PRIMARY KEY (id),
    UNIQUE KEY uk_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='tenant_capacity';

-- 租户信息表
CREATE TABLE IF NOT EXISTS tenant_info (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
    kp VARCHAR(128) NOT NULL COMMENT 'kp',
    tenant_id VARCHAR(128) DEFAULT '' COMMENT 'tenant_id',
    tenant_name VARCHAR(128) DEFAULT '' COMMENT 'tenant_name',
    tenant_desc VARCHAR(256) DEFAULT NULL COMMENT 'tenant_desc',
    create_resource VARCHAR(256) DEFAULT NULL COMMENT 'create_source',
    gmt_create BIGINT NOT NULL COMMENT 'created time',
    gmt_modified BIGINT NOT NULL COMMENT 'modified time',
    PRIMARY KEY (id),
    UNIQUE KEY uk_tenant_info_kptenantid (kp, tenant_id),
    KEY idx_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='tenant_info';

-- 应用列表表
CREATE TABLE IF NOT EXISTS app_list (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
    app_name VARCHAR(128) NOT NULL COMMENT 'app_name',
    description VARCHAR(256) DEFAULT NULL COMMENT 'description',
    type VARCHAR(32) DEFAULT NULL COMMENT 'type',
    gmt_create BIGINT NOT NULL COMMENT 'created time',
    gmt_modified BIGINT NOT NULL COMMENT 'modified time',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='app_list';

-- 应用配置数据关系表
CREATE TABLE IF NOT EXISTS app_configdata_relation (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
    app_name VARCHAR(128) NOT NULL COMMENT 'app_name',
    data_id VARCHAR(255) NOT NULL COMMENT 'data_id',
    group_id VARCHAR(128) NOT NULL COMMENT 'group_id',
    type VARCHAR(64) DEFAULT NULL COMMENT 'type',
    tenant_id VARCHAR(128) DEFAULT '' COMMENT 'tenant_id',
    gmt_create BIGINT NOT NULL COMMENT 'created time',
    gmt_modified BIGINT NOT NULL COMMENT 'modified time',
    PRIMARY KEY (id),
    UNIQUE KEY uk_app_configdata_relation (app_name, data_id, group_id, tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='app_configdata_relation';

-- 配置客户端锁表
CREATE TABLE IF NOT EXISTS config_client_lock (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
    client_id VARCHAR(128) NOT NULL COMMENT 'client_id',
    data_id VARCHAR(255) NOT NULL COMMENT 'data_id',
    group_id VARCHAR(128) NOT NULL COMMENT 'group_id',
    tenant_id VARCHAR(128) DEFAULT '' COMMENT 'tenant_id',
    gmt_create BIGINT NOT NULL COMMENT 'created time',
    gmt_modified BIGINT NOT NULL COMMENT 'modified time',
    PRIMARY KEY (id),
    UNIQUE KEY uk_config_client_lock (client_id, data_id, group_id, tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='config_client_lock';

-- 配置客户端错误信息表
CREATE TABLE IF NOT EXISTS config_client_flase_info (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
    client_id VARCHAR(128) NOT NULL COMMENT 'client_id',
    data_id VARCHAR(255) NOT NULL COMMENT 'data_id',
    group_id VARCHAR(128) NOT NULL COMMENT 'group_id',
    tenant_id VARCHAR(128) DEFAULT '' COMMENT 'tenant_id',
    app_name VARCHAR(128) DEFAULT NULL COMMENT 'app_name',
    ip VARCHAR(128) DEFAULT NULL COMMENT 'ip',
    config_svr VARCHAR(128) DEFAULT NULL COMMENT 'config_svr',
    error_info VARCHAR(512) DEFAULT NULL COMMENT 'error_info',
    gmt_create BIGINT NOT NULL COMMENT 'created time',
    gmt_modified BIGINT NOT NULL COMMENT 'modified time',
    PRIMARY KEY (id),
    KEY idx_client_id (client_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='config_client_flase_info';

-- 许可信息表
CREATE TABLE IF NOT EXISTS permissions (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
    role VARCHAR(50) NOT NULL COMMENT 'role',
    resource VARCHAR(255) NOT NULL COMMENT 'resource',
    action VARCHAR(8) NOT NULL COMMENT 'action',
    gmt_create BIGINT NOT NULL COMMENT 'created time',
    gmt_modified BIGINT NOT NULL COMMENT 'modified time',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='permissions';

-- 角色用户关系表
CREATE TABLE IF NOT EXISTS role_permissions (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
    role VARCHAR(50) NOT NULL COMMENT 'role',
    username VARCHAR(50) NOT NULL COMMENT 'username',
    gmt_create BIGINT NOT NULL COMMENT 'created time',
    gmt_modified BIGINT NOT NULL COMMENT 'modified time',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_permission (role, username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='role_permissions';

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) NOT NULL COMMENT 'username',
    password VARCHAR(500) NOT NULL COMMENT 'password',
    enabled TINYINT(1) NOT NULL COMMENT 'enabled',
    PRIMARY KEY (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='users';

-- 用户角色表
CREATE TABLE IF NOT EXISTS roles (
    username VARCHAR(50) NOT NULL COMMENT 'username',
    role VARCHAR(50) NOT NULL COMMENT 'role',
    UNIQUE KEY idx_user_role (username, role),
    KEY idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='user_roles';

-- 插入默认用户 (nacos/nacos)
INSERT INTO users (username, password, enabled) VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrXFvkO', 1);

INSERT INTO roles (username, role) VALUES ('nacos', 'ROLE_ADMIN');
