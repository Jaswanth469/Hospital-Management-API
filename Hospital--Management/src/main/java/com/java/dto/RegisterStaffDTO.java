package com.java.dto;


import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterStaffDTO {
	
	private String firstName;
	
	private String lastName;
	
	private String phoneNumber;
	
	private String email;
	
	private String role; // Example: "Receptionist", "Technician", etc.
	
	private LocalDate dateOfJoining;
	
	private double experienceInYears;
	
	private boolean canLogin;
	
	private AddressDTO addressDTO; // Address details
}