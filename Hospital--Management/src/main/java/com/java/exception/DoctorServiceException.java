package com.java.exception;

public class DoctorServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DoctorServiceException(String message) {
        super(message);
    }

   
}