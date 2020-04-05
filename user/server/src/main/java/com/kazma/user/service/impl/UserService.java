package com.kazma.user.service.impl;

import com.kazma.user.dao.UserDao;
import com.kazma.user.entity.UserInfo;
import com.kazma.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserInfo findUserByOpenid(String uid) {
        return userDao.findByOpenid(uid);
    }
}
