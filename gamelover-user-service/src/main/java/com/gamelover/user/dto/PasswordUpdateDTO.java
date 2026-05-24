package com.gamelover.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@ApiModel("修改密码请求")
public class PasswordUpdateDTO {

    @NotBlank(message = "旧密码不能为空")
    @ApiModelProperty(value = "旧密码", required = true, example = "123456")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "新密码长度为6-20个字符")
    @ApiModelProperty(value = "新密码", required = true, example = "654321")
    private String newPassword;
}
