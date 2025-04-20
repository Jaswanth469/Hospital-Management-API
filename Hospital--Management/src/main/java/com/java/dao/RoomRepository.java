package com.java.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.java.model.Room;

public interface RoomRepository extends JpaRepository<Room, String>{

}
