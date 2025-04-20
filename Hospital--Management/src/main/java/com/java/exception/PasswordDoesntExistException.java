package com.java.exception;


public class PasswordDoesntExistException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public PasswordDoesntExistException(String message) {
		super(message);
	}

}
