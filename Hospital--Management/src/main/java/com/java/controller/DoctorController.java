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

import com.java.dto.DoctorDetailsDTO;
import com.java.dto.RegisterDoctorDTO;
import com.java.service.DoctorService;

@RestController
@RequestMapping("/doctors")
public class DoctorController {
	
	private final DoctorService doctorService;
	
	public DoctorController(DoctorService doctorService) {
		super();
		this.doctorService = doctorService;
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
	public ResponseEntity<DoctorDetailsDTO> registerDoctor(@RequestBody RegisterDoctorDTO doctorDto) {
		
		DoctorDetailsDTO savedDoctor = doctorService.saveDoctor(doctorDto);
		return new ResponseEntity<DoctorDetailsDTO>(savedDoctor,HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN','Doctor')")
	public ResponseEntity<DoctorDetailsDTO> updateDoctor(@PathVariable(name = "id") String doctorId , @RequestBody RegisterDoctorDTO doctorDto) {
		
		DoctorDetailsDTO updatedDoctor = doctorService.updateDoctor(doctorId, doctorDto);
		return new ResponseEntity<DoctorDetailsDTO>(updatedDoctor,HttpStatus.OK);
	}
	
	@GetMapping("/id")
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN','Doctor')")
	public ResponseEntity<DoctorDetailsDTO> getDoctorDetails(@RequestParam(name = "id") String doctorId) {
		
		DoctorDetailsDTO doctorDetails = doctorService.getDoctorDetails(doctorId);
		return new ResponseEntity<DoctorDetailsDTO>(doctorDetails,HttpStatus.OK);
	}
	
	@GetMapping("/all")
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
	public ResponseEntity<List<DoctorDetailsDTO>> getAllDcotors() {
		
		List<DoctorDetailsDTO> allDoctorsDeatils = doctorService.getAllDoctorsDeatils();
		return new ResponseEntity<List<DoctorDetailsDTO>>(allDoctorsDeatils,HttpStatus.OK);
	}
	
	@GetMapping("/search")
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN','Doctor')")
	public ResponseEntity<List<DoctorDetailsDTO>> searchDoctorsByName(@RequestParam String name) {
	    List<DoctorDetailsDTO> doctors = doctorService.getDoctorsByName(name);
	    return ResponseEntity.ok(doctors);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
	public ResponseEntity<Object> deleteDcotor(@PathVariable(name = "id") String doctorId) {
		
		doctorService.deleteDoctor(doctorId);
		return ResponseEntity.noContent().build();
	}

}
