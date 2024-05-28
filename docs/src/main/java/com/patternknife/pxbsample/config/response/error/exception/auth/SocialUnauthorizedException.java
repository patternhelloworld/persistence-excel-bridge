package com.patternknife.pxbsample.config.response.error.exception.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// UNAUTHORIZED : 401
// FORBIDDEN : 403
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class SocialUnauthorizedException extends RuntimeException {
    public SocialUnauthorizedException(String message) {
        super(message);
    }
}