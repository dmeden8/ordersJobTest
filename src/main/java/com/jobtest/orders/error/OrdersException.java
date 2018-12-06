package com.jobtest.orders.error;

public class OrdersException extends RuntimeException {
	
	private static final long serialVersionUID = 3789252423033362059L;
	
	public OrdersException() {}

	public OrdersException(String message) {
        super(message);
    }
	
}
