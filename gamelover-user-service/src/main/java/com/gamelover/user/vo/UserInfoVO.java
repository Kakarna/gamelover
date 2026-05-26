package com.gamelover.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO {

    private Long userUid;
    private String username;
    private String nickname;
    private String avatarUrl;
    private String email;
    private String phone;
    private String registerType;
    private String lastLoginTime;
    private Integer status;
}
