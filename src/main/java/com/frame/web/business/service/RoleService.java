package com.frame.web.business.service;

import com.frame.core.sql.Pager;
import com.frame.core.sql.Record;
import com.frame.web.business.entity.orgainzation.Role;
import com.frame.web.business.entity.ref.UserRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoleService {
    Role saveOrUpdate(Role role);

    Pager roleList(String name, String code, Pager page);

    Role roleInfo(String roleId);

    Role getRoleByCode(String code);

    List<Record> roleRefList(String roleId, String userId);

    @Transactional
    void deleteRole(String roleId);

    @Transactional
    void authc(List<UserRole> refs);
}
