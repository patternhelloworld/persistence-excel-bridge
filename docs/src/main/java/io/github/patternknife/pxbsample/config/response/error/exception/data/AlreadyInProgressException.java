package io.github.patternknife.pxbsample.config.response.error.exception.data;


public class AlreadyInProgressException extends RuntimeException {
    public AlreadyInProgressException(String message) {
        super(message);
    }
}
