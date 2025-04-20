package com.java.service.impl;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.java.dao.UserRepository;
import com.java.dto.LoginDTO;
import com.java.exception.InvalidCredentialsException;
import com.java.model.User;
import com.java.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	private UserRepository userRepository;
	
	private PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public User resetPassword(LoginDTO loginDTO) {
		User user = userRepository.findUserByEmail(loginDTO.getUserEmail());
		
		if(user==null) {
			throw new InvalidCredentialsException("Invalid email");
		}
		
		user.setPassword(passwordEncoder.encode(loginDTO.getPassword()));
		User savedUser = userRepository.save(user);
		return savedUser;
	}

}
