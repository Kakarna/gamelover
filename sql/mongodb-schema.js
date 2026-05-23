// GameLover MongoDB Schema
// 数据库: gamelover

// ========== 公告表 ==========
db.createCollection("announcement", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["announcement_id", "game_list", "start_time_ms", "end_time_ms"],
            properties: {
                announcement_id: {
                    bsonType: "string",
                    description: "公告ID"
                },
                game_list: {
                    bsonType: "array",
                    description: "关联游戏列表"
                },
                is_red_dot: {
                    bsonType: "int",
                    description: "是否红点提示: 0-否, 1-是"
                },
                is_permanent: {
                    bsonType: "int",
                    description: "是否永久: 0-否, 1-是"
                },
                start_time_ms: {
                    bsonType: "long",
                    description: "开始时间(毫秒时间戳)"
                },
                end_time_ms: {
                    bsonType: "long",
                    description: "结束时间(毫秒时间戳)"
                },
                is_to_webp: {
                    bsonType: "bool",
                    description: "是否转换为WebP"
                },
                platform_list: {
                    bsonType: "array",
                    description: "平台列表: 1-Android, 2-iOS, 3-PC"
                },
                channel_list: {
                    bsonType: "array",
                    description: "渠道列表"
                },
                white_list: {
                    bsonType: "array",
                    description: "白名单"
                },
                content_html: {
                    bsonType: "string",
                    description: "公告内容(HTML)"
                },
                title: {
                    bsonType: "string",
                    description: "公告标题"
                },
                game_code: {
                    bsonType: "string",
                    description: "游戏编码: WUWA-鸣潮, HSR-星穹铁道, Genshin-原神"
                },
                announcement_type: {
                    bsonType: "string",
                    description: "公告类型: VERSION-版本公告, EVENT-活动公告, BUGFIX-修复公告"
                },
                priority: {
                    bsonType: "int",
                    description: "优先级: 越大越靠前"
                },
                create_time: {
                    bsonType: "date",
                    description: "创建时间"
                },
                update_time: {
                    bsonType: "date",
                    description: "更新时间"
                }
            }
        }
    }
});

// 创建索引
db.announcement.createIndex({ announcement_id: 1 }, { unique: true });
db.announcement.createIndex({ game_code: 1 });
db.announcement.createIndex({ start_time_ms: 1, end_time_ms: 1 });
db.announcement.createIndex({ priority: -1, create_time: -1 });
