package com.example.comboapp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidationExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex){
		
		List<ValidationError> errors = ex.getBindingResult().getFieldErrors()
				.stream()
				.map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
				.collect(Collectors.toList());
		
		ValidationErrorResponse response = new ValidationErrorResponse(400, errors);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

}
