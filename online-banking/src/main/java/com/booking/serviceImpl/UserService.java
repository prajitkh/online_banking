package com.booking.serviceImpl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.booking.configuration.RequestConfiguration;
import com.booking.dto.UserDto;
import com.booking.entity.ClientEntity;
import com.booking.repository.ClientRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private RequestConfiguration configuration;

	public String getInfo(UserDto dto) {

		ClientEntity entity = clientRepository.findByNameOrUsername(dto.getName(), dto.getName());
		String generateToken = null;

		if (entity.getUsername().equals(dto.getName())
				|| entity.getName().equals(dto.getName()) && entity.getPassword().equals(dto.getPass())) {
			UserDetails loadUserByUsername = this.loadUserByUsername(dto.getName());

			generateToken = configuration.generateToken(loadUserByUsername);

		}
		return generateToken;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ClientEntity entity = clientRepository.findByNameOrUsername(username, username);

		if (entity.getName().equals(username) || entity.getUsername().equals(username)) {
			return new User(entity.getName(), entity.getPassword(), new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
}
