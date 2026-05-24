package com.gamelover.user.controller;

import com.gamelover.common.result.Result;
import com.gamelover.user.dto.*;
import com.gamelover.user.service.UserService;
import com.gamelover.user.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result<RegisterVO> register(@Valid @RequestBody RegisterDTO registerDTO) {
        return Result.success(userService.register(registerDTO));
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        return Result.success(userService.login(loginDTO));
    }

    @ApiOperation("获取当前用户信息")
    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo(@RequestHeader(value = "X-User-Id", required = false) Long userUid) {
        if (userUid == null) {
            userUid = 1L;
        }
        return Result.success(userService.getUserInfo(userUid));
    }

    @ApiOperation("更新用户信息")
    @PutMapping("/update")
    public Result<UserInfoVO> updateUserInfo(
            @RequestHeader(value = "X-User-Id", required = false) Long userUid,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        if (userUid == null) {
            userUid = 1L;
        }
        return Result.success(userService.updateUserInfo(userUid, userUpdateDTO));
    }

    @ApiOperation("修改密码")
    @PutMapping("/password")
    public Result<Void> updatePassword(
            @RequestHeader(value = "X-User-Id", required = false) Long userUid,
            @Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        if (userUid == null) {
            userUid = 1L;
        }
        userService.updatePassword(userUid, passwordUpdateDTO);
        return Result.success("密码修改成功", null);
    }

    @ApiOperation("绑定游戏账号")
    @PostMapping("/game-account/bind")
    public Result<GameAccountVO> bindGameAccount(
            @RequestHeader(value = "X-User-Id", required = false) Long userUid,
            @Valid @RequestBody GameAccountBindDTO bindDTO) {
        if (userUid == null) {
            userUid = 1L;
        }
        return Result.success(userService.bindGameAccount(userUid, bindDTO));
    }

    @ApiOperation("解绑游戏账号")
    @DeleteMapping("/game-account/unbind")
    public Result<Void> unbindGameAccount(
            @RequestHeader(value = "X-User-Id", required = false) Long userUid,
            @RequestParam Long bindUid) {
        if (userUid == null) {
            userUid = 1L;
        }
        userService.unbindGameAccount(userUid, bindUid);
        return Result.success("解绑成功", null);
    }

    @ApiOperation("获取游戏账号列表")
    @GetMapping("/game-account/list")
    public Result<List<GameAccountVO>> getGameAccountList(
            @RequestHeader(value = "X-User-Id", required = false) Long userUid) {
        if (userUid == null) {
            userUid = 1L;
        }
        return Result.success(userService.getGameAccountList(userUid));
    }
}
