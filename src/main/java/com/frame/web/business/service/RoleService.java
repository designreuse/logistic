package com.frame.web.business.service;

import com.logistics.core.sql.PageUtil;
import com.logistics.core.sql.Record;
import com.logistics.web.entity.orgainzation.Role;
import com.logistics.web.entity.ref.Ref_user_role;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoleService {
    Role saveOrUpdate(Role role);

    PageUtil roleList(String name, String code, PageUtil page);

    Role roleInfo(String roleId);

    Role getRoleByCode(String code);

    List<Record> roleRefList(String roleId, String userId);

    @Transactional
    void deleteRole(String roleId);

    @Transactional
    void authc(List<Ref_user_role> refs);
}
