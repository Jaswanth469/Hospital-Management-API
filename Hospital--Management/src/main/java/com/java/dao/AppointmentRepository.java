package com.java.dao;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.java.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, String>{
	
	@Query("SELECT COUNT(a) FROM Appointment a " +
		       "WHERE a.appointmentDate = :date " +
		       "AND NOT (a.endTime <= :startTime OR a.startTime >= :endTime) " +
		       "AND (a.doctor.id = :doctorId OR a.patient.id = :patientId) " +
		       "AND a.status <> 'CANCELLED'")
	public int countConflictingAppointments(
		    @Param("doctorId") String doctorId, 
		    @Param("patientId") String patientId, 
		    @Param("date") LocalDate date, 
		    @Param("startTime") LocalTime startTime, 
		    @Param("endTime") LocalTime endTime
		);
	
	
	@Query("SELECT a FROM Appointment a " +
		       "WHERE a.doctor.staffId = :doctorId " +
		       "AND a.status <> 'CANCELLED' " +
		       "AND a.appointmentDate >= CURRENT_DATE")
	public List<Appointment> findFutureAppointmentsByDoctorId(@Param("doctorId") String doctorId);
	
	
	@Query("SELECT a FROM Appointment a " +
		       "WHERE a.doctor.staffId = :doctorId " +
		       "AND a.status <> 'CANCELLED' " +
		       "AND a.appointmentDate = :appointmentDate")
	public List<Appointment> findByDoctorAndDate(@Param("doctorId") String doctorId, @Param("appointmentDate") LocalDate appointmentDate);


}