package com.frame.web.base.login;

import java.util.List;

public interface LoginRoleUserRefDao {

    List<BaseRoleUserRef> findAllByAccount(String account);
}
