package io.github.patternknife.pxbsample.config.response.error.exception.data;

import io.github.patternknife.pxbsample.config.logger.dto.ErrorMessages;
import io.github.patternknife.pxbsample.config.response.error.exception.ErrorMessagesContainedException;

public class CustomIllegalStateException extends ErrorMessagesContainedException {
    public CustomIllegalStateException() {
    }

    public CustomIllegalStateException(String message) {
        super(message);
    }

    public CustomIllegalStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomIllegalStateException(ErrorMessages errorMessages) {
        super(errorMessages);
    }
}
