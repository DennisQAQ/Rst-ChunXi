package com.ChunXi.service;

import com.ChunXi.dto.UserLoginDTO;
import com.ChunXi.entity.User;

public interface UserService {

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    User WXLogin(UserLoginDTO userLoginDTO);
}
