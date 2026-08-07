package com.project.notificationmanagement.exception;

public class RemoteServiceException extends RuntimeException {

    private final int statusCode;

    private final String serviceMethod;

    public RemoteServiceException(
            int statusCode,
            String serviceMethod,
            String message) {

        super(message);
        this.statusCode = statusCode;
        this.serviceMethod = serviceMethod;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getServiceMethod() {
        return serviceMethod;
    }
}