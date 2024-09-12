package com.example.hms.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public class ServiceException extends RuntimeException implements ErrorResponse {
    private final ProblemDetail body;

    public ServiceException(String message, HttpStatusCode status) {
        super(message);
        this.body = ProblemDetail.forStatusAndDetail(status, message);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatusCode.valueOf(body.getStatus());
    }

    @Override
    public ProblemDetail getBody() {
        return body;
    }
}