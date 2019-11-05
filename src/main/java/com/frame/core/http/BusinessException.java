package com.frame.core.http;

public class BusinessException extends RuntimeException {
    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message, new Throwable(message));
    }

    public BusinessException(String message, Exception e) {
        super(message, getRootCause(e.getCause() == null ? new Throwable(e.getMessage()) : e.getCause()));
    }

    public BusinessException(String message, Throwable cause) {
        super(message, getRootCause(cause == null ? new Throwable(message) : cause));
    }

    public BusinessException(String message, String cause) {
        super(message, new Throwable(cause));
    }

    public BusinessException(Throwable cause) {
        super(getRootCause(cause));
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, getRootCause(cause), enableSuppression, writableStackTrace);
    }

    private static Throwable getRootCause(Throwable cause) {
        if (cause == null) return null;
        Throwable e = cause;
        while ((cause = cause.getCause()) != null) {
            e = cause;
        }
        return e;
    }
}