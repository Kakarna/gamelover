package com.gamelover.gamedata.vo;

import lombok.Data;

import java.time.Instant;

@Data
public class AnnouncementVO {

    private String announcementId;

    private String gameCode;

    private String title;

    private String contentHtml;

    private String tabBanner;

    private String foldBanner;

    private Integer isRedDot;

    private Long startTimeMs;

    private Long endTimeMs;

    private String startTimeStr;

    private String endTimeStr;

    private String announcementType;

    private Integer priority;

    private Instant createTime;
}