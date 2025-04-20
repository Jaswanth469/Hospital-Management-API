package com.java.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java.dto.PatientDetailsDTO;
import com.java.dto.RegisterPatientDTO;
import com.java.service.PatientService;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public ResponseEntity<PatientDetailsDTO> registerPatient(@RequestBody RegisterPatientDTO patientDto) {
        PatientDetailsDTO savedPatient = patientService.savePatient(patientDto);
        return new ResponseEntity<PatientDetailsDTO>(savedPatient,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public ResponseEntity<PatientDetailsDTO> updatePatient(
            @PathVariable String id,
            @RequestBody RegisterPatientDTO patientDto) {
    	
        PatientDetailsDTO updatedPatient = patientService.updatePatient(id, patientDto);
        return ResponseEntity.ok(updatedPatient);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN','Doctor')")
    public ResponseEntity<PatientDetailsDTO> getPatientById(@PathVariable String id) {
    	
        PatientDetailsDTO patientDetails = patientService.getPatientDetails(id);
        return ResponseEntity.ok(patientDetails);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN','Doctor')")
    public ResponseEntity<List<PatientDetailsDTO>> getAllPatients() {
    	
        List<PatientDetailsDTO> patients = patientService.getAllPatientDetails();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN','Doctor')")
    public ResponseEntity<?> searchPatients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phoneNumber) {

        if (name != null) {
            List<PatientDetailsDTO> patients = patientService.getPatientsByName(name);
            return ResponseEntity.ok(patients);
        } else if (phoneNumber != null) {
            return ResponseEntity.ok(patientService.getPatientByPhone(phoneNumber));
        } else {
            return ResponseEntity.badRequest().body("Please provide a name or phoneNumber.");
        }
    }

    @DeleteMapping("/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public ResponseEntity<Object> deletePatient(@PathVariable String patientId) {
    	
        patientService.deletePatient(patientId);
        return ResponseEntity.noContent().build();
    }
}
