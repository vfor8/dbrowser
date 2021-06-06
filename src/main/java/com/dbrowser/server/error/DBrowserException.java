package com.dbrowser.server.error;

import org.springframework.http.HttpStatus;

public class DBrowserException extends RuntimeException {

    private HttpStatus httpStatus;

    public DBrowserException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
