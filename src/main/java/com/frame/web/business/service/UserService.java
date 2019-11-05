package com.frame.web.business.service;

import com.logistics.core.sql.PageUtil;
import com.logistics.web.entity.orgainzation.User;
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

    PageUtil getUserList(String name, String mobile, String deptId, Integer revision, PageUtil page);

    PageUtil getPendList(String applier, PageUtil page);

    User getUserById(String userId);
}
