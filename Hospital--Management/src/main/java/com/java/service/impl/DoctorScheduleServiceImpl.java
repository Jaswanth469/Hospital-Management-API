package com.java.service.impl;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.dao.DoctorRepository;
import com.java.dao.DoctorScheduleRepository;
import com.java.exception.DoctorNotFoundException;
import com.java.exception.DoctorStatusUpdateFailedException;
import com.java.exception.InvalidDateException;
import com.java.model.Doctor;
import com.java.model.DoctorSchedule;
import com.java.service.DoctorScheduleService;
import com.java.util.Constants;

@Service
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    DoctorScheduleRepository doctorScheduleRepository;
    DoctorRepository doctorRepository;
    
    
    
    public DoctorScheduleServiceImpl(DoctorScheduleRepository doctorScheduleRepository,
			DoctorRepository doctorRepository) {
		this.doctorScheduleRepository = doctorScheduleRepository;
		this.doctorRepository = doctorRepository;
	}

	public static final Logger logger = LoggerFactory.getLogger(DoctorScheduleServiceImpl.class);

	@Transactional
	public void markDoctorUnavailable(String doctorId, List<LocalDate> dates) {
	    try {
	        logger.info("Marking doctor with ID {} as unavailable for dates: {}", doctorId, dates);
	        
	        validateDates(dates);

	        Doctor doctor = doctorRepository.findById(doctorId)
	                .orElseThrow(() -> new DoctorNotFoundException("Doctor " + Constants.NOT_FOUND + doctorId));

	        // Fetch existing unavailable dates
	        List<LocalDate> existingUnavailableDates = doctorScheduleRepository
	                .findUnavailableDatesByDoctorId(doctorId);

	        // Filter out already unavailable dates
	        List<LocalDate> newDates = dates.stream()
	                .filter(date -> !existingUnavailableDates.contains(date))
	                .collect(Collectors.toList());

	        if (newDates.isEmpty()) {
	            logger.warn("No new unavailable dates to update for doctor {}", doctorId);
	            throw new DoctorStatusUpdateFailedException("No new unavailable dates to update for doctor : " + doctorId);
	        }

	        List<DoctorSchedule> unavailableDates = newDates.stream()
	                .map(date -> new DoctorSchedule(null, doctor, date))
	                .collect(Collectors.toList());

	        doctorScheduleRepository.saveAll(unavailableDates);

	        logger.info("Successfully marked doctor {} as unavailable for new dates: {}", doctorId, newDates);
	    } catch (DoctorNotFoundException e) {
	        logger.error("Doctor " + Constants.NOT_FOUND + " {} - Exception - {}", doctorId, e.getMessage());
	        throw e;
	    }catch(InvalidDateException e) {
        	logger.error(e.getMessage());
        	throw e;
        } 
	    catch(DoctorStatusUpdateFailedException e) {
	    	throw e;
	    }
	    catch (Exception e) {
	    	logger.error(Constants.ERROR +" marking doctor {} as unavailable", doctorId, e);
	        throw new DoctorStatusUpdateFailedException(Constants.ERROR + " marking doctor as unavailable - Exception - " + e.getMessage());
	    }
	}


    @Transactional
    public void removeUnavailableDates(String doctorId, List<LocalDate> dates) {
        try {
            logger.info("Removing unavailable dates for doctor with ID {}: {}", doctorId, dates);
            
            validateDates(dates);

            if (!doctorRepository.existsById(doctorId)) {
                throw new DoctorNotFoundException("Doctor not found with ID: " + doctorId);
            }

            doctorScheduleRepository.deleteByDoctor_StaffIdAndUnavailableDateIn(doctorId, dates);

            logger.info("Successfully removed unavailable dates for doctor {}", doctorId);
        } catch (DoctorNotFoundException e) {
            logger.error("Doctor " + Constants.NOT_FOUND + " {} - Exception - {}", doctorId, e.getMessage());
            throw e;
        } catch(InvalidDateException e) {
        	logger.error(e.getMessage());
        	throw e;
        }
        catch (Exception e) {
	        logger.error(Constants.ERROR +" marking doctor {} as unavailable", doctorId, e);
	        throw new DoctorStatusUpdateFailedException(Constants.ERROR + " marking doctor as unavailable - Exception - " + e.getMessage());
	    }
    }

    public boolean isDoctorAvailable(String doctorId, LocalDate date) {
        try {
            logger.info("Checking availability for doctor ID {} on {}", doctorId, date);
            
            validateDate(date);

            if (!doctorRepository.existsById(doctorId)) {
                throw new DoctorNotFoundException("Doctor not found with ID: " + doctorId);
            }

            boolean available = !doctorScheduleRepository.existsByDoctor_StaffIdAndUnavailableDate(doctorId, date);

            logger.info("Doctor {} is {} on {}", doctorId, available ? "AVAILABLE" : "UNAVAILABLE", date);
            return available;
        } catch (DoctorNotFoundException e) {
            logger.error("Doctor " + Constants.NOT_FOUND + " {} - Exception - {}", doctorId, e.getMessage());
            throw e;
        }catch(InvalidDateException e) {
        	logger.error(e.getMessage());
        	throw e;
        }
        catch (Exception e) {
	        logger.error(Constants.ERROR +" fetching doctor {} availability {}", doctorId, e);
	        throw new DoctorStatusUpdateFailedException(Constants.ERROR + " fetching doctor availability - Exception - " + e.getMessage());
	    }
    }
    
    private void validateDates(List<LocalDate> dates) {
        LocalDate today = LocalDate.now();
        if (dates.stream().anyMatch(date -> date.isBefore(today))) {
            throw new InvalidDateException("Dates must be today or later.");
        }
    }

    private void validateDate(LocalDate date) {
        LocalDate today = LocalDate.now();
        if (date.isBefore(today)) {
            throw new InvalidDateException("Date must be today or later.");
        }
    }
}
