package com.chat.app.controller;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Centralizes mapping of common exceptions to HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Converts validation and business-rule failures into 400 responses.
     *
     * @param ex thrown exception
     * @return error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleIllegalArgument(IllegalArgumentException ex) {
        return ex.getMessage();
    }

    /**
     * Converts missing-resource exceptions into 404 responses.
     *
     * @param ex thrown exception
     * @return error message
     */
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleNotFound(NoSuchElementException ex) {
        return ex.getMessage();
    }
}
