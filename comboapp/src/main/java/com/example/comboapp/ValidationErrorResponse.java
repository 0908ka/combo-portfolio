package com.example.comboapp;

import java.util.List;

public class ValidationErrorResponse {

	
	private int status;
	private List<ValidationError> errors;
	
	public ValidationErrorResponse(int status, List<ValidationError> errors) {
		this.status = status;
		this.errors = errors;		
	}

	public int getStatus() {
		return status;
	}

	public List<ValidationError> getErrors() {
		return errors;
	}

}
