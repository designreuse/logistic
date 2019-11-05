package com.frame.web.base.Enum;

public enum Sync {
    // 已同步 , 待同步 , 不能同步
    ACTIVE(1), PENDING(0), INVALID(-1);

    private Integer value;// 状态值

    Sync(Integer value) {
        this.value = value;
    }

    public Integer getInt() {
        return this.value;
    }

    public static Revision getEnum(Integer value){
        for(Revision revision : Revision.values()){
            if(revision.getInt()==value){
                return revision;
            }
        }
        return null;
    }
}
