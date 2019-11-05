package com.frame.web.business.dao;

import com.frame.web.base.baseRepository.BaseRepository;
import com.frame.web.base.login.BaseRole;
import com.frame.web.base.login.LoginRoleDao;
import com.frame.web.business.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDao extends BaseRepository<Role,String>, LoginRoleDao {
    @Override
    List<BaseRole> findAllBySync(Integer sync);

    @Override
    BaseRole save(BaseRole baseRole);
}
