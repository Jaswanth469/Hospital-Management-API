package com.java.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id")
	private Long addressId;
	
	@Column(nullable = false)
	private String street;
	
	private String landMark;
	
	@Column(nullable = false)
	private String city;
	
	@Column(name = "postal_code" , nullable = false)
	private String postalCode;
	
	@Column(nullable = false)
	private String state;
	
	@Column(nullable = false)
	private String country;

	public Address(String street, String landMark, String city, String postalCode, String state, String country) {
		super();
		this.street = street;
		this.landMark = landMark;
		this.city = city;
		this.postalCode = postalCode;
		this.state = state;
		this.country = country;
	}
	
}
