package com.java.model;


import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @Column(name = "room_number" , unique = true , nullable = false)
    private String roomNumber; // Unique room number (e.g., A101)
    
    @Column(name = "room_type" , nullable = false) 
    private String roomType; // E.g., ICU, General, Private, etc.
    
    @Column(nullable = false)
    private Integer capacity; // Number of beds in the room

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bed> beds; // List of beds in the room

   
}
