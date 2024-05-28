package com.patternknife.pxbsample.config.response.error.exception.push;

public class PushException extends RuntimeException {
    public PushException(String message) {
        super(message);
    }
    public PushException(String message, Throwable cause) {
        super(message, cause);
    }
}
