package com.gamelover.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "success"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    USER_ALREADY_EXIST(1001, "用户已存在"),
    USER_NOT_EXIST(1002, "用户不存在"),
    USER_NOT_FOUND(1002, "用户不存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    TOKEN_INVALID(1004, "Token无效"),
    TOKEN_EXPIRED(1005, "Token已过期"),
    GAME_ACCOUNT_ALREADY_BIND(2001, "游戏账号已绑定"),
    GAME_ACCOUNT_NOT_BIND(2002, "游戏账号未绑定"),
    GAME_ACCOUNT_BIND_FAILED(2003, "游戏账号绑定失败"),
    DATA_SYNC_FAILED(3001, "数据同步失败"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误");

    private final Integer code;
    private final String message;
}