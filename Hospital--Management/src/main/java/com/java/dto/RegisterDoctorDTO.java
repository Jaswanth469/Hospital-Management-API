package com.java.dto;


import java.time.LocalDate;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDoctorDTO {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Phone Number can not be Null")
    private String phoneNumber;

    private AddressDTO addressDTO;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @PositiveOrZero(message = "Experience must be zero or positive")
    private double experienceInYears;

    private LocalDate dateOfJoining;
}
