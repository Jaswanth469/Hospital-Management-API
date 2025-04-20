package com.java.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(DoctorNotFoundException.class)
	public ResponseEntity<String> doctorNotFoundException(DoctorNotFoundException exception){
		return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DoctorServiceException.class)
	public ResponseEntity<String> doctorServiceException(DoctorServiceException exception){
		return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(StaffNotFoundException.class)
	public ResponseEntity<String> staffNotFoundException(StaffNotFoundException exception){
		return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(StaffServiceException.class)
	public ResponseEntity<String> staffServiceException(StaffServiceException exception){
		return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(PatientNotFoundException.class)
	public ResponseEntity<String> patientNotFoundException(PatientNotFoundException exception){
		return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(PatientServiceException.class)
	public ResponseEntity<String> patientServiceException(PatientServiceException exception){
		return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(DoctorStatusUpdateFailedException.class)
	public ResponseEntity<String> doctorStatusUpdateFailedException(DoctorStatusUpdateFailedException exception){
		return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(DoctorUnavailableException.class)
	public ResponseEntity<String> doctorUnavailableException(DoctorUnavailableException exception){
		return new ResponseEntity<>(exception.getMessage(),HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(AppointmentAlreadyExistsException.class)
	public ResponseEntity<String> appointmentAlreadyExistsException(AppointmentAlreadyExistsException exception){
		return new ResponseEntity<>(exception.getMessage(),HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(AppointmentNotFoundException.class)
	public ResponseEntity<String> appointmentNotFoundException(AppointmentNotFoundException exception){
		return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InvalidDateException.class)
	public ResponseEntity<String> invalidDateException(InvalidDateException exception){
		return new ResponseEntity<>(exception.getMessage(),HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(BedNotFoundException.class)
	public ResponseEntity<String> bedNotFoundException(BedNotFoundException exception){
		return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(RoomNotFoundException.class)
	public ResponseEntity<String> roomNotFoundException(RoomNotFoundException exception){
		return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BedNotAvailableException.class)
	public ResponseEntity<String> bedNotAvailableException(BedNotAvailableException exception){
		return new ResponseEntity<>(exception.getMessage(),HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(PasswordDoesntExistException.class)
	public ResponseEntity<String> passwordDoesntExistException(PasswordDoesntExistException exception){
		return new ResponseEntity<>(exception.getMessage(),HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<String> invalidCredentialsException(InvalidCredentialsException exception){
		return new ResponseEntity<>(exception.getMessage(),HttpStatus.UNAUTHORIZED);
	}
}
