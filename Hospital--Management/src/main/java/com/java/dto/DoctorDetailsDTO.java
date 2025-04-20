package com.java.dto;


import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDetailsDTO {

	private String doctorId;

	private String firstName;

	private String lastName;

	private String email;

	private String phoneNumber;

	private AddressDTO addressDTO;

	private String specialization;

	private double experienceInYears;

	private LocalDate dateOfJoining;

	private Boolean isActive;

}
