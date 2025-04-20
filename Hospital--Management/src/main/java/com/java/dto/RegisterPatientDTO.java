package com.java.dto;


import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPatientDTO {

	private String firstName;

	private String lastName;

	private String email;

	private String phoneNumber;
	
	private AddressDTO addressDTO;

	private String gender;

	private LocalDate dateOfBirth; // Stored in 'YYYY-MM-DD' format.

}