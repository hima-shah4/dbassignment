package com.db.emailservice.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidMessageException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public InvalidMessageException(String message) {
		super(message);
	}
}
