package com.gamelover.gamedata.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Data
@Document(collection = "announcement")
public class Announcement {

    @Id
    private String id;

    @Field("announcement_id")
    private String announcementId;

    @Field("game_code")
    private String gameCode;

    @Field("title")
    private String title;

    @Field("content_html")
    private String contentHtml;

    @Field("tab_banner")
    private String tabBanner;

    @Field("fold_banner")
    private String foldBanner;

    @Field("is_red_dot")
    private Integer isRedDot;

    @Field("start_time_ms")
    private Long startTimeMs;

    @Field("end_time_ms")
    private Long endTimeMs;

    @Field("announcement_type")
    private String announcementType;

    @Field("priority")
    private Integer priority;

    @Field("create_time")
    private Instant createTime;

    @Field("update_time")
    private Instant updateTime;
}