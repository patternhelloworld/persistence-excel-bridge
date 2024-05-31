package io.github.patternknife.pxbsample.domain.admin.exception;

import io.github.patternknife.pxbsample.config.logger.dto.ErrorMessages;
import io.github.patternknife.pxbsample.config.response.error.exception.auth.UnauthenticatedException;
import io.github.patternknife.pxbsample.config.response.error.message.SecurityExceptionMessage;

public class PasswordFailedExceededException extends UnauthenticatedException {
    public PasswordFailedExceededException() {
        super(SecurityExceptionMessage.PASSWORD_FAILED_EXCEEDED.getMessage());
    }

    public PasswordFailedExceededException(String message) {
        super(message);
    }

    public PasswordFailedExceededException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordFailedExceededException(ErrorMessages errorMessages) {
        super(errorMessages);
    }
}
