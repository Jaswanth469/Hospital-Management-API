package com.java.dto;


import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StaffDetailsDTO {
	
    private String staffId;
    
    private String firstName;
    
    private String lastName;
    
    private String email;
    
    private String phoneNumber;
    
    private AddressDTO addressDTO;
    
    private String role;
    
    private double experienceInYears;
    
    private LocalDate dateOfJoining;
    
    private boolean isActive;
    
    private boolean canLogin;
}

