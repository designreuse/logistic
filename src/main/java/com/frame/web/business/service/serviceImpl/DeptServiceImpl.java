package com.frame.web.business.service.serviceImpl;

import com.logistics.core.shiro.ShiroUtil;
import com.logistics.core.sql.PageUtil;
import com.logistics.core.utils.EncryptUtil;
import com.logistics.web.dao.DeptDao;
import com.logistics.web.dao.UserDao;
import com.logistics.web.dao.pending.DeptPendingDao;
import com.logistics.web.entity.enumeration.Revision;
import com.logistics.web.entity.orgainzation.Dept;
import com.logistics.web.entity.pending.BasePend;
import com.logistics.web.entity.pending.DeptPend;
import com.logistics.web.service.DeptService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptDao deptDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private DeptPendingDao deptPendingDao;

    @Override
    public Dept saveOrUpdate(Dept.SaveDto dto) {
        dto.setRevision(Revision.ACTIVE.getInt());
        Dept dept = validateDept(dto);
        return deptDao.saveOrUpdate(dept);
    }

    @Override
    public Dept pendingDept(Dept.SaveDto dto){
        dto.setRevision(Revision.PENDING.getInt());
        Dept dept = validateDept(dto);
        deptDao.saveOrUpdate(dept);
        DeptPend deptPending = new DeptPend();
        deptPending.setId(EncryptUtil.randomUUID());
        deptPending.setDeptId(dept.getId());
        deptPending.setApplier(ShiroUtil.getCurrentUser().getAccount());
        deptPending.setOperation(BasePend.Operation.INSERT.toString());
        deptPending.setApplyTime(LocalDateTime.now());
        deptPendingDao.save(deptPending);
        return dept;
    }

    @Override
    public void deleteDept(String deptId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("dept_id", deptId);
        if (userDao.findAllCount(params) != 0) {
            throw new Exception("删除失败，该部门下有多个关联用户，请先转移用户");
        }
        deptDao.deleteById(deptId);
    }

    @Override
    public PageUtil getDeptList(String deptName,Revision revision, PageUtil page) {
        Map<String, Object> params = new HashMap<>();
        params.put("revision", revision.getInt());
        if (Strings.isNotEmpty(deptName)) {
            params.put("name", deptName);
        }
        return deptDao.findBySql(Dept.class, page, params);
    }

    @Override
    public PageUtil getPendList(String applier, PageUtil page){
        return deptPendingDao.pendList(applier,page);
    }

    @Override
    public Dept getDeptInfo(String deptId) {
        return deptDao.find(deptId);
    }

    @Override
    public void activeDept(String deptId, boolean resolve) {
        Dept dept = deptDao.findByIdAndRevision(deptId, Revision.PENDING.getInt());
        DeptPend deptPending = deptPendingDao.findByDeptId(deptId);
        deptPending.setApprover(ShiroUtil.getCurrentUser().getAccount());
        deptPending.setApproveTime(LocalDateTime.now());
        if (resolve) {
            deptPending.setResult(DeptPend.Result.RESOLVE.toString());
            dept.setRevision(Revision.ACTIVE.getInt());
            deptDao.saveOrUpdate(dept);
        } else {
            deptPending.setResult(DeptPend.Result.REJECT.toString());
            deptDao.deleteById(deptId);
        }
        deptPendingDao.save(deptPending);
    }

    private Dept validateDept(Dept.SaveDto dto){
        if (Strings.isEmpty(dto.getId())) {
            dto.setId(EncryptUtil.randomUUID());
            dto.setCreateTime(LocalDateTime.now());
        }
        dto.setUpdateTime(LocalDateTime.now());
        Dept dept = new Dept();
        BeanUtils.copyProperties(dto, dept);
        return dept;
    }

}
