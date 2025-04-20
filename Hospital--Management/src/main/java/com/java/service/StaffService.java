package com.java.service;


import java.util.List;

import com.java.dto.RegisterStaffDTO;
import com.java.dto.StaffDetailsDTO;

public interface StaffService {
	
	 public StaffDetailsDTO saveStaff(RegisterStaffDTO staffDto);
	 
	 public StaffDetailsDTO updateStaff(String staffId, RegisterStaffDTO staffDto);
	 
	 public StaffDetailsDTO getStaffDetails(String staffId);
	 
	 public List<StaffDetailsDTO> getAllStaffDetails();
	 
	 public List<StaffDetailsDTO> getStaffByName(String name);
	 
	 public void deleteStaff(String staffId);

}
