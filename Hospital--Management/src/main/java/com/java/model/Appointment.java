package com.java.model;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "appointments")
@Data
@ToString(exclude = "patient")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    private String appointmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "status", nullable = false)
    private String status; // e.g., "SCHEDULED", "CANCELLED", "COMPLETED"

    @Column(name = "notes")
    private String notes; // Additional information about the appointment
    
    @PrePersist
    private void generateAppointmentId() {
        if (this.appointmentId == null && this.doctor != null) {
        	
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHMMSS"));

            String doctorSuffix = doctor.getStaffId().length() > 4
                    ? doctor.getStaffId().substring(doctor.getStaffId().length() - 4)
                    : doctor.getStaffId();

            this.appointmentId = timestamp + doctorSuffix;
        }
    }
    
    
    
}
