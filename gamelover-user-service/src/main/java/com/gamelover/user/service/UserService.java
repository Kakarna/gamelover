package com.gamelover.user.service;

import com.gamelover.user.dto.*;
import com.gamelover.user.entity.User;
import com.gamelover.user.entity.UserGameAccount;
import com.gamelover.user.vo.*;

import java.util.List;

public interface UserService {

    RegisterVO register(RegisterDTO registerDTO);

    LoginVO login(LoginDTO loginDTO);

    UserInfoVO getUserInfo(Long userUid);

    UserInfoVO updateUserInfo(Long userUid, UserUpdateDTO userUpdateDTO);

    void updatePassword(Long userUid, PasswordUpdateDTO passwordUpdateDTO);

    GameAccountVO bindGameAccount(Long userUid, GameAccountBindDTO bindDTO);

    void unbindGameAccount(Long userUid, Long bindUid);

    List<GameAccountVO> getGameAccountList(Long userUid);

    User getUserByUsername(String username);

    User getUserByUid(Long userUid);
}
