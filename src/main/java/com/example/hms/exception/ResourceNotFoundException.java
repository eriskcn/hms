package com.example.hms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public class ResourceNotFoundException extends RuntimeException implements ErrorResponse {
    private final ProblemDetail body;

    public ResourceNotFoundException(String message) {
        super(message);
        this.body = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public ProblemDetail getBody() {
        return body;
    }
}