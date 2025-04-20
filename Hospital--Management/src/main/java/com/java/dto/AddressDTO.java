package com.java.dto;


import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

	private String street;

	private String landMark;

	private String city;

	private String postalCode;

	private String state;

	private String country;

}
