package com.frame.web.business.entity.pending;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_dept_pend")
public class DeptPend extends BasePend {

    private String deptId;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
}
