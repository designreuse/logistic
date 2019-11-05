package com.frame.core.utils;


import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredFields;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;

public class CommonUtil {

    /**
     * 根据字符串字段名获取对象相应值，只支持map与一般对象
     *
     * @param o
     * @param field
     * @return
     */
    public static Object getAttribute(Object o, String field) {
        Object r = "";
        try {
            if (o instanceof Map) return ((Map) o).get(field);
            Field f = o.getClass().getDeclaredField(field);
            f.setAccessible(true);
            r = f.get(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 根据字符串字段名设置对象对应值，只支持map与一般对象
     *
     * @param o
     * @param field
     * @param keyWord
     */
    public static void setAttribute(Object o, String field, Object keyWord) {
        try {
            if (o instanceof Map) {
                ((Map) o).put(field, keyWord);
                return;
            }
            Field f = o.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(o, keyWord);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 驼峰转下划线
     *
     * @param desk
     * @return
     */
    public static String camel2Underline(String desk) {
        StringBuilder builder = new StringBuilder();
        boolean lower = false;
        char[] deskArr = desk.toCharArray();
        for (int i = 0; i < deskArr.length; i++) {
            if (Character.isLowerCase(deskArr[i]) || Character.isDigit(deskArr[i])) {
                lower = true;
                builder.append(deskArr[i]);
            } else if (Character.isUpperCase(deskArr[i]) && lower) {
                lower = false;
                builder.append("_").append(Character.toLowerCase(deskArr[i]));
            } else {
                lower = false;
                builder.append(deskArr[i]);
            }
        }
        return builder.toString().toLowerCase();
    }

    /**
     * 下划线转驼峰
     *
     * @param desk
     * @return
     */
    public static String underline2Camel(String desk) {
        if (desk.indexOf("_") == -1) {
            return desk;
        }
        StringBuilder builder = new StringBuilder();
        boolean change = false;
        char[] deskArr = desk.toCharArray();
        for (int i = 0; i < deskArr.length; i++) {
            if (deskArr[i] == '_') {
                change = true;
            } else if (Character.isDigit(deskArr[i]) && change) {
                change = false;
                builder.append("_").append(deskArr[i]);
            } else if (deskArr[i] != '_' && change) {
                change = false;
                builder.append(Character.toUpperCase(deskArr[i]));
            } else {
                builder.append(deskArr[i]);
            }
        }
        return builder.toString();
    }

    /**
     * 通过对象自定义task
     * @param task 如何实现
     * @param obj 实体类对象
     * @param during 多少时间/秒
     * @param <T>
     */
    public static <T> void timerTask(Consumer<T> task,T obj,long during){
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                task.accept(obj);
                timer.cancel();
            }
        },during*1000);
    }

    /**
     * 获取N位验证码
     * @param n
     * @return
     */
    public static int signCode(int n){
        return n>0?(int)((Math.random()*9+1)*Math.pow(10,n-1)):0;
    }

    /**
     * 获取包含父类以内的所有字段
     * @param entityClass
     * @return
     */
    public static List<Field> getDeclaredFields(Class entityClass){
        List<Field> fieldList = new ArrayList<>();
        Class nowCalss = entityClass;
        while (nowCalss!=null) {
            fieldList.addAll(Arrays.asList(nowCalss.getDeclaredFields()));
            nowCalss = nowCalss.getSuperclass();
        }
        return fieldList;
    }
}
