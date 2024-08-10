package com.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.booking.configuration.RequestConfiguration;
import com.booking.dto.TokenResponse;
import com.booking.dto.UserDto;
import com.booking.serviceImpl.UserService;

@RestController
//@RequestMapping("/api")
public class UserController {
	@Autowired
	private RequestConfiguration configuration;

	@Autowired
	private UserService service;

	// login and genrate token
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserDto users) {

		try {
			String generateToken = this.service.getInfo(users);

			return new ResponseEntity<>(new TokenResponse("User login successfully", generateToken), HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

}
