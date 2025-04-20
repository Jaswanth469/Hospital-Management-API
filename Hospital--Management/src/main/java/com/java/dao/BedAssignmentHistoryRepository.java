package com.java.dao;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.model.Bed;
import com.java.model.BedAssignmentHistory;

public interface BedAssignmentHistoryRepository extends JpaRepository<BedAssignmentHistory, Long>{
	
	public Optional<BedAssignmentHistory> findByBedAndVacatedAtIsNull(Bed bed);

}