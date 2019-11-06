package com.frame.web.business.service.impl;

import com.frame.core.sql.Pager;
import com.frame.core.sql.Record;
import com.frame.core.sql.RefRule;
import com.frame.core.utils.EncryptUtil;
import com.frame.web.business.dao.RoleDao;
import com.frame.web.business.dao.ref.UserRoleDao;
import com.frame.web.business.entity.orgainzation.Role;
import com.frame.web.business.entity.ref.UserRole;
import com.frame.web.business.service.RoleService;
import org.apache.logging.log4j.util.Strings;
import org.camunda.bpm.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserRoleDao refUserRoleDao;
    @Autowired
    private IdentityService identityService;

    @Override
    @Transactional
    public Role saveOrUpdate(Role role) {
        role = roleDao.saveOrUpdate(role);
        if (identityService.createGroupQuery().groupId(role.getCode()).singleResult() == null) {
            identityService.saveGroup(identityService.newGroup(role.getCode()));
        }
        return role;
    }

    @Override
    public Pager roleList(String name, String code, Pager page) {
        return roleDao.roleList(name, code, page);
    }

    @Override
    public Role roleInfo(String roleId) {
        return roleDao.find(roleId);
    }

    @Override
    public Role getRoleByCode(String code) {
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);
        return (Role) roleDao.findOne(param);
    }

    @Override
    public List<Record> roleRefList(String roleId, String userId) {
        return refUserRoleDao.roleRefList(roleId, userId);
    }

    @Transactional
    @Override
    public void deleteRole(String roleId) {
        Role role = roleDao.findById(roleId).get();
        roleDao.deleteObject(role);
        Map<String, Object> params = new HashMap<>();
        params.put("code", role.getCode());
        refUserRoleDao.deleteByParam(params);
        // 删除组,并自动把组关联信息删除
        identityService.deleteGroup(role.getCode());
    }

    @Transactional
    @Override
    public void authc(List<UserRole> refs) {
        List<RefRule<UserRole>> rus = refs.stream().map(ref -> {
            ref.setId(Strings.isNotEmpty(ref.getId()) ? ref.getId() : EncryptUtil.randomUUID());
            return new RefRule<>(ref, RefRule.Rule.valueOf(ref.getRule().toUpperCase()));
        }).collect(Collectors.toList());
        refUserRoleDao.refRuleOperation(rus);
        rus.forEach(ru -> {
            UserRole ref = ru.getEntity();
            switch (ru.getRule()) {
                case INSERT:
                    identityService.createMembership(ref.getAccount(), ref.getRole());
                    break;
                case DELETE:
                    identityService.deleteMembership(ref.getAccount(), ref.getRole());
                    break;
            }
        });
    }
}
