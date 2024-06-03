package io.github.patternknife.pxb.config.response.error.exception.data;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ExcelDBReadTaskNotFoundException extends ResourceNotFoundException {
	public ExcelDBReadTaskNotFoundException() {
	}
	public ExcelDBReadTaskNotFoundException(String message) {
		super(message);
	}

}
