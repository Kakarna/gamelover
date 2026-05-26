package com.gamelover.user.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GameAccountBindDTO {

    @NotBlank(message = "游戏编码不能为空")
    private String gameCode;

    @NotBlank(message = "游戏UID不能为空")
    private String gameUid;

    private String gameNickname;

    private String serverId;

    private String serverName;

    private String authToken;

    private String authCookie;
}