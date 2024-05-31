package io.github.patternknife.pxbsample.config.response.error.exception.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UserDeletedException extends AccessDeniedException {
    public UserDeletedException(String message) {
        super(message);
    }
}