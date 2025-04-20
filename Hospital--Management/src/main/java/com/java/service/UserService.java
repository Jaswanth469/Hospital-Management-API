package com.java.service;


import com.java.dto.LoginDTO;
import com.java.model.User;

public interface UserService {
	
	public User resetPassword(LoginDTO loginDTO);

}
