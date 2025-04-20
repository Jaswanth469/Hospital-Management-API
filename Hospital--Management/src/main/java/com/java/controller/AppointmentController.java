package com.java.controller;


import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java.dto.AppointmentDTO;
import com.java.dto.RescheduleAppointmentDTO;
import com.java.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/book-appointment")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public ResponseEntity<AppointmentDTO> bookAppointment(@RequestBody AppointmentDTO appointmentDTO) {

    	AppointmentDTO appointment = appointmentService.bookAppointment(appointmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }
    
    @PutMapping("/reschedule/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public ResponseEntity<AppointmentDTO> rescheduleAppointment(
            @PathVariable String appointmentId,
            @RequestBody RescheduleAppointmentDTO rescheduleAppointmentDTO) {
        
        AppointmentDTO updatedAppointment = appointmentService.rescheduleAppointment(appointmentId, rescheduleAppointmentDTO);
        return ResponseEntity.ok(updatedAppointment);
    }

    @GetMapping("/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN','Doctor')")
    public ResponseEntity<AppointmentDTO> getAppointmentDetails(@PathVariable String appointmentId) {
        AppointmentDTO appointment = appointmentService.getAppointmentDetails(appointmentId);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        List<AppointmentDTO> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    /** Get future appointments of a doctor */
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN','Doctor')")
    public ResponseEntity<List<AppointmentDTO>> getFutureAppointmentsByDoctor(@PathVariable String doctorId) {
        List<AppointmentDTO> appointments = appointmentService.getFutureAppointmentsByDoctor(doctorId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/doctor/{doctorId}/date")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN','Doctor')")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsOfDoctorByDate(
            @PathVariable String doctorId,
            @RequestParam LocalDate appointmentDate) {
        
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsOfDoctorByDate(doctorId, appointmentDate);
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/cancel/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public ResponseEntity<String> cancelAppointment(@PathVariable String appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.ok("Appointment cancelled successfully");
    }
}


