package com.frame.web.base.login;

import java.util.List;

public interface LoginUserRoleDao {

    List<BaseUserRole> findAllByAccount(String account);
}
