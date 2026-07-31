package com.project.bookingmanagement.exception;

public class ExternalServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExternalServiceException(String message) {
        super(message);
    }

    public ExternalServiceException(String serviceName, String message) {
        super(serviceName + " service error: " + message);
    }

    public ExternalServiceException(String serviceName,
                                    String message,
                                    Throwable cause) {
        super(serviceName + " service error: " + message, cause);
    }
}
