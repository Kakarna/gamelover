package com.gamelover.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@ApiModel("用户注册请求")
public class RegisterDTO {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度为3-20个字符")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    @ApiModelProperty(value = "用户名", required = true, example = "admin")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度为6-20个字符")
    @ApiModelProperty(value = "密码", required = true, example = "123456")
    private String password;

    @ApiModelProperty(value = "邮箱", example = "admin@example.com")
    private String email;

    @ApiModelProperty(value = "手机号", example = "13800138000")
    private String phone;

    @ApiModelProperty(value = "昵称", example = "管理员")
    private String nickname;
}
