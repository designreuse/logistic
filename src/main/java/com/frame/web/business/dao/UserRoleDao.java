package com.frame.web.business.dao;

import com.frame.web.base.baseRepository.BaseRepository;
import com.frame.web.base.login.BaseUserRole;
import com.frame.web.base.login.LoginUserRoleDao;
import com.frame.web.business.entity.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleDao extends BaseRepository<UserRole,String>, LoginUserRoleDao {

    @Override
    List<BaseUserRole> findAllByAccount(String account);
}
