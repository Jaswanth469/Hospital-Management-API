package com.java.controller;


import com.java.dto.BedDTO;
import com.java.dto.RoomDTO;
import com.java.service.RoomService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<RoomDTO> addRoom(@RequestBody RoomDTO roomDTO) {
        RoomDTO roomDTO2 = roomService.addRoom(roomDTO);
		return new ResponseEntity<RoomDTO>(roomDTO2,HttpStatus.CREATED);
    }

    @PostMapping("/beds/{roomNumber}")
    public ResponseEntity<BedDTO> addBedToRoom(@PathVariable String roomNumber, @RequestBody BedDTO bedDTO) {
        BedDTO addedBedDTo = roomService.addBedToRoom(roomNumber, bedDTO);
		return new ResponseEntity<BedDTO>(addedBedDTo,HttpStatus.CREATED);
    }
    
    @PutMapping("/update")
    public ResponseEntity<RoomDTO> updateRoom(@RequestParam String roomNumber, @RequestBody RoomDTO roomDTO) {
        return ResponseEntity.ok(roomService.updateRoom(roomNumber, roomDTO));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteRoom(@RequestParam String roomNumber) {
        roomService.deleteRoom(roomNumber);
        return ResponseEntity.ok("Room deleted successfully.");
    }

    @PutMapping("/beds/update")
    public ResponseEntity<BedDTO> updateBed(@RequestParam String bedNumber, @RequestBody BedDTO bedDTO) {
        return ResponseEntity.ok(roomService.updateBed(bedNumber, bedDTO));
    }

    @DeleteMapping("/beds/delete")
    public ResponseEntity<String> deleteBed(@RequestParam String bedNumber) {
        roomService.deleteBed(bedNumber);
        return ResponseEntity.ok("Bed deleted successfully.");
    }

    @PostMapping("/beds/assign")
    public ResponseEntity<Void> assignBedToPatient(@RequestParam(name = "bedNumber") String bedNumber, @RequestParam(name="patientId") String patientId) {
        roomService.assignBedToPatient(bedNumber, patientId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/beds/vacate")
    public ResponseEntity<Void> vacateBed(@RequestParam(name = "bedNumber") String bedNumber) {
        roomService.vacateBed(bedNumber);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/beds/available")
    public ResponseEntity<List<BedDTO>> getAvailableBedsInRoom(@RequestParam(name = "roomNumber") String roomNumber) {
        return ResponseEntity.ok(roomService.getAvailableBedsInRoom(roomNumber));
    }

    @GetMapping("/beds/all-available")
    public ResponseEntity<List<BedDTO>> getAllAvailableBeds() {
        return ResponseEntity.ok(roomService.getAllAvailableBeds());
    }
    
    @GetMapping("/beds/patient/{patientId}")
    public ResponseEntity<BedDTO> getBedDetailsByPatient(@PathVariable String patientId) {
        return ResponseEntity.ok(roomService.getBedDetailsByPatientId(patientId));
    }

}
