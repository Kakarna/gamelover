package com.gamelover.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gamelover.common.exception.BusinessException;
import com.gamelover.common.result.ResultCode;
import com.gamelover.user.dto.*;
import com.gamelover.user.entity.User;
import com.gamelover.user.entity.UserGameAccount;
import com.gamelover.user.mapper.UserGameAccountMapper;
import com.gamelover.user.mapper.UserMapper;
import com.gamelover.user.service.UserService;
import com.gamelover.user.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserGameAccountMapper userGameAccountMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public RegisterVO register(RegisterDTO registerDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, registerDTO.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXIST);
        }

        if (StringUtils.hasText(registerDTO.getEmail())) {
            LambdaQueryWrapper<User> emailWrapper = new LambdaQueryWrapper<>();
            emailWrapper.eq(User::getEmail, registerDTO.getEmail());
            if (userMapper.selectCount(emailWrapper) > 0) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "邮箱已被注册");
            }
        }

        if (StringUtils.hasText(registerDTO.getPhone())) {
            LambdaQueryWrapper<User> phoneWrapper = new LambdaQueryWrapper<>();
            phoneWrapper.eq(User::getPhone, registerDTO.getPhone());
            if (userMapper.selectCount(phoneWrapper) > 0) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "手机号已被注册");
            }
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setNickname(StringUtils.hasText(registerDTO.getNickname())
                ? registerDTO.getNickname() : registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        user.setRegisterType("LOCAL");
        user.setStatus(1);
        user.setEmailVerified(0);

        userMapper.insert(user);

        RegisterVO registerVO = new RegisterVO();
        registerVO.setUserUid(user.getUserUid());
        registerVO.setUsername(user.getUsername());
        registerVO.setNickname(user.getNickname());
        return registerVO;
    }

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginDTO.getAccount())
                .or()
                .eq(User::getEmail, loginDTO.getAccount())
                .or()
                .eq(User::getPhone, loginDTO.getAccount());

        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }

        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.FORBIDDEN, "账号已被禁用");
        }

        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getUserUid, user.getUserUid())
                .set(User::getLastLoginTime, LocalDateTime.now());
        userMapper.update(null, updateWrapper);

        String token = generateToken(user);

        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);

        LoginVO.UserBasicVO userBasicVO = new LoginVO.UserBasicVO();
        userBasicVO.setUserUid(user.getUserUid());
        userBasicVO.setUsername(user.getUsername());
        userBasicVO.setNickname(user.getNickname());
        userBasicVO.setAvatarUrl(user.getAvatarUrl());
        loginVO.setUserInfo(userBasicVO);

        return loginVO;
    }

    @Override
    public UserInfoVO getUserInfo(Long userUid) {
        User user = userMapper.selectById(userUid);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        return convertToUserInfoVO(user);
    }

    @Override
    public UserInfoVO updateUserInfo(Long userUid, UserUpdateDTO userUpdateDTO) {
        User user = userMapper.selectById(userUid);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        if (StringUtils.hasText(userUpdateDTO.getNickname())) {
            user.setNickname(userUpdateDTO.getNickname());
        }
        if (StringUtils.hasText(userUpdateDTO.getAvatarUrl())) {
            user.setAvatarUrl(userUpdateDTO.getAvatarUrl());
        }
        if (StringUtils.hasText(userUpdateDTO.getEmail())) {
            user.setEmail(userUpdateDTO.getEmail());
        }
        if (StringUtils.hasText(userUpdateDTO.getPhone())) {
            user.setPhone(userUpdateDTO.getPhone());
        }

        userMapper.updateById(user);
        return convertToUserInfoVO(user);
    }

    @Override
    public void updatePassword(Long userUid, PasswordUpdateDTO passwordUpdateDTO) {
        User user = userMapper.selectById(userUid);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        if (!passwordEncoder.matches(passwordUpdateDTO.getOldPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR, "旧密码错误");
        }

        user.setPassword(passwordEncoder.encode(passwordUpdateDTO.getNewPassword()));
        userMapper.updateById(user);
    }

    @Override
    public GameAccountVO bindGameAccount(Long userUid, GameAccountBindDTO bindDTO) {
        LambdaQueryWrapper<UserGameAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserGameAccount::getUserUid, userUid)
                .eq(UserGameAccount::getGameCode, bindDTO.getGameCode())
                .eq(UserGameAccount::getGameUid, bindDTO.getGameUid());
        if (userGameAccountMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ResultCode.DATA_SYNC_FAILED, "游戏账号已绑定");
        }

        long count = userGameAccountMapper.selectCount(
                new LambdaQueryWrapper<UserGameAccount>().eq(UserGameAccount::getUserUid, userUid));
        int isMain = count == 0 ? 1 : 0;

        UserGameAccount account = new UserGameAccount();
        account.setUserUid(userUid);
        account.setGameCode(bindDTO.getGameCode());
        account.setGameUid(bindDTO.getGameUid());
        account.setGameNickname(bindDTO.getGameNickname());
        account.setServerId(bindDTO.getServerId());
        account.setServerName(bindDTO.getServerName());
        account.setAuthToken(bindDTO.getAuthToken());
        account.setAuthCookie(bindDTO.getAuthCookie());
        account.setIsMain(isMain);
        account.setSyncStatus(1);

        userGameAccountMapper.insert(account);

        GameAccountVO vo = new GameAccountVO();
        vo.setBindUid(account.getBindUid());
        vo.setGameCode(account.getGameCode());
        vo.setGameUid(account.getGameUid());
        vo.setGameNickname(account.getGameNickname());
        vo.setIsMain(account.getIsMain());
        return vo;
    }

    @Override
    public void unbindGameAccount(Long userUid, Long bindUid) {
        UserGameAccount account = userGameAccountMapper.selectById(bindUid);
        if (account == null || !account.getUserUid().equals(userUid)) {
            throw new BusinessException(ResultCode.NOT_FOUND, "绑定记录不存在");
        }
        userGameAccountMapper.deleteById(bindUid);
    }

    @Override
    public List<GameAccountVO> getGameAccountList(Long userUid) {
        LambdaQueryWrapper<UserGameAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserGameAccount::getUserUid, userUid)
                .orderByDesc(UserGameAccount::getIsMain)
                .orderByDesc(UserGameAccount::getCreateTime);

        List<UserGameAccount> accounts = userGameAccountMapper.selectList(wrapper);
        return accounts.stream().map(this::convertToGameAccountVO).collect(Collectors.toList());
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    @Override
    public User getUserByUid(Long userUid) {
        return userMapper.selectById(userUid);
    }

    private String generateToken(User user) {
        return "jwt_token_" + user.getUserUid() + "_" + System.currentTimeMillis();
    }

    private UserInfoVO convertToUserInfoVO(User user) {
        UserInfoVO vo = new UserInfoVO();
        vo.setUserUid(user.getUserUid());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setAvatarUrl(user.getAvatarUrl());
        vo.setEmail(user.getEmail());
        vo.setPhone(user.getPhone());
        vo.setRegisterType(user.getRegisterType());
        vo.setStatus(user.getStatus());
        if (user.getLastLoginTime() != null) {
            vo.setLastLoginTime(user.getLastLoginTime().format(DATE_TIME_FORMATTER));
        }
        return vo;
    }

    private GameAccountVO convertToGameAccountVO(UserGameAccount account) {
        GameAccountVO vo = new GameAccountVO();
        vo.setBindUid(account.getBindUid());
        vo.setGameCode(account.getGameCode());
        vo.setGameUid(account.getGameUid());
        vo.setGameNickname(account.getGameNickname());
        vo.setServerId(account.getServerId());
        vo.setServerName(account.getServerName());
        vo.setIsMain(account.getIsMain());
        vo.setSyncStatus(account.getSyncStatus());
        if (account.getLastSyncTime() != null) {
            vo.setLastSyncTime(account.getLastSyncTime().format(DATE_TIME_FORMATTER));
        }
        if (account.getCreateTime() != null) {
            vo.setCreateTime(account.getCreateTime().format(DATE_TIME_FORMATTER));
        }
        return vo;
    }
}
