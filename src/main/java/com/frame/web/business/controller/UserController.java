package com.frame.web.business.controller;

import com.frame.core.http.BusinessException;
import com.frame.core.http.ResponseEntity;
import com.frame.core.shiro.JWTUtil;
import com.frame.core.shiro.ShiroUtil;
import com.frame.core.sql.Pager;
import com.frame.web.base.Enum.Revision;
import com.frame.web.business.entity.orgainzation.User;
import com.frame.web.business.entity.pending.BasePend;
import com.frame.web.business.service.UserService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 管理员查询用户列表
     *
     * @param dto
     * @return
     */
    @RequiresRoles(value = {"admin", "manager"}, logical = Logical.OR)
    @PostMapping("/listForAdmin")
    public ResponseEntity listForAdmin(@RequestBody User.SearchDto dto) {
        try {
            Pager Pager = new Pager(dto.getPageIndex(), dto.getPageSize());
            Pager = userService.getUserList(dto.getName(), dto.getName(), dto.getDeptId(), Revision.ACTIVE.getInt(), Pager);
            logger.info("用户列表查询成功！");
            return ResponseEntity.ok().putData(Pager);
        } catch (Exception e) {
            logger.info("用户列表查询失败！", e);
            throw new BusinessException("用户列表查询失败！", e);
        }
    }

    /**
     * 管理员查询用户列表
     *
     * @param dto
     * @return
     */
    @RequiresRoles(value = {"admin", "manager"}, logical = Logical.OR)
    @PostMapping("/listForPendApprove")
    public ResponseEntity listForPendApprove(@RequestBody User.SearchDto dto) {
        try {
            Pager Pager = new Pager(dto.getPageIndex(), dto.getPageSize());
            Pager = userService.getUserList(dto.getName(), dto.getName(), dto.getDeptId(), Revision.PENDING.getInt(), Pager);
            logger.info("用户列表查询成功！");
            return ResponseEntity.ok().putData(Pager);
        } catch (Exception e) {
            logger.info("用户列表查询失败！", e);
            throw new BusinessException("用户列表查询失败！", e);
        }
    }

    /**
     * 企业负责人查询用户列表
     *
     * @param dto
     * @return
     */
    @RequiresRoles({"principal"})
    @PostMapping("/listForCo")
    public ResponseEntity getUserList(@RequestBody User.SearchDto dto) {
        try {
            Pager Pager = new Pager(dto.getPageIndex(), dto.getPageSize());
            String deptId = ((User)ShiroUtil.getCurrentUser()).getDeptId();
            Pager = userService.getUserList(dto.getName(), dto.getMobile(), deptId, Revision.ACTIVE.getInt(), Pager);
            logger.info("用户列表查询成功！");
            return ResponseEntity.ok().putData(Pager);
        } catch (Exception e) {
            logger.info("用户列表查询失败！", e);
            throw new BusinessException("用户列表查询失败！", e);
        }
    }

    /**
     * 企业负责人查询用户申请列表
     *
     * @param dto
     * @return
     */
    @RequiresRoles({"principal"})
    @PostMapping("/listForPendApply")
    public ResponseEntity listForPendApply(@RequestBody User.SearchDto dto) {
        try {
            Pager Pager = new Pager(dto.getPageIndex(), dto.getPageSize());
            String account = ShiroUtil.getCurrentUser().getAccount();
            Pager = userService.getPendList(account, Pager);
            logger.info("用户申请列表查询成功！");
            return ResponseEntity.ok("用户申请列表查询成功！").putData(Pager);
        } catch (Exception e) {
            logger.info("用户申请列表查询失败！", e);
            throw new BusinessException("用户申请列表查询失败！", e);
        }
    }

    @RequiresRoles(value={"admin", "manager"},logical = Logical.OR)
    @PostMapping("/activeUser")
    public ResponseEntity activeUser(@RequestBody Map<String,Object> params) {
        try {
            if (BasePend.Result.RESOLVE.toString().equals(params.get("result"))) {
                userService.activeUser(params.get("account").toString(), true);
                logger.info("用户申请审核通过！");
            } else {
                userService.activeUser(params.get("account").toString(), false);
                logger.info("用户申请驳回！");
            }
            return ResponseEntity.ok("用户申请已审核");
        } catch (Exception e) {
            throw new BusinessException("用户申请审核失败!", e);
        }
    }
    
    @GetMapping("/getUserInfo")
    public ResponseEntity getUserInfo(@Param("token") String token) {
        try {
            User user = userService.getUserByAccount(JWTUtil.getAccount(token));
            logger.info("用户查询成功！");
            return ResponseEntity.ok().putData(user);
        } catch (Exception e) {
            logger.info("用户查询失败！", e);
            throw new BusinessException("用户信息查询失败！", e);
        }
    }

    @RequiresRoles({"admin"})
    @PostMapping("/saveUser")
    public ResponseEntity saveUser(@RequestBody User.SaveDto user) {
        try {
            userService.save(user);
            logger.info("用户：" + user.getAccount() + "保存成功！");
            return ResponseEntity.ok("用户保存成功").putData(user);
        } catch (Exception e) {
            logger.error("用户保存失败！", e);
            throw new BusinessException("用户保存失败", e);
        }
    }

    @RequiresRoles(value = {"principal", "manager"}, logical = Logical.OR)
    @PostMapping("/pendingUser")
    public ResponseEntity pendingUser(@RequestBody User.SaveDto user) {
        try {
            userService.pending(user);
            logger.info("用户：" + user.getAccount() + "保存成功！");
            return ResponseEntity.ok("用户保存成功").putData(user);
        } catch (Exception e) {
            logger.error("用户保存失败！", e);
            throw new BusinessException("用户保存失败", e);
        }
    }

    @RequiresRoles(value = {"admin"}, logical = Logical.OR)
    @PostMapping("/deleteUser/{userId}")
    public ResponseEntity deleteUser(@PathVariable String userId) {
        try {
            userService.deleteUser(userId);
            logger.info("用户删除成功！");
            return ResponseEntity.ok();
        } catch (Exception e) {
            logger.error("用户删除失败！", e);
            throw new BusinessException("用户删除失败", e);
        }
    }

    @GetMapping("/info/{userId}")
    public ResponseEntity viewUser(@PathVariable("userId") String userId) {
        try {
            User user = userService.getUserById(userId);
            logger.info("用户查询成功！");
            return ResponseEntity.ok().putData(user);
        } catch (Exception e) {
            logger.error("用户查询失败！", e);
            throw new BusinessException("用户查询失败", e);
        }
    }
}
