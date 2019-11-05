package com.frame.core.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(value = {BusinessException.class})
    public ResponseEntity defaultErrorHandler(Exception ex) {
        return ResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage(), ex.getCause().getMessage());
    }
}
