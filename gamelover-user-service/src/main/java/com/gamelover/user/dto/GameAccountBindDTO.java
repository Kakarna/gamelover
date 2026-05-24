package com.gamelover.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("游戏账号绑定请求")
public class GameAccountBindDTO {

    @NotBlank(message = "游戏编码不能为空")
    @ApiModelProperty(value = "游戏编码", required = true, example = "WUWA")
    private String gameCode;

    @NotBlank(message = "游戏UID不能为空")
    @ApiModelProperty(value = "游戏内UID", required = true, example = "123456789")
    private String gameUid;

    @ApiModelProperty(value = "游戏内昵称", example = "漂泊者")
    private String gameNickname;

    @ApiModelProperty(value = "服务器ID", example = "os_abcd_001")
    private String serverId;

    @ApiModelProperty(value = "服务器名称", example = "手机游戏-A")
    private String serverName;

    @ApiModelProperty(value = "API认证Token")
    private String authToken;

    @ApiModelProperty(value = "Cookie")
    private String authCookie;
}
