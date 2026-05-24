package com.gamelover.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("用户登录请求")
public class LoginDTO {

    @NotBlank(message = "账号不能为空")
    @ApiModelProperty(value = "账号（用户名/邮箱/手机号）", required = true, example = "admin")
    private String account;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码", required = true, example = "123456")
    private String password;
}
