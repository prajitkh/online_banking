package com.booking.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.booking.dto.ClientRequestDto;
import com.booking.dto.ClientResponseDto;

@Service
public interface ClientService {

	public boolean addClient(ClientRequestDto clientRequestDto);

	public void transferMoney(Long fromClientId, Long toClientId, Double amount);

	public void removePhone(Long clientId, Long phoneId);

	public void addEmail(Long clientId, String email);

	public void removeEmail(Long clientId, Long emailId);

	public void addPhone(Long clientId, String phoneNumber);

	public void modifyPhone(Long clientId, Long phoneId, String newPhoneNumber);

	public Page<ClientResponseDto> searchClients(String name, String phone, String email, LocalDate dob, int pageNo,
			int pageSize);

}
