package com.java.service;


import java.time.LocalDate;
import java.util.List;

import com.java.dto.AppointmentDTO;
import com.java.dto.RescheduleAppointmentDTO;

public interface AppointmentService {
	
	public AppointmentDTO bookAppointment(AppointmentDTO appointmentDTO);
	
	public AppointmentDTO rescheduleAppointment(String appointmentId, RescheduleAppointmentDTO rescheduleAppointmentDTO);
	
	public AppointmentDTO getAppointmentDetails(String appointmentId);
	
	public List<AppointmentDTO> getAllAppointments();
	
	public List<AppointmentDTO> getFutureAppointmentsByDoctor(String doctorId);
	
	public List<AppointmentDTO> getAppointmentsOfDoctorByDate(String doctorId, LocalDate appointmentDate);
	
	public void cancelAppointment(String appointmentId);
	
}
