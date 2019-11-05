package com.frame.web.base.login;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
public class BaseUser implements Serializable {
    /* 用户id */
    @Id
    protected String id;
    /* 用户名 */
    protected String account;
    /* 密码 */
    protected String password;
    /* 密码盐 */
    protected String salt;
    /* 是否已同步 -1:不能同步,0:未同步,1:已同步 */
    protected Integer sync;
    /* 数据状态 -1:不可以,0:待审核,1:已激活 */
    protected Integer revision;
    /* 创建时间 */
    protected LocalDateTime createTime;
    /* 更新时间 */
    protected LocalDateTime updateTime;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getSync() {
        return sync;
    }

    public void setSync(Integer sync) {
        this.sync = sync;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
