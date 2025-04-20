package com.java.service.impl;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.builder.AppointmentBuilder;
import com.java.dao.AppointmentRepository;
import com.java.dao.DoctorRepository;
import com.java.dao.DoctorScheduleRepository;
import com.java.dao.PatientRepository;
import com.java.dto.AppointmentDTO;
import com.java.dto.RescheduleAppointmentDTO;
import com.java.exception.AppointmentAlreadyExistsException;
import com.java.exception.AppointmentNotFoundException;
import com.java.exception.DoctorNotFoundException;
import com.java.exception.DoctorUnavailableException;
import com.java.exception.InvalidDateException;
import com.java.exception.PatientNotFoundException;
import com.java.model.Appointment;
import com.java.model.Doctor;
import com.java.model.Patient;
import com.java.notification.EmailService;
import com.java.service.AppointmentService;
import com.java.util.Constants;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final EmailService emailService;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, PatientRepository patientRepository,DoctorScheduleRepository doctorScheduleRepository,EmailService emailService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
		this.patientRepository = patientRepository;
		this.doctorScheduleRepository = doctorScheduleRepository;
		this.emailService = emailService;
    }

    @Transactional
    public AppointmentDTO bookAppointment(AppointmentDTO appointmentDTO) {
        logger.info("Booking appointment: {}", appointmentDTO);

        validateDate(appointmentDTO.getAppointmentDate());

        validateTime(appointmentDTO.getStartTime(), appointmentDTO.getEndTime());

        Doctor doctor = doctorRepository.findById(appointmentDTO.getDoctorId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor " + Constants.NOT_FOUND + appointmentDTO.getDoctorId()));
       

        if (!doctor.getIsEmployeeActive()) {  
            throw new DoctorUnavailableException("Doctor is no longer active and cannot take appointments.");
        }
        
        
        boolean isDateUnAvailable = doctorScheduleRepository.existsByDoctor_StaffIdAndUnavailableDate(doctor.getStaffId(), appointmentDTO.getAppointmentDate());
        
        if(isDateUnAvailable) {
        	throw new DoctorUnavailableException("Doctor is not available on : "+ appointmentDTO.getAppointmentDate());
        }
        
        Patient patient = patientRepository.findById(appointmentDTO.getPatientId())
        		.orElseThrow(() -> new PatientNotFoundException("Patient " + Constants.NOT_FOUND + appointmentDTO.getPatientId()));
        		

        int count = appointmentRepository.countConflictingAppointments(
                doctor.getStaffId(),patient.getPatientId(), appointmentDTO.getAppointmentDate(), appointmentDTO.getStartTime(), appointmentDTO.getEndTime());

        if (count>0) {
            throw new AppointmentAlreadyExistsException(Constants.APPOINTMENT_EXISTS);
        }

        Appointment appointment = AppointmentBuilder.buildAppointmentFromDTO(appointmentDTO, doctor, patient, "SCHEDULED");
       
        Appointment savedAppointemnt = appointmentRepository.save(appointment);
        
		emailService.sendAppointmentConfirmation(
                patient.getEmail(),
                patient.getFirstName(),
                doctor.getFirstName(),
                appointmentDTO.getAppointmentDate(),
                appointmentDTO.getStartTime(),
                appointmentDTO.getEndTime()
            );
        
        AppointmentDTO dto = AppointmentBuilder.buildDTOFromAppointment(savedAppointemnt);

        logger.info("Successfully booked appointment for patient {} with doctor {} on {} from {} to {}",
                appointmentDTO.getPatientId(), appointmentDTO.getDoctorId(), 
                appointmentDTO.getAppointmentDate(), appointmentDTO.getStartTime(), appointmentDTO.getEndTime());
        return dto;
    }
    
    @Transactional
    @Override
    public AppointmentDTO rescheduleAppointment(String appointmentId, RescheduleAppointmentDTO rescheduleAppointmentDTO) {
        logger.info("Rescheduling appointment ID: {} to {} from {} to {}", appointmentId, rescheduleAppointmentDTO.getNewDate(), rescheduleAppointmentDTO.getStartTime(), rescheduleAppointmentDTO.getEndTime());

        validateDate(rescheduleAppointmentDTO.getNewDate());
        
        validateTime(rescheduleAppointmentDTO.getStartTime(), rescheduleAppointmentDTO.getEndTime());

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with ID: " + appointmentId));

        Doctor doctor = appointment.getDoctor();
        Patient patient = appointment.getPatient();

        if (!doctor.getIsEmployeeActive()) {
            throw new DoctorUnavailableException("Doctor is no longer active and cannot take appointments.");
        }
        
        boolean isDateUnAvailable = doctorScheduleRepository.existsByDoctor_StaffIdAndUnavailableDate(doctor.getStaffId(), rescheduleAppointmentDTO.getNewDate());
        
        if(isDateUnAvailable) {
        	throw new DoctorUnavailableException("Doctor is not available on : "+ rescheduleAppointmentDTO.getNewDate());
        }

        int count = appointmentRepository.countConflictingAppointments(
                doctor.getStaffId(), patient.getPatientId(), rescheduleAppointmentDTO.getNewDate(), rescheduleAppointmentDTO.getStartTime(), rescheduleAppointmentDTO.getEndTime());

        if (count > 0) {
            throw new AppointmentAlreadyExistsException(Constants.APPOINTMENT_EXISTS);
        }

        appointment.setAppointmentDate(rescheduleAppointmentDTO.getNewDate());
        appointment.setStartTime(rescheduleAppointmentDTO.getStartTime());
        appointment.setEndTime(rescheduleAppointmentDTO.getEndTime());
        appointment.setStatus("SCHEDULED");

        Appointment updatedAppointment = appointmentRepository.save(appointment);

        logger.info("Successfully rescheduled appointment ID: {} to {} from {} to {}", appointmentId,rescheduleAppointmentDTO.getNewDate(), rescheduleAppointmentDTO.getStartTime(), rescheduleAppointmentDTO.getEndTime());
        return AppointmentBuilder.buildDTOFromAppointment(updatedAppointment);
    }
    
    @Override
    public AppointmentDTO getAppointmentDetails(String appointmentId) {
        logger.info("Fetching details for appointment ID: {}", appointmentId);

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with ID: " + appointmentId));

        return AppointmentBuilder.buildDTOFromAppointment(appointment);
    }
    
    @Override
    public List<AppointmentDTO> getAllAppointments() {
        logger.info("Fetching all appointments");

        List<Appointment> appointments = appointmentRepository.findAll();
        
        return appointments.stream()
                .map(AppointmentBuilder::buildDTOFromAppointment)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<AppointmentDTO> getFutureAppointmentsByDoctor(String doctorId) {
        logger.info("Fetching appointments for doctor ID: {}", doctorId);

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with ID: " + doctorId));
        
        if(!doctor.getIsEmployeeActive()) {
        	throw new DoctorUnavailableException("Doctor is no longer active and cannot take appointments.");
        }

        List<Appointment> appointments = appointmentRepository.findFutureAppointmentsByDoctorId(doctorId);
        
        return appointments.stream()
                .map(AppointmentBuilder::buildDTOFromAppointment)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<AppointmentDTO> getAppointmentsOfDoctorByDate(String doctorId, LocalDate appointmentDate) {
        logger.info("Fetching appointments for doctor {} on {}", doctorId, appointmentDate);
        
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with ID: " + doctorId));
        
        List<Appointment> appointments = appointmentRepository.findByDoctorAndDate(doctor.getStaffId(), appointmentDate);

        if (appointments.isEmpty()) {
            logger.info("No appointments found for doctor {} on {}", doctorId, appointmentDate);
        }

        return appointments.stream()
                .map(AppointmentBuilder::buildDTOFromAppointment)
                .toList();
    }

    @Transactional
    @Override
    public void cancelAppointment(String appointmentId) {
        logger.info("Cancelling appointment with ID: {}", appointmentId);

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment "+ Constants.NOT_FOUND  + appointmentId));

        appointment.setStatus("CANCELLED");
        appointmentRepository.save(appointment);

        logger.info("Appointment cancelled successfully with ID: {}", appointmentId);
    }
    
    private void validateDate(LocalDate date) {
        LocalDate today = LocalDate.now();
        if (date.isBefore(today)) {
            throw new InvalidDateException("Appointment date must be today or later.");
        }
    }
    

    private void validateTime(LocalTime startTime, LocalTime endTime) {
        LocalTime startLimit = LocalTime.of(10, 0);
        LocalTime endLimit = LocalTime.of(17, 0);
        if(startTime.isAfter(endTime)) {
        	throw new InvalidDateException("End time cannot be before start time");
        }else if(startTime.equals(endTime)) {
        	throw new InvalidDateException("Start time and End Time can not be same");
        }
        else if (startTime.isBefore(startLimit) || endTime.isAfter(endLimit)) {
            throw new InvalidDateException("Appointment time must be between 10:00 AM and 5:00 PM.");
        }
    }
}
