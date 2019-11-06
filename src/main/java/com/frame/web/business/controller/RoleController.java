package com.frame.web.business.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.frame.core.http.BusinessException;
import com.frame.core.http.ResponseEntity;
import com.frame.core.sql.Pager;
import com.frame.core.sql.Record;
import com.frame.core.utils.EncryptUtil;
import com.frame.web.base.Enum.Revision;
import com.frame.web.base.Enum.Sync;
import com.frame.web.business.entity.orgainzation.Role;
import com.frame.web.business.entity.ref.UserRole;
import com.frame.web.business.service.RoleService;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    Logger logger = LoggerFactory.getLogger(RoleController.class);
    @Autowired
    private RoleService roleService;

    // 新增或修改role
    @PostMapping("/save")
    public ResponseEntity saveOrUpdate(@RequestBody String roleJson) {
        try {
            JSONObject json = JSON.parseObject(roleJson);
            if(Strings.isEmpty(json.getString("code"))){
                logger.error("角色保存失败！code不能为空");
                throw new BusinessException("code不能为空");
            }
            Role role = roleService.getRoleByCode(json.getString("code"));
            if(role==null){
                role = new Role();
                role.setCreateTime(LocalDateTime.now());
            }else if(!role.getId().equals(json.getString("id"))){
                logger.error("角色保存失败！code已存在");
                throw new BusinessException("code已存在");
            }
            role.setId(Strings.isNotEmpty(json.getString("id"))?json.getString("id"): EncryptUtil.randomUUID());
            role.setCode(json.getString("code"));
            role.setName(json.getString("name"));
            role.setRevision(Revision.ACTIVE.getInt());
            role.setSync(Sync.PENDING.getInt());
            role.setUpdateTime(LocalDateTime.now());
            role = roleService.saveOrUpdate(role);
            logger.info("角色保存成功！");
            return ResponseEntity.ok("角色保存成功!").putData(role);
        } catch (Exception e) {
            logger.error("角色保存失败!");
            throw new BusinessException("角色保存失败!", e);
        }
    }

    // role列表
    @GetMapping("/roleList")
    public ResponseEntity roleList(@Param("name") String name,
                                   @Param("code") String code,
                                   @Param("page") String pageJson) {
        try {
            JSONObject json = JSON.parseObject(pageJson);
            Pager page = new Pager(json.getInteger("current"), json.getInteger("pageSize"));
            page = roleService.roleList(name, code, page);
            logger.info("角色列表查询成功！");
            return ResponseEntity.ok("角色列表查询成功!").putData(page);
        } catch (Exception e) {
            logger.error("角色列表查询失败!");
            throw new BusinessException("角色列表查询失败!", e);
        }
    }

    // role信息
    @GetMapping("/info/{roleId}")
    public ResponseEntity roleInfo(@PathVariable("roleId") String roleId) {
        try {
            Role role = roleService.roleInfo(roleId);
            List<Record> roleRef = roleService.roleRefList(roleId, null);
            logger.info("角色信息查询成功！");
            return ResponseEntity.ok("角色信息查询成功!").putData(Record.jsonView(role, Role.InfoView.class).put("ref", roleRef));
        } catch (Exception e) {
            logger.error("角色信息查询失败!");
            throw new BusinessException("角色信息查询失败!", e);
        }
    }

    // 删除role
    @PostMapping("/delete/{roleId}")
    public ResponseEntity deleteRole(@PathVariable("roleId") String roleId) {
        try {
            roleService.deleteRole(roleId);
            logger.info("角色删除成功!");
            return ResponseEntity.ok("角色删除成功!");
        } catch (Exception e) {
            logger.error("角色删除失败!");
            throw new BusinessException("角色删除失败!", e);
        }
    }

    // 授权
    @PostMapping("/authc")
    public ResponseEntity authc(@RequestBody List<UserRole> refs) {
        try {
            roleService.authc(refs);
            logger.info("授权成功!");
            return ResponseEntity.ok("授权成功!");
        } catch (Exception e) {
            logger.error("授权失败!");
            throw new BusinessException("授权失败!", e);
        }
    }
}
