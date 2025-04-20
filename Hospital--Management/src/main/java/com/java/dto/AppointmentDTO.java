package com.java.dto;


import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDTO {
	
	private String appointmentId;
	
	private String doctorId;

	private String patientId;

	private LocalDate appointmentDate;

	private LocalTime startTime;

	private LocalTime endTime;
	
	private String notes;

}
