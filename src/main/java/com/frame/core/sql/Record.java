package com.frame.core.sql;


import com.frame.core.utils.CommonUtil;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Record extends LinkedHashMap<String, Object> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 保留指定key
     *
     * @param keys
     */
    public Record Retention(String... keys) {
        this.keySet().stream().filter(key -> !Arrays.asList(keys).contains(key))
                .collect(Collectors.toList())
                .forEach(retentKey -> this.remove(retentKey));
        return this;
    }

    public Object get(String key, Object defaultValue) {
        return this.get(key) == null ? defaultValue : this.get(key);
    }

    public void set(String key, Object value) {
        this.remove(key);
        this.put(key, value);
    }

    public void removes(String... keys) {
        try {
            for (int i = 0; i < keys.length; i++) {
                this.remove(keys[i]);
            }
        } catch (Exception ex) {
        }
    }

    /**
     * 自定义获取字段名称并保存成record(只支持一般对象和支持map)
     *
     * @param o
     * @param fields 保留的字段名
     * @return
     */
    public static Record customOutputField(Object o, String... fields) {
        Record record = new Record();
        for (String field : fields) {
            record.set(field, CommonUtil.getAttribute(o, field));
        }
        return record;
    }

    /**
     * 自定义获取字段名称并保存成record(只支持一般对象和支持map)
     *
     * @param o
     * @param fields Map<保留的字段名，别名>
     * @return
     */
    public static Record customOutputField(Object o, Map<String, String> fields) {
        Record record = new Record();
        for (String field : fields.keySet()) {
            record.set(fields.get(field), CommonUtil.getAttribute(o, field));
        }
        return record;
    }

    /**
     * 根据自定义注解返回record
     * @param bean 需要过滤的对象
     * @param anno 过滤字段的注解
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T, D> Record jsonView(T bean, Class<D> anno) {
        if (bean == null) return null;
        try {
            Record record = new Record();
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                View view;
                if ((view = field.getAnnotation(View.class)) != null) {
                    if (Arrays.stream(view.value()).anyMatch(c -> c == anno)) {
                        record.put(field.getName(), field.get(bean));
                    }
                }
            }
            return record;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface View {
        Class<?>[] value() default {};
    }
}
