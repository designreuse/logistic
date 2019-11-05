package com.frame.core.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class DateUtil {

    private static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    private static final String FORMAT_DATETIME_NOS = "yyyy-MM-dd HH:mm";
    private static final String FORMAT_DATETIME_SLASH = "MM/dd/yy HH:mm:ss";
    private static final String FORMAT_DATETIME_SLASH_NOS = "MM/dd/yy HH:mm";
    private static final String FORMAT_DATE = "yyyy-MM-dd";
    private static final String FORMAT_DATE_SLASH = "MM/dd/yy";
    private static final String FORMAT_TIME = "HH:mm:ss";
    private static final String FORMAT_TIME_NOS = "HH:mm";

    public static String asDateTimeString(Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATETIME);
        return formatter.format(asLocalDateTime(date));
    }

    public static String asDateTimeString(TemporalAccessor date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATETIME);
        return formatter.format(date);
    }

    public static String asDateTimeNOSString(Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATETIME_NOS);
        return formatter.format(asLocalDateTime(date));
    }

    public static String asDateTimeNOSString(TemporalAccessor date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATETIME_NOS);
        return formatter.format(date);
    }

    public static String asDateTimeSlashString(Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATETIME_SLASH);
        return formatter.format(asLocalDateTime(date));
    }

    public static String asDateTimeSlashString(TemporalAccessor date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATETIME_SLASH);
        return formatter.format(date);
    }

    public static String asDateTimeSlashNOSString(Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATETIME_SLASH_NOS);
        return formatter.format(asLocalDateTime(date));
    }

    public static String asDateTimeSlashNOSString(TemporalAccessor date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATETIME_SLASH_NOS);
        return formatter.format(date);
    }

    public static String asDateString(Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
        return formatter.format(asLocalDateTime(date));
    }

    public static String asDateString(TemporalAccessor date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
        return formatter.format(date);
    }

    public static String asDateSlashString(Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE_SLASH);
        return formatter.format(asLocalDateTime(date));
    }

    public static String asDateSlashString(TemporalAccessor date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE_SLASH);
        return formatter.format(date);
    }

    public static String asTimeString(Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_TIME);
        return formatter.format(asLocalDateTime(date));
    }

    public static String asTimeString(TemporalAccessor date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_TIME);
        return formatter.format(date);
    }

    public static String asTimeNOSString(Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_TIME_NOS);
        return formatter.format(asLocalDateTime(date));
    }

    public static String asTimeNOSString(TemporalAccessor date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_TIME_NOS);
        return formatter.format(date);
    }

    public static String asPatternString(Date date,String pattern){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(asLocalDateTime(date));
    }

    public static String asPatternString(TemporalAccessor date,String pattern){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(date);
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(String dateTimeStr){
        return Date.from(LocalDateTime.parse(dateTimeStr).atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDate asLocalDate(String dateTimeStr) {
        return LocalDate.parse(dateTimeStr);
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime asLocalDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr);
    }
}