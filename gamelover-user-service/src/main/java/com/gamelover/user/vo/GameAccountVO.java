package com.gamelover.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameAccountVO {

    private Long bindUid;
    private String gameCode;
    private String gameUid;
    private String gameNickname;
    private String serverId;
    private String serverName;
    private Integer isMain;
    private Integer syncStatus;
    private String lastSyncTime;
    private String createTime;
}
