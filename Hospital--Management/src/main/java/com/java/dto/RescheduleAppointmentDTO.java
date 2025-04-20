package com.java.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RescheduleAppointmentDTO {
	
	private LocalDate newDate;
	
	private LocalTime startTime;
	
	private LocalTime endTime;

}
