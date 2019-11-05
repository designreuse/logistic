package com.frame.web.business.entity.orgainzation;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@DynamicInsert
@Entity
@Table(name = "t_dept")
public class Dept implements Serializable {
    /* 部门id */
    @Id
    @Column(length=32)
    private String id;
    /* 部门名称 */
    @Column(length = 26)
    private String name;
    /* 统一社会信用代码 */
    @Column(length = 18)
    private String creditCode;
    /* 营业执照 url*/
    @Column
    private String certificate;
    /* 企业负责人id */
    @Column(length = 32)
    private String principal;
    /* 修订 */
    private Integer revision;
    /* 创建时间 */
    private LocalDateTime createTime;
    /* 更新时间 */
    private LocalDateTime updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public static class SaveDto extends Dept{
        private String signCode;

        public String getSignCode() {
            return signCode;
        }

        public void setSignCode(String signCode) {
            this.signCode = signCode;
        }
    }

    public static class searchDto{
        private Integer pageIndex;
        private Integer pageSize;
        private String name;

        public Integer getPageIndex() {
            return pageIndex;
        }

        public void setPageIndex(Integer pageIndex) {
            this.pageIndex = pageIndex;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
