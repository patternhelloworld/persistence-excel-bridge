package com.patternknife.pxbsample.domain.customer.exception;

import com.patternknife.pxbsample.domain.customer.entity.Email;
import lombok.Getter;

@Getter
public class EmailDuplicationException extends RuntimeException {

    private Email email;
    private String field;

  public EmailDuplicationException(Email email) {
        this.field = "email";
        this.email = email;
    }
}
