package com.java.dao;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.model.Bed;
import com.java.model.Patient;

public interface BedRepository extends JpaRepository<Bed, String>{
	
	public List<Bed> findByRoom_RoomNumberAndIsOccupiedFalse(String roomNumber);
	
	public List<Bed> findByIsOccupiedFalse();
	
	Optional<Bed> findByPatient(Patient patient);


}