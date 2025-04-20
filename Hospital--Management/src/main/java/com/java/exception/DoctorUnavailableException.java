package com.java.exception;

public class DoctorUnavailableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DoctorUnavailableException(String message) {
		super(message);
	}

}