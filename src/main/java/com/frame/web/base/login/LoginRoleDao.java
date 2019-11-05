package com.frame.web.base.login;

import java.util.List;

public interface LoginRoleDao {

    List<BaseRole> findAllBySync(Integer sync);

    BaseRole save(BaseRole baseRole);
}
