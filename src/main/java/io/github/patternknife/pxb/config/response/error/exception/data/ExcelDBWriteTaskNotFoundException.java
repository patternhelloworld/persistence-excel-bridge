package io.github.patternknife.pxb.config.response.error.exception.data;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ExcelDBWriteTaskNotFoundException extends ResourceNotFoundException {
	public ExcelDBWriteTaskNotFoundException() {
	}
	public ExcelDBWriteTaskNotFoundException(String message) {
		super(message);
	}

}
