package com.gamelover.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户更新请求")
public class UserUpdateDTO {

    @ApiModelProperty(value = "昵称", example = "新昵称")
    private String nickname;

    @ApiModelProperty(value = "头像URL", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    @ApiModelProperty(value = "邮箱", example = "new@example.com")
    private String email;

    @ApiModelProperty(value = "手机号", example = "13900139000")
    private String phone;
}
