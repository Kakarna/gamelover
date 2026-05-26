package com.gamelover.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class User implements Serializable {

    @TableId(type = IdType.AUTO, value = "user_uid")
    private Long userUid;

    private String username;

    private String password;

    private String nickname;

    @TableField("avatar_url")
    private String avatarUrl;

    private String email;

    private String phone;

    @TableField("register_type")
    private String registerType;

    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;

    @TableField("last_login_ip")
    private String lastLoginIp;

    @TableField("email_verified")
    private Integer emailVerified;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}
