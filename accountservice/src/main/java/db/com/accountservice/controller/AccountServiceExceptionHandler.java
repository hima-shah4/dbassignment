package db.com.accountservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;

import db.com.accountservice.vo.error.AccountNotFoundException;
import db.com.accountservice.vo.error.UserNotFoundException;

@ControllerAdvice
public class AccountServiceExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(AccountServiceExceptionHandler.class);
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleAllExceptions(Exception ex, WebRequest request) {
        logger.error("Exception occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	@ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<String> handleHttpStatusException(HttpStatusCodeException ex, WebRequest request) {
		logger.error("Exception occured : {}", ex.getMessage(), ex);
		return new ResponseEntity<>(ex.getMessage(), ex.getStatusCode());
    }
	
	@ExceptionHandler(AccountNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<String> handleAccountNotFoundException(HttpStatusCodeException ex, WebRequest request) {
		logger.error("Account Not Found : {}", ex.getMessage(), ex);
		return new ResponseEntity<>(ex.getMessage(), ex.getStatusCode());
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<String> handleUserNotFoundException(HttpStatusCodeException ex, WebRequest request) {
		logger.error("User Not Found : {}", ex.getMessage(), ex);
		return new ResponseEntity<>(ex.getMessage(), ex.getStatusCode());
	}
}
