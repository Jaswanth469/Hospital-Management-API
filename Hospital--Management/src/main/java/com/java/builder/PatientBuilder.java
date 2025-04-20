package com.java.builder;


import com.java.dto.AddressDTO;
import com.java.dto.PatientDetailsDTO;
import com.java.dto.RegisterPatientDTO;
import com.java.model.Patient;
import com.java.model.Address;

public class PatientBuilder {

    public static Patient buildPatientFromDTO(RegisterPatientDTO dto) {
        if (dto == null) {
            return null;
        }

        return new Patient(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                dto.getPhoneNumber(),
                dto.getGender(),
                dto.getDateOfBirth(),
                Address.builder()
                        .street(dto.getAddressDTO().getStreet())
                        .landMark(dto.getAddressDTO().getLandMark())
                        .city(dto.getAddressDTO().getCity())
                        .state(dto.getAddressDTO().getState())
                        .postalCode(dto.getAddressDTO().getPostalCode())
                        .country(dto.getAddressDTO().getCountry())
                        .build()
        );
    }

    public static PatientDetailsDTO buildPatientDetailsDTOFromPatient(Patient patient) {
        if (patient == null) {
            return null;
        }

        return PatientDetailsDTO.builder()
                .patientId(patient.getPatientId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .email(patient.getEmail())
                .phoneNumber(patient.getPhoneNumber())
                .gender(patient.getGender())
                .dateOfBirth(patient.getDateOfBirth())
                .addressDTO(
                        AddressDTO.builder()
                                .street(patient.getAddress().getStreet())
                                .landMark(patient.getAddress().getLandMark())
                                .city(patient.getAddress().getCity())
                                .state(patient.getAddress().getState())
                                .postalCode(patient.getAddress().getPostalCode())
                                .country(patient.getAddress().getCountry())
                                .build()
                )
                .build();
    }
}
