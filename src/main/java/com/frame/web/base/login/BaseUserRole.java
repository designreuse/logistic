package com.frame.web.base.login;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseUserRole {
    @Id
    protected String id;
    /* 人员账号 */
    protected String account;
    /* 角色代码 */
    protected String role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
