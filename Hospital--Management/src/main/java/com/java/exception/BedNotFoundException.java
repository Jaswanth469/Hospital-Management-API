package com.java.exception;


public class BedNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public BedNotFoundException(String message) {
		super(message);
	}

}