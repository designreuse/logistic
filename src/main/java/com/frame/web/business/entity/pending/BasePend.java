package com.frame.web.business.entity.pending;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
public class BasePend implements Serializable {

    public static enum Operation {
        INSERT, DELETE
    }

    public static enum Result {
        RESOLVE, REJECT
    }

    @Id
    @Column(length = 32)
    protected String id;
    @Column(length = 6)
    protected String operation;
    @Column(length = 32)
    protected String applier;
    @Column(length = 32)
    protected String approver;
    @Column(length = 8)
    protected String result;
    protected LocalDateTime applyTime;
    protected LocalDateTime approveTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getApplier() {
        return applier;
    }

    public void setApplier(String applier) {
        this.applier = applier;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDateTime getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(LocalDateTime applyTime) {
        this.applyTime = applyTime;
    }

    public LocalDateTime getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(LocalDateTime approveTime) {
        this.approveTime = approveTime;
    }
}
