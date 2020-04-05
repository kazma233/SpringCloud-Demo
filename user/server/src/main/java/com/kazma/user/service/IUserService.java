package com.kazma.user.service;

import com.kazma.user.entity.UserInfo;

public interface IUserService {

    public UserInfo findUserByOpenid(String uid);

}
