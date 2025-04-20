package com.java.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BedDTO {

	private String bedNumber;

	private Boolean isOccupied;

	private String roomNumber; // Associated room for this bed

}
