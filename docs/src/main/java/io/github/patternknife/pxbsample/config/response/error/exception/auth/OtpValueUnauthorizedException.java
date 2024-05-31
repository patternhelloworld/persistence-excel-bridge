package io.github.patternknife.pxbsample.config.response.error.exception.auth;


public class OtpValueUnauthorizedException extends RuntimeException {
    public OtpValueUnauthorizedException(String message) {
        super(message);
    }
}