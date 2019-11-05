package com.frame.core.sql;

import com.frame.core.utils.CommonUtil;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

public class AliasToRecordTransformer extends AliasedTupleSubsetResultTransformer {


    /**
     * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
     */

    private static final long serialVersionUID = 1L;
    public static final AliasToRecordTransformer INSTANCE = new AliasToRecordTransformer();

    /**
     * Disallow instantiation of AliasToEntityMapResultTransformer.
     */
    private AliasToRecordTransformer() {
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Record result = new Record();
        for (int i = 0; i < tuple.length; i++) {
            String alias = aliases[i];
            if (alias != null) {
                result.put(CommonUtil.underline2Camel(alias), tuple[i]);
            }
        }
        return result;
    }

    @Override
    public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
        return false;
    }

    /**
     * Serialization hook for ensuring singleton uniqueing.
     *
     * @return The singleton instance : {@link #INSTANCE}
     */
    private Object readResolve() {
        return INSTANCE;
    }
}
