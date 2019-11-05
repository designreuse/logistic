package com.frame.web.business.controller;

import com.logistics.core.http.BusinessException;
import com.logistics.core.http.ResponseEntity;
import com.logistics.core.shiro.ShiroUtil;
import com.logistics.core.sql.PageUtil;
import com.logistics.web.entity.enumeration.Revision;
import com.logistics.web.entity.orgainzation.Dept;
import com.logistics.web.entity.pending.BasePend;
import com.logistics.web.service.DeptService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dept")
public class DeptController {

    Logger logger = LoggerFactory.getLogger(DeptController.class);

    @Autowired
    private DeptService deptService;

    @RequiresRoles(value = {"admin", "manager"}, logical = Logical.OR)
    @GetMapping("/info/{deptId}")
    public ResponseEntity viewDept(@PathVariable("deptId") String deptId) {
        try {
            Dept dept = deptService.getDeptInfo(deptId);
            logger.info("部门查询成功！");
            return ResponseEntity.ok().putData(dept);
        } catch (Exception e) {
            logger.error("部门查询失败！", e);
            throw new BusinessException("用户查询失败", e);
        }
    }

    @RequiresRoles(value = {"admin", "manager"}, logical = Logical.OR)
    @PostMapping("/getDeptList")
    public ResponseEntity deptList(@RequestBody Dept.searchDto dto) {
        try {
            PageUtil pageUtil = new PageUtil(dto.getPageIndex(), dto.getPageSize());
            pageUtil = deptService.getDeptList(dto.getName(), Revision.ACTIVE, pageUtil);
            logger.info("部门列表查询成功！");
            return ResponseEntity.ok().putData(pageUtil);
        } catch (Exception e) {
            logger.error("部门列表查询失败！", e);
            throw new BusinessException("部门列表查询失败！", e);
        }
    }

    @RequiresRoles(value = {"principal"})
    @GetMapping("/customDept")
    public ResponseEntity customDept() {
        try {
            String deptId = ShiroUtil.getCurrentUser().getDeptId();
            Dept dept = deptService.getDeptInfo(deptId);
            logger.info("部门查询成功！");
            return ResponseEntity.ok().putData(dept);
        } catch (Exception e) {
            logger.error("部门查询失败！", e);
            throw new BusinessException("部门查询失败！", e);
        }
    }

    @RequiresRoles(value = {"admin", "manager"}, logical = Logical.OR)
    @PostMapping("/listForPendApprove")
    public ResponseEntity listForPendApprove(@RequestBody Dept.searchDto dto) {
        try {
            PageUtil pageUtil = new PageUtil(dto.getPageIndex(), dto.getPageSize());
            pageUtil = deptService.getDeptList(dto.getName(), Revision.PENDING, pageUtil);
            logger.info("部门列表查询成功！");
            return ResponseEntity.ok().putData(pageUtil);
        } catch (Exception e) {
            logger.error("部门列表查询失败！", e);
            throw new BusinessException("部门列表查询失败！", e);
        }
    }

    @RequiresRoles(value = {"principal"})
    @PostMapping("/listForPendApply")
    public ResponseEntity listForPendApply(@RequestBody Dept.searchDto dto) {
        try {
            PageUtil pageUtil = new PageUtil(dto.getPageIndex(), dto.getPageSize());
            String account = ShiroUtil.getCurrentUser().getAccount();
            pageUtil = deptService.getPendList(account, pageUtil);
            logger.info("用户申请列表查询成功！");
            return ResponseEntity.ok("用户申请列表查询成功！").putData(pageUtil);
        } catch (Exception e) {
            logger.info("用户申请列表查询失败！", e);
            throw new BusinessException("用户申请列表查询失败！", e);
        }
    }

    @RequiresRoles(value = {"admin"})
    @PostMapping("/delete/{deptId}")
    public ResponseEntity deleteDept(@PathVariable("deptId") String deptId) {
        try {
            deptService.deleteDept(deptId);
            logger.info("部门删除成功！");
            return ResponseEntity.ok();
        } catch (Exception e) {
            logger.error("部门删除失败！", e);
            throw new BusinessException("部门删除失败", e);
        }
    }

    @RequiresRoles(value = {"admin", "manager"}, logical = Logical.OR)
    @PostMapping("/saveDept")
    public ResponseEntity saveDept(@RequestBody Dept.SaveDto dto) {
        try {
            deptService.saveOrUpdate(dto);
            logger.info("部门：" + dto.getName() + "保存成功！");
            return ResponseEntity.ok("部门：" + dto.getName() + "保存成功！");
        } catch (Exception e) {
            logger.error("部门保存失败！", e);
            throw new BusinessException("部门保存失败", e);
        }
    }

    @RequiresRoles(value = {"principal"})
    @PostMapping("/pendDept")
    public ResponseEntity pendDept(@RequestBody Dept.SaveDto dto) {
        try {
            deptService.saveOrUpdate(dto);
            logger.info("部门：" + dto.getName() + "提交成功！");
            return ResponseEntity.ok("部门：" + dto.getName() + "提交成功！");
        } catch (Exception e) {
            logger.error("部门提交失败！", e);
            throw new BusinessException("部门提交失败", e);
        }
    }

    @RequiresRoles(value = {"admin", "manager"}, logical = Logical.OR)
    @PostMapping("/activeDept")
    public ResponseEntity activeDept(@Param("deptId") String deptId, @Param("result") String result) {
        try {
            if (BasePend.Result.RESOLVE.toString().equals(result)) {
                deptService.activeDept(deptId, true);
                logger.info("部门申请审核通过！");
            } else {
                deptService.activeDept(deptId, false);
                logger.info("部门申请驳回！");
            }
            return ResponseEntity.ok("部门申请已审核");
        } catch (Exception e) {
            throw new BusinessException("部门申请审核失败!", e);
        }
    }
}
