package com.dbrowser.server.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DBrowserException.class)
    public final ResponseEntity<Object> handlePendingChangeException(DBrowserException ex, WebRequest request) {
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body("Status: " + ex.getHttpStatus().toString() + " | Reason: " + ex.getMessage());
    }
}
