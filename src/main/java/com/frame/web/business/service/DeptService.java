package com.frame.web.business.service;

import com.frame.core.sql.Pager;
import com.frame.web.base.Enum.Revision;
import com.frame.web.business.entity.orgainzation.Dept;

public interface DeptService {

    Dept saveOrUpdate(Dept.SaveDto dto);

    Dept pendingDept(Dept.SaveDto dto);

    void deleteDept(String deptId) throws Exception;

    Pager getDeptList(String deptName, Revision revision, Pager page);

    Pager getPendList(String applier, Pager page);

    Dept getDeptInfo(String deptId);

    void activeDept(String deptId, boolean resolve);
}
