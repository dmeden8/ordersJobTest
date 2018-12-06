package com.jobtest.orders.error;

public class ValidationException extends RuntimeException {
	
	private static final long serialVersionUID = 1703154479583554317L;

	public ValidationException() {}

	public ValidationException(String message) {
        super(message);
    }

}
