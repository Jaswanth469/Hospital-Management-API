package com.java.exception;


public class BedAssignmentNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public BedAssignmentNotFoundException(String message) {
		super(message);
	}
}