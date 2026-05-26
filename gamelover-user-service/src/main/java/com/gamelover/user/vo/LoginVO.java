package com.gamelover.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {

    private String token;
    private UserBasicVO userInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserBasicVO {
        private Long userUid;
        private String username;
        private String nickname;
        private String avatarUrl;
    }
}
