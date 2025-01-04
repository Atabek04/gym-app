package com.epam.gym.authservice.exception;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import reactor.util.annotation.NonNull;

public class CustomResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(@NonNull ClientHttpResponse response) {
        return false;   // for preventing exception throwing on any status code
    }

    @Override
    public void handleError(@NonNull ClientHttpResponse response) {
        // intentionally left blank to prevent exception throwing
    }
}
