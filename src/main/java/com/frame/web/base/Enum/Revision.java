package com.frame.web.base.Enum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

public enum Revision {
    // 激活 , 待审核 , 失效的
    ACTIVE(1), PENDING(0), INVALID(-1);

    private Integer value;// 状态值

    Revision(Integer value) {
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

    @Converter
    public static class RevisionConverter implements AttributeConverter<Revision,Integer> {

        @Override
        public Integer convertToDatabaseColumn(Revision revision) {
            return revision.value;
        }

        @Override
        public Revision convertToEntityAttribute(Integer integer) {
            return getEnum(integer);
        }
    }
}
