package com.java.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.java.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	public User findUserByEmail(String email);

}