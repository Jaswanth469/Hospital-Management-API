package com.java.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "patients")
@Data
@ToString(exclude = "appointments")
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @Column(name = "patient_id")
    private String patientId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth; // Stored in 'YYYY-MM-DD' format.

    @OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	private Address address;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Appointment> appointments;
    
    
    @PrePersist
    private void generatePatientId() {
        if (this.patientId == null) {
            String yearMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));

            String randomDigits = String.format("%06d", new Random().nextInt(1_000_000));

            this.patientId = yearMonth + randomDigits;
        }
    }

	public Patient(String firstName, String lastName, String email, String phoneNumber, String gender,
			LocalDate dateOfBirth, Address address) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
	}
    
}
