package com.java.exception;


public class StaffServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public StaffServiceException(String message) {
		super(message);
	}

}
