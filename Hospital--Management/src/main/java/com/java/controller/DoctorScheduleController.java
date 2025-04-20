package com.java.controller;


import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java.service.DoctorScheduleService;

@RestController
@RequestMapping("/doctor-schedule")
public class DoctorScheduleController {

    private final DoctorScheduleService doctorScheduleService;

    public DoctorScheduleController(DoctorScheduleService doctorScheduleService) {
        this.doctorScheduleService = doctorScheduleService;
    }

    @PostMapping("/mark-unavailable/{doctorId}")
    public ResponseEntity<String> markDoctorUnavailable(@PathVariable String doctorId,
                                                         @RequestBody List<LocalDate> dates) {
        doctorScheduleService.markDoctorUnavailable(doctorId, dates);
        return ResponseEntity.ok("Doctor marked as unavailable successfully.");
    }

    @DeleteMapping("/remove-unavailable/{doctorId}")
    public ResponseEntity<String> removeUnavailableDates(@PathVariable String doctorId,
                                                          @RequestBody List<LocalDate> dates) {
        doctorScheduleService.removeUnavailableDates(doctorId, dates);
        return ResponseEntity.ok("Unavailable dates removed successfully.");
    }

    @GetMapping("/is-available/{doctorId}")
    public ResponseEntity<Boolean> isDoctorAvailable(@PathVariable String doctorId,
                                                      @RequestParam(name = "date") LocalDate date) {
        boolean available = doctorScheduleService.isDoctorAvailable(doctorId, date);
        return ResponseEntity.ok(available);
    }
}
