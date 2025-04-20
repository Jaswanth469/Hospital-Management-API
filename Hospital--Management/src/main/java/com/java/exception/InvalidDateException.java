package com.java.exception;


public class InvalidDateException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public InvalidDateException(String message) {
		super(message);
	}

}
