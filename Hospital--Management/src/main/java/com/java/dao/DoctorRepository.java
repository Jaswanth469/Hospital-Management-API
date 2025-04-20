package com.java.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, String>{
	
	List<Doctor> findByFirstNameContainingIgnoreCase(String firstName);

}