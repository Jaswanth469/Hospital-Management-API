package com.java.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, String>{
	
	List<Patient> findByFirstNameContainingIgnoreCase(String name);
	
	Optional<Patient> findByPhoneNumber(String phoneNumber);

}