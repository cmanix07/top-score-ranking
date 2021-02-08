package com.rakuten.redi.controller;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.rakuten.redi.security.config.ApiError;
import com.rakuten.redi.service.utils.RediException;

@Component
@ControllerAdvice
public class ApplicationErrorController extends ResponseEntityExceptionHandler {
	private static Logger log = LoggerFactory.getLogger(ApplicationErrorController.class);

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = "Invalid syntax for this request was provided.";
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "GE-001", error, ex));
	}

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@ExceptionHandler(RediException.class)
	protected ResponseEntity<Object> handleRediException(RediException ex) {

		log.info("Error occurred: {}", ex.getErrorMessage());

		ApiError apiError = new ApiError(ex.getStatus());
		apiError.setCode(ex.getErrorCode());
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(RuntimeException.class)
	protected ResponseEntity<Object> handleRuntimeServerException(RuntimeException ex) {
		log.error("Error occurred: {}", ex);

		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
		apiError.setCode("500");
		apiError.setMessage("Unexpected internal server error. Please try after sometime.");
		return buildResponseEntity(apiError);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		Set<String> errors = new HashSet<>();
		ex.getBindingResult().getFieldErrors().stream().forEach(error -> 
			errors.add(error.getDefaultMessage())
		);
		String errorMessage = String.join(", ", errors);
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setCode("400");
		apiError.setMessage(errorMessage);
		return buildResponseEntity(apiError);
	}

}
