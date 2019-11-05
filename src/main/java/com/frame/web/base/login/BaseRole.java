package com.frame.web.base.login;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public class BaseRole {
    /* 角色id */
    @Id
    protected String id;
    /* 角色代码 */
    protected String code;
    /* 角色名称 */
    protected String name;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
