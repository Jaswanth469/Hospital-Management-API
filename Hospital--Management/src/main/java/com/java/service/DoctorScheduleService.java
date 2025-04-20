package com.java.service;


import java.time.LocalDate;
import java.util.List;

public interface DoctorScheduleService {
	
	public void markDoctorUnavailable(String doctorId, List<LocalDate> dates);
	
	public void removeUnavailableDates(String doctorId, List<LocalDate> dates);
	
	public boolean isDoctorAvailable(String doctorId, LocalDate date);

}
