package com.frame.web.business.dao;

import com.frame.web.base.baseRepository.BaseRepository;
import com.frame.web.base.login.BaseRoleUserRef;
import com.frame.web.base.login.LoginRoleUserRefDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleUserRefDao extends BaseRepository<RoleUserRef, String>, LoginRoleUserRefDao {
    @Override
    List<BaseRoleUserRef> findAllByAccount(String account);
    @Override
    List<BaseRoleUserRef> findAllBySyncAndRevision(Integer sync,Integer revision);
}
