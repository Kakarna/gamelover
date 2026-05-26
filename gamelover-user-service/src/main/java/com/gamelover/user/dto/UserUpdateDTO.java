package com.gamelover.user.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {

    private String nickname;

    private String avatarUrl;

    private String email;

    private String phone;
}