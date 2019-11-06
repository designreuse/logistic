package com.frame.web.business.entity.ref;

import com.frame.web.base.login.BaseUserRole;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="r_user_role")
public class UserRole extends BaseUserRole {
    @Transient
    private String rule;

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}
