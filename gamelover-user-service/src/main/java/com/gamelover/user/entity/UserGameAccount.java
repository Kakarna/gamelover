package com.gamelover.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("user_game_account")
public class UserGameAccount implements Serializable {

    @TableId(type = IdType.AUTO, value = "bind_uid")
    private Long bindUid;

    @TableField("user_uid")
    private Long userUid;

    @TableField("game_code")
    private String gameCode;

    @TableField("game_uid")
    private String gameUid;

    @TableField("game_nickname")
    private String gameNickname;

    @TableField("server_id")
    private String serverId;

    @TableField("server_name")
    private String serverName;

    @TableField("auth_token")
    private String authToken;

    @TableField("auth_cookie")
    private String authCookie;

    @TableField("refresh_token")
    private String refreshToken;

    @TableField("auth_expire_time")
    private LocalDateTime authExpireTime;

    @TableField("is_main")
    private Integer isMain;

    @TableField("sync_status")
    private Integer syncStatus;

    @TableField("last_sync_time")
    private LocalDateTime lastSyncTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}
