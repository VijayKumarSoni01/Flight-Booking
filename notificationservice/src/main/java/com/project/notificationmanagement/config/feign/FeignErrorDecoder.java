package com.project.notificationmanagement.config.feign;

import com.project.notificationmanagement.exception.RemoteServiceException;

import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {

        String message = switch (response.status()) {

            case 400 -> "Bad request while calling remote service.";

            case 401 -> "Unauthorized access to remote service.";

            case 403 -> "Access denied by remote service.";

            case 404 -> "Requested resource not found in remote service.";

            case 409 -> "Conflict occurred in remote service.";

            case 500 -> "Remote service encountered an internal server error.";

            case 503 -> "Remote service is currently unavailable.";

            default -> null;
        };

        if (message != null) {
            return new RemoteServiceException(
                    response.status(),
                    methodKey,
                    message);
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }
}