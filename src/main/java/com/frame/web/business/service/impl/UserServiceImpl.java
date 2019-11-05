package com.frame.web.business.service.impl;

import com.frame.core.utils.EncryptUtil;
import com.frame.web.business.dao.UserDao;
import com.frame.web.business.entity.User;
import com.frame.web.business.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public void saveUser(User user) {
        user.setId(EncryptUtil.randomUUID());
        user.setSalt(EncryptUtil.randomUUID());
        userDao.saveOrUpdate(user);
        user = null;
        user.getAccount();
    }
}
