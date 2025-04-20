package com.java.service.impl;


import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.java.builder.DoctorBuilder;
import com.java.dao.DoctorRepository;
import com.java.dto.DoctorDetailsDTO;
import com.java.dto.RegisterDoctorDTO;
import com.java.exception.DoctorNotFoundException;
import com.java.exception.DoctorServiceException;
import com.java.model.Doctor;
import com.java.service.DoctorService;
import com.java.util.Constants;

@Service
public class DoctorServiceImpl implements DoctorService {
    
    
    DoctorRepository doctorRepository;
    
    public DoctorServiceImpl(DoctorRepository doctorRepository) {
		this.doctorRepository = doctorRepository;
	}

	private final Logger logger = LoggerFactory.getLogger(DoctorServiceImpl.class);

    @Override
    public DoctorDetailsDTO saveDoctor(RegisterDoctorDTO doctorDto) {
        try {
            Doctor doctor = DoctorBuilder.buildDoctorFromDTO(doctorDto);
            Doctor savedDoctor = doctorRepository.save(doctor);
            logger.info("Doctor " + Constants.CREATED, savedDoctor.getStaffId());
            return DoctorBuilder.buildDoctorDetailsDTOFromDoctor(savedDoctor);
        } catch (Exception e) {
            logger.error(Constants.ERROR+ " saving doctor: {}", e.getMessage());
            throw new DoctorServiceException(Constants.ERROR+ " saving doctor" + e.getMessage());
        }
    }

    @Override
    public DoctorDetailsDTO updateDoctor(String doctorId, RegisterDoctorDTO doctorDto) {
        try {
            Doctor existingDoctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor " + Constants.NOT_FOUND + doctorId));
            
            logger.info(Constants.RETRIEVED, "Doctor" ,  doctorId);

            Doctor updatedDoctor = DoctorBuilder.buildDoctorFromDTO(doctorDto);

            // Preserve essential details from the existing doctor
            updatedDoctor.getUser().setUserId(existingDoctor.getUser().getUserId());
            updatedDoctor.getUser().setPassword(existingDoctor.getUser().getPassword());
            updatedDoctor.setStaffId(existingDoctor.getStaffId());
            updatedDoctor.getAddress().setAddressId(existingDoctor.getAddress().getAddressId());

            doctorRepository.save(updatedDoctor);
            logger.info("Doctor " + Constants.UPDATED + "{}", doctorId);

            return DoctorBuilder.buildDoctorDetailsDTOFromDoctor(updatedDoctor);

        } catch (DataAccessException e) {  // Catching database-related issues
            logger.error("{} updating doctor with ID: {} - Exception: {}", Constants.ERROR, doctorId, e.getMessage());
            throw new DoctorServiceException("Database "+ Constants.ERROR + " updating doctor with ID: " + doctorId);
        } catch (DoctorNotFoundException e) {
        	logger.error(e.getMessage());// Letting not found exceptions pass through
            throw e;
        } catch (Exception e) { // Catching unexpected errors
            logger.error("{} updating doctor with ID: {} - Exception - {}", Constants.ERROR, doctorId, e);
            throw new DoctorServiceException(Constants.ERROR + " updating doctor with ID: " + doctorId);
        }
    }


    @Override
    public DoctorDetailsDTO getDoctorDetails(String doctorId) {
        try {
            Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor" + Constants.NOT_FOUND+ doctorId));

            logger.info(Constants.RETRIEVED, "Doctor" ,  doctorId);
            return DoctorBuilder.buildDoctorDetailsDTOFromDoctor(doctor);
            
        } catch (DataAccessException e) {  // Catching database-related issues
            logger.error("{} Error updating doctor with ID: {} - Exception: {}", Constants.ERROR, doctorId, e.getMessage(), e);
            throw new DoctorServiceException(Constants.ERROR + " Database error while updating doctor with ID: " + doctorId);
        } 
        catch (DoctorNotFoundException e) {  // Letting this exception pass through
            logger.info(e.getMessage());
        	throw e;
        } catch (Exception e) { // Catching unexpected exceptions
            logger.error("{} fetching Doctor details with id: {} - Exception - {}", Constants.ERROR, doctorId, e);
            throw new DoctorServiceException(Constants.ERROR + " Unexpected error while fetching Doctor details with id: " + doctorId);
        }
    }


    @Override
    public List<DoctorDetailsDTO> getAllDoctorsDeatils() {
        try {
            List<Doctor> doctors = doctorRepository.findAll();
            if (doctors.isEmpty()) {
                logger.info("No doctors found");
            }
            return doctors.stream()
                    .map(DoctorBuilder::buildDoctorDetailsDTOFromDoctor)
                    .collect(Collectors.toList());
        } catch (Exception e) {
        	logger.error(Constants.ERROR+ " Fetching All doctors deatails: {}", e.getMessage());
            throw new DoctorServiceException(Constants.ERROR+ " Fetching All doctors deatails: " + e.getMessage());
        }
    }
    
    @Override
    public List<DoctorDetailsDTO> getDoctorsByName(String name) {
        try {
            List<Doctor> doctors = doctorRepository.findByFirstNameContainingIgnoreCase(name);
            if (doctors.isEmpty()) {
                logger.warn("No doctors found with name containing: {}", name);
                throw new DoctorNotFoundException("No doctors found with name: " + name);
            }

            logger.info("Retrieved {} doctors with name containing: {}", doctors.size(), name);
            return doctors.stream()
                          .map(DoctorBuilder::buildDoctorDetailsDTOFromDoctor)
                          .collect(Collectors.toList());

        } catch (DoctorNotFoundException e) {  // Handle specific exception
            logger.warn("{}", e.getMessage());
            throw e; 

        } catch (Exception e) {  // Handle other exceptions
            logger.error("{} retrieving doctors with name: {} - exception - {}", Constants.ERROR, name, e);
            throw new DoctorServiceException(Constants.ERROR + " retrieving doctors with name: " + name);
        }
    }


    @Override
    public void deleteDoctor(String doctorId) {
        try {
        	 Doctor doctor = doctorRepository.findById(doctorId)
                     .orElseThrow(() -> new DoctorNotFoundException("Doctor" + Constants.NOT_FOUND+ doctorId));
        	 doctor.setIsEmployeeActive(false);
        	 doctorRepository.save(doctor);
            logger.info("Doctor deleted successfully with ID: {}", doctorId);

        } catch (DataAccessException e) { // Handling database-related exceptions
            logger.error("{} doctor with ID: {} - Exception: {}", Constants.ERROR, doctorId, e.getMessage());
            throw new DoctorServiceException(Constants.ERROR + " deleting doctor with ID: " + doctorId);
        }
        catch (DoctorNotFoundException e) {  // Letting this exception pass through
            logger.info(e.getMessage());
        	throw e;
        } 
        catch (Exception e) { // Catching unexpected errors
            logger.error("{} deleting doctor with ID: {} - exception - {}", Constants.ERROR, doctorId, e);
            throw new DoctorServiceException(Constants.ERROR + " deleting doctor with ID: " + doctorId);
        }
    }

}
