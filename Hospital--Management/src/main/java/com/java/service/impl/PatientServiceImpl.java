package com.java.service.impl;


import java.util.List;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.java.builder.PatientBuilder;
import com.java.dao.PatientRepository;
import com.java.dto.RegisterPatientDTO;
import com.java.dto.PatientDetailsDTO;
import com.java.exception.PatientNotFoundException;
import com.java.exception.PatientServiceException;
import com.java.model.Patient;
import com.java.service.PatientService;
import com.java.util.Constants;

@Service
public class PatientServiceImpl implements PatientService {
    
    private final PatientRepository patientRepository;
    
    private final Logger logger = LoggerFactory.getLogger(PatientServiceImpl.class);

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public PatientDetailsDTO savePatient(RegisterPatientDTO patientDto) {
        try {
            Patient patient = PatientBuilder.buildPatientFromDTO(patientDto);
            Patient savedPatient = patientRepository.save(patient);
            logger.info("Patient {}", savedPatient.getPatientId());
            return PatientBuilder.buildPatientDetailsDTOFromPatient(savedPatient);
        } catch (Exception e) {
            logger.error("{} saving patient: {}", Constants.ERROR, e.getMessage());
            throw new PatientServiceException(Constants.ERROR + " saving patient: " + e.getMessage());
        }
    }

    @Override
    public PatientDetailsDTO updatePatient(String patientId, RegisterPatientDTO patientDto) {
        try {
            Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient " + Constants.NOT_FOUND + patientId));

            logger.info(Constants.RETRIEVED, "Patient", patientId);

            Patient updatedPatient = PatientBuilder.buildPatientFromDTO(patientDto);
            updatedPatient.setPatientId(existingPatient.getPatientId());
            updatedPatient.getAddress().setAddressId(existingPatient.getAddress().getAddressId());

            patientRepository.save(updatedPatient);
            logger.info("Patient {} {}",Constants.UPDATED, patientId);

            return PatientBuilder.buildPatientDetailsDTOFromPatient(updatedPatient);

        } catch (DataAccessException e) {
            logger.error("{} updating patient with ID: {} - Exception: {}", Constants.ERROR, patientId, e.getMessage());
            throw new PatientServiceException(Constants.ERROR + " updating patient with ID: " + patientId);
        } catch (PatientNotFoundException e) {
            logger.warn(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("{} updating patient with ID: {} - Exception - {}", Constants.ERROR, patientId, e);
            throw new PatientServiceException(Constants.ERROR + " updating patient with ID: " + patientId);
        }
    }

    @Override
    public PatientDetailsDTO getPatientDetails(String patientId) {
        try {
            Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient " + Constants.NOT_FOUND + patientId));

            logger.info("Patient {} retrieved successfully", patientId);
            return PatientBuilder.buildPatientDetailsDTOFromPatient(patient);
            
        } catch (DataAccessException e) {
            logger.error("{} fetching patient details with ID: {} - Exception: {}", Constants.ERROR, patientId, e.getMessage());
            throw new PatientServiceException(Constants.ERROR + " fetching patient details with ID: " + patientId);
        } catch (PatientNotFoundException e) {
            logger.warn(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("{} fetching patient details with ID: {} - Exception - {}", Constants.ERROR, patientId, e);
            throw new PatientServiceException(Constants.ERROR + " fetching patient details with ID: " + patientId);
        }
    }

    @Override
    public List<PatientDetailsDTO> getAllPatientDetails() {
        try {
            List<Patient> patientList = patientRepository.findAll();
            if (patientList.isEmpty()) {
                logger.info("No patients found");
            }
            return patientList.stream()
                    .map(PatientBuilder::buildPatientDetailsDTOFromPatient)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("{} Fetching all patient details: {}", Constants.ERROR, e.getMessage());
            throw new PatientServiceException(Constants.ERROR + " Fetching all patient details: " + e.getMessage());
        }
    }
    
    @Override
    public List<PatientDetailsDTO> getPatientsByName(String name) {
        try {
            List<Patient> patientList = patientRepository.findByFirstNameContainingIgnoreCase(name);
            if (patientList.isEmpty()) {
                logger.warn("No patients found with name containing: {}", name);
                throw new PatientNotFoundException("No patients found with name: " + name);
            }

            logger.info("Retrieved {} patients with name containing: {}", patientList.size(), name);
            return patientList.stream()
                          .map(PatientBuilder::buildPatientDetailsDTOFromPatient)
                          .collect(Collectors.toList());

        } catch (PatientNotFoundException e) {
            logger.warn(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("{} retrieving patients with name: {} - exception - {}", Constants.ERROR, name, e);
            throw new PatientServiceException(Constants.ERROR + " retrieving patients with name: " + name);
        }
    }

    @Override
    public void deletePatient(String patientId) {
        try {
            if (!patientRepository.existsById(patientId)) {
                logger.warn("Patient {} not found", patientId);
                throw new PatientNotFoundException("Patient " + Constants.NOT_FOUND + patientId);
            }

            patientRepository.deleteById(patientId);
            logger.info("Patient {} {}",Constants.DELETED,  patientId);

        } catch (DataAccessException e) {
            logger.error("{} deleting patient with ID: {} - Exception: {}", Constants.ERROR, patientId, e.getMessage());
            throw new PatientServiceException(Constants.ERROR + " deleting patient with ID: " + patientId);
        } catch (PatientNotFoundException e) {
            logger.warn(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("{} deleting patient with ID: {} - exception - {}", Constants.ERROR, patientId, e);
            throw new PatientServiceException(Constants.ERROR + " deleting patient with ID: " + patientId);
        }
    }

	@Override
	public PatientDetailsDTO getPatientByPhone(String phoneNumber) {
		try {
			Patient patient = patientRepository.findByPhoneNumber(phoneNumber)
							.orElseThrow(() -> new PatientNotFoundException("Patient Not Found With Phone Number : " + phoneNumber));
			
			logger.info("Patient {} retrieved successfully", patient.getPatientId());
	        return PatientBuilder.buildPatientDetailsDTOFromPatient(patient);
		}catch (DataAccessException e) {
            logger.error("{} fetching patient details with Phone Number: {} - Exception: {}", Constants.ERROR, phoneNumber, e.getMessage());
            throw new PatientServiceException(Constants.ERROR + " fetching patient details with Phone Number: " + phoneNumber);
        } 
		catch (PatientNotFoundException e) {
            logger.warn(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("{} fetching patient details with Phone Number: {} - Exception - {}", Constants.ERROR, phoneNumber, e);
            throw new PatientServiceException(Constants.ERROR + " fetching patient details with Phone number: " + phoneNumber);
        }
	}
}
