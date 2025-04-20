package com.java.dao;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.java.model.DoctorSchedule;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

    public boolean existsByDoctor_StaffIdAndUnavailableDate(String doctorId, LocalDate date);

    public void deleteByDoctor_StaffIdAndUnavailableDateIn(String doctorId, List<LocalDate> dates);
    
    @Query("SELECT d.unavailableDate FROM DoctorSchedule d WHERE d.doctor.id = :doctorId")
    List<LocalDate> findUnavailableDatesByDoctorId(@Param("doctorId") String doctorId);

}