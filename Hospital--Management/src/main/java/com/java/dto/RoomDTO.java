package com.java.dto;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDTO {
	
	private String roomNumber; // Unique room number (e.g., A101)
    
    private String roomType; // E.g., ICU, General, Private, etc.
    
    private Integer capacity; // Number of beds in the room

    private List<BedDTO> bedsList; 

}
