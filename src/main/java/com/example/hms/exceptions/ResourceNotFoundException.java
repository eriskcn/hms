package com.example.hms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.NonNull;
import org.springframework.web.ErrorResponse;

public class ResourceNotFoundException extends RuntimeException implements ErrorResponse {
    private final ProblemDetail body;

    public ResourceNotFoundException(String message) {
        super(message);
        this.body = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
    }

    @Override
    @NonNull
    public HttpStatusCode getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    @NonNull
    public ProblemDetail getBody() {
        return body;
    }
}