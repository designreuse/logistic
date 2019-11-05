package com.frame.web.business.entity;

import com.frame.web.base.login.BaseUserRole;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="r_user_role")
public class UserRole extends BaseUserRole {
}
