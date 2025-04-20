package com.java.service.impl;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.builder.BedBuilder;
import com.java.builder.RoomBuilder;
import com.java.dao.BedAssignmentHistoryRepository;
import com.java.dao.BedRepository;
import com.java.dao.PatientRepository;
import com.java.dao.RoomRepository;
import com.java.dto.BedDTO;
import com.java.dto.RoomDTO;
import com.java.exception.BedAssignmentNotFoundException;
import com.java.exception.BedNotAvailableException;
import com.java.exception.BedNotFoundException;
import com.java.exception.PatientNotFoundException;
import com.java.exception.RoomNotFoundException;
import com.java.model.Bed;
import com.java.model.BedAssignmentHistory;
import com.java.model.Patient;
import com.java.model.Room;
import com.java.service.RoomService;

@Service
public class RoomServiceImpl implements RoomService {
    
    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;
    private final PatientRepository patientRepository;
    private final BedAssignmentHistoryRepository historyRepository;
    
    private Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);
    
    public RoomServiceImpl(RoomRepository roomRepository, BedRepository bedRepository,
			PatientRepository patientRepository, BedAssignmentHistoryRepository historyRepository) {
		super();
		this.roomRepository = roomRepository;
		this.bedRepository = bedRepository;
		this.patientRepository = patientRepository;
		this.historyRepository = historyRepository;
	}

	@Override
    public RoomDTO addRoom(RoomDTO roomDTO) {
        logger.info("Adding new room: {}", roomDTO);
        Room room = RoomBuilder.buildRoomFromDTO(roomDTO);
        Room savedRoom = roomRepository.save(room);
        logger.info("Room added successfully: {}", savedRoom.getRoomNumber());
        return RoomBuilder.buildDTOFromRoom(savedRoom);
    }

    @Override
    public BedDTO addBedToRoom(String roomNumber, BedDTO bedDTO) {
        logger.info("Adding new bed to room {}: {}", roomNumber, bedDTO);
        Room room = roomRepository.findById(roomNumber)
                .orElseThrow(() -> new RoomNotFoundException("Room not found: " + roomNumber));
        Bed bed = BedBuilder.buildBedFromDto(bedDTO, room);
        Bed savedBed = bedRepository.save(bed);
        logger.info("Bed {} added to room {}", savedBed.getBedNumber(), roomNumber);
        return BedBuilder.buildBedDTOFromBed(savedBed);
    }
    
    @Transactional
    @Override
    public RoomDTO updateRoom(String roomNumber, RoomDTO updatedRoomDTO) {
        logger.info("Updating room details for room {}", roomNumber);
        
        Room existingRoom = roomRepository.findById(roomNumber)
                .orElseThrow(() -> {
                    logger.error("Room not found: {}", roomNumber);
                    return new RoomNotFoundException("Room not found: " + roomNumber);
                });

        existingRoom.setRoomType(updatedRoomDTO.getRoomType());
        existingRoom.setCapacity(updatedRoomDTO.getCapacity());

        Room updatedRoom = roomRepository.save(existingRoom);
        logger.info("Room {} updated successfully", roomNumber);
        
        return RoomBuilder.buildDTOFromRoom(updatedRoom);
    }

    @Transactional
    @Override
    public void deleteRoom(String roomNumber) {
        logger.info("Deleting room {}", roomNumber);

        Room room = roomRepository.findById(roomNumber)
                .orElseThrow(() -> {
                    logger.error("Room not found: {}", roomNumber);
                    return new RoomNotFoundException("Room not found: " + roomNumber);
                });

        roomRepository.delete(room);
        logger.info("Room {} deleted successfully", roomNumber);
    }

    @Transactional
    @Override
    public BedDTO updateBed(String bedNumber, BedDTO updatedBedDTO) {
        logger.info("Updating bed details for bed {}", bedNumber);

        Bed existingBed = bedRepository.findById(bedNumber)
                .orElseThrow(() -> {
                    logger.error("Bed not found: {}", bedNumber);
                    return new BedNotFoundException("Bed not found: " + bedNumber);
                });

        existingBed.setIsOccupied(updatedBedDTO.getIsOccupied());

        Bed updatedBed = bedRepository.save(existingBed);
        logger.info("Bed {} updated successfully", bedNumber);
        
        return BedBuilder.buildBedDTOFromBed(updatedBed);
    }

    @Transactional
    @Override
    public void deleteBed(String bedNumber) {
        logger.info("Deleting bed {}", bedNumber);

        Bed bed = bedRepository.findById(bedNumber)
                .orElseThrow(() -> {
                    logger.error("Bed not found: {}", bedNumber);
                    return new BedNotFoundException("Bed not found: " + bedNumber);
                });

        bedRepository.delete(bed);
        logger.info("Bed {} deleted successfully", bedNumber);
    }


    @Transactional
    @Override
    public void assignBedToPatient(String bedNumber, String patientId) {
        logger.info("Assigning bed {} to patient {}", bedNumber, patientId);
        Bed bed = bedRepository.findById(bedNumber)
                .orElseThrow(() -> new BedNotFoundException("Bed not found: " + bedNumber));
        if (bed.getIsOccupied()) {
            throw new BedNotAvailableException("Bed is already occupied: " + bedNumber);
        }
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found: " + patientId));
        bed.setIsOccupied(true);
        bed.setPatient(patient);
        bedRepository.save(bed);

        BedAssignmentHistory history = new BedAssignmentHistory(null, bed, patient, LocalDateTime.now(), null);
        historyRepository.save(history);
        logger.info("Bed {} successfully assigned to patient {}", bedNumber, patientId);
    }

    @Transactional
    @Override
    public void vacateBed(String bedNumber) {
        logger.info("Vacating bed {}", bedNumber);
        Bed bed = bedRepository.findById(bedNumber)
                .orElseThrow(() -> new BedNotFoundException("Bed not found: " + bedNumber));
        if (!bed.getIsOccupied()) {
            logger.warn("Bed {} is already vacant", bedNumber);
            return;
        }
        bed.setIsOccupied(false);
        bed.setPatient(null);
        bedRepository.save(bed);

        BedAssignmentHistory history = historyRepository.findByBedAndVacatedAtIsNull(bed)
                .orElseThrow(() -> new BedAssignmentNotFoundException("No active assignment found for bed: " + bedNumber));
        history.setVacatedAt(LocalDateTime.now());
        historyRepository.save(history);
        logger.info("Bed {} vacated successfully", bedNumber);
    }

    @Override
    public List<BedDTO> getAvailableBedsInRoom(String roomNumber) {
        logger.info("Fetching available beds for room {}", roomNumber);
        List<Bed> availableBeds = bedRepository.findByRoom_RoomNumberAndIsOccupiedFalse(roomNumber);
        return availableBeds.stream()
                .map(BedBuilder::buildBedDTOFromBed)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BedDTO> getAllAvailableBeds(){
    	logger.info("Fetching available beds");
    	List<Bed> availableBeds = bedRepository.findByIsOccupiedFalse();
    	return availableBeds.stream()
                .map(BedBuilder::buildBedDTOFromBed)
                .collect(Collectors.toList());
    }
    
    @Override
    public BedDTO getBedDetailsByPatientId(String patientId) {
        logger.info("Fetching bed details for patient {}", patientId);
        
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->  new PatientNotFoundException("Patient not found: " + patientId));

        Bed bed = bedRepository.findByPatient(patient)
                .orElseThrow(() -> new BedNotFoundException("No bed assigned to patient: " + patientId) );

        logger.info("Patient {} has bed {}", patientId, bed.getBedNumber());
        return BedBuilder.buildBedDTOFromBed(bed);
    }

}
