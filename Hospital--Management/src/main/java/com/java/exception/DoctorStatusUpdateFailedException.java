package com.java.exception;


public class DoctorStatusUpdateFailedException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public DoctorStatusUpdateFailedException(String message) {
		super(message);
	}

}
