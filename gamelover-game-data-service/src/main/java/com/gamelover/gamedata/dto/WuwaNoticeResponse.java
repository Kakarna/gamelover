package com.gamelover.gamedata.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WuwaNoticeResponse {

    private List<GameNotice> game;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GameNotice {
        private String id;
        private Integer red;
        private Long startTimeMs;
        private Long endTimeMs;
        private String content;
        private String tabTitle;
        private String tabBanner;
        private String foldBanner;
    }
}