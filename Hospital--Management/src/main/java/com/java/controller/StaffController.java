package com.java.controller;


import com.java.dto.RegisterStaffDTO;
import com.java.dto.StaffDetailsDTO;
import com.java.service.StaffService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping()
    public ResponseEntity<StaffDetailsDTO> registerStaff(@RequestBody RegisterStaffDTO registerStaffDTO) {
        StaffDetailsDTO staffDetailsDTO = staffService.saveStaff(registerStaffDTO);
        return new ResponseEntity<StaffDetailsDTO>(staffDetailsDTO,HttpStatus.CREATED);
    }
    
    @PutMapping("/{staffId}")
    public ResponseEntity<StaffDetailsDTO> updateStaff(@PathVariable String staffId, @RequestBody RegisterStaffDTO staffDTO) {
        StaffDetailsDTO staffDetailsDTO = staffService.updateStaff(staffId, staffDTO);
        return ResponseEntity.ok(staffDetailsDTO);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<StaffDetailsDTO> getStaffById(@PathVariable String id) {
        StaffDetailsDTO staffDetails = staffService.getStaffDetails(id);
        return ResponseEntity.ok(staffDetails);
    }

    @GetMapping("/all")
    public ResponseEntity<List<StaffDetailsDTO>> getAllStaff() {
        List<StaffDetailsDTO> staffList = staffService.getAllStaffDetails();
        return ResponseEntity.ok(staffList);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<StaffDetailsDTO>> getStaffByName(@RequestParam(name = "name") String name) {
        List<StaffDetailsDTO> staffList = staffService.getStaffByName(name);
        return ResponseEntity.ok(staffList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteStaff(@PathVariable String id) {
    	
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }

   
}