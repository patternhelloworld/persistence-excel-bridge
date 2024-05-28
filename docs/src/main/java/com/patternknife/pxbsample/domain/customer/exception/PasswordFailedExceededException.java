package com.patternknife.pxbsample.domain.customer.exception;

import com.patternknife.pxbsample.config.response.error.message.GeneralErrorMessage;
import lombok.Getter;

@Getter
public class PasswordFailedExceededException extends RuntimeException {

    private GeneralErrorMessage generalErrorMessage;

    public PasswordFailedExceededException() {
        this.generalErrorMessage = GeneralErrorMessage.PASSWORD_FAILED_EXCEEDED;
    }
}
