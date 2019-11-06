package com.frame.web.business.entity.orgainzation;

import com.frame.web.base.login.BaseRole;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="t_role")
public class Role extends BaseRole {

    public interface InfoView {
    }
}
