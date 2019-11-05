package com.frame.web.business.entity;

import com.frame.web.base.login.BaseUser;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="t_user")
public class User extends BaseUser {
    private String name;
    private Integer sex;
    private String mobile;
    private Integer age;
    private String deptId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
}
