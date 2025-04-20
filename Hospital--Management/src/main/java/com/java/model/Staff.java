package com.java.model;



import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Staff {

    @Id
    @Column(name = "staff_id")
    private String staffId;  // Primary Key

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;  // Phone number for contact

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "date_of_joining")
    private LocalDate dateOfJoining;

    @Column(name = "experience", nullable = false)
    private double experienceInYears;

    @Column(name = "active")
    private Boolean isEmployeeActive;  // Whether the staff is currently working

    @Column(name = "can_login", nullable = false)
    private Boolean canLogin; 

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Link to the User entity (authentication)

    @PrePersist
    private void generateStaffId() {
        if (this.staffId == null) {
            this.staffId = "VIJ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }
}


