package com.java.builder;


import com.java.dto.AddressDTO;
import com.java.dto.RegisterStaffDTO;
import com.java.dto.StaffDetailsDTO;
import com.java.model.Address;
import com.java.model.Staff;
import com.java.model.User;

public class StaffBuilder {

    // Convert DTO to Staff Entity
    public static Staff buildStaffFromDTO(RegisterStaffDTO dto) {
        return Staff.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phoneNumber(dto.getPhoneNumber())
                .dateOfJoining(dto.getDateOfJoining())
                .experienceInYears(dto.getExperienceInYears())
                .isEmployeeActive(true)
                .canLogin(dto.isCanLogin()) // Some staff may have login, some may not
                .address(Address.builder()
                        .street(dto.getAddressDTO().getStreet())
                        .landMark(dto.getAddressDTO().getLandMark())
                        .city(dto.getAddressDTO().getCity())
                        .postalCode(dto.getAddressDTO().getPostalCode())
                        .state(dto.getAddressDTO().getState())
                        .country(dto.getAddressDTO().getCountry())
                        .build())
                .user(User.builder()
                        .email(dto.getEmail() != null ? dto.getEmail() : null)
                        .password(null) 
                        .role(dto.getRole())
                        .build()) 
                .build();
    }

    // Convert Staff Entity to StaffDetailsDTO
    public static StaffDetailsDTO buildStaffDetailsDTOFromStaff(Staff staff) {
        return StaffDetailsDTO.builder()
                .staffId(staff.getStaffId())
                .firstName(staff.getFirstName())
                .lastName(staff.getLastName())
                .email(staff.getUser().getEmail() != null ? staff.getUser().getEmail() : null) // Only if user exists
                .phoneNumber(staff.getPhoneNumber())
                .addressDTO(AddressDTO.builder()
                        .street(staff.getAddress().getStreet())
                        .landMark(staff.getAddress().getLandMark())
                        .city(staff.getAddress().getCity())
                        .postalCode(staff.getAddress().getPostalCode())
                        .state(staff.getAddress().getState())
                        .country(staff.getAddress().getCountry())
                        .build())
                .experienceInYears(staff.getExperienceInYears())
                .dateOfJoining(staff.getDateOfJoining())
                .isActive(staff.getIsEmployeeActive())
                .role(staff.getUser().getRole()) // If user exists, get role
                .canLogin(staff.getCanLogin())
                .build();
    }
}