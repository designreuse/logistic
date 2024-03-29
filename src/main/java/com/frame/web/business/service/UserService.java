package com.frame.web.business.service;

import com.frame.core.sql.Pager;
import com.frame.web.business.entity.orgainzation.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    @Transactional
    User save(User user) throws Exception;

    @Transactional
    User saveOrUpdate(User user);

    @Transactional
    User pending(User user);

    @Transactional
    void activeUser(String account, boolean resolve);

    User getUserByAccount(String account);

    void deleteUser(String userId);

    Pager getUserList(String name, String mobile, String deptId, Integer revision, Pager page);

    Pager getPendList(String applier, Pager page);

    User getUserById(String userId);
}
