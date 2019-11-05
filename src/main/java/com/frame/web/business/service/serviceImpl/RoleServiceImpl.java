package com.frame.web.business.service.serviceImpl;

import com.logistics.core.sql.PageUtil;
import com.logistics.core.sql.Record;
import com.logistics.core.sql.RefRule;
import com.logistics.core.utils.EncryptUtil;
import com.logistics.web.dao.RoleDao;
import com.logistics.web.dao.ref.RefUserRoleDao;
import com.logistics.web.entity.orgainzation.Role;
import com.logistics.web.entity.ref.Ref_user_role;
import com.logistics.web.service.RoleService;
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
    private RefUserRoleDao refUserRoleDao;
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
    public PageUtil roleList(String name, String code, PageUtil page) {
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
    public void authc(List<Ref_user_role> refs) {
        List<RefRule<Ref_user_role>> rus = refs.stream().map(ref -> {
            ref.setId(Strings.isNotEmpty(ref.getId()) ? ref.getId() : EncryptUtil.randomUUID());
            return new RefRule<>(ref, RefRule.Rule.valueOf(ref.getRule().toUpperCase()));
        }).collect(Collectors.toList());
        refUserRoleDao.refRuleOperation(rus);
        rus.forEach(ru -> {
            Ref_user_role ref = ru.getEntity();
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
