package com.frame.web.business.entity.pending;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_user_pend")
public class UserPend extends BasePend {

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
