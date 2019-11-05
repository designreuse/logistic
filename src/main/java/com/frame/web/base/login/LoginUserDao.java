package com.frame.web.base.login;

import java.util.List;

public interface LoginUserDao {
    BaseUser findByAccount(String account);

    List<BaseUser> findAllBySync(Integer sync);

    BaseUser save(BaseUser baseUser);
}
