package com.booking.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booking.dto.ClientRequestDto;
import com.booking.dto.ClientResponseDto;
import com.booking.service.ClientService;

@RestController
@RequestMapping("/api")
public class ClientController {

	@Autowired
	private ClientService clientService;

	@PostMapping("/create")
	public ResponseEntity<?> createClient(@RequestBody ClientRequestDto request) {

		this.clientService.addClient(request);
		return new ResponseEntity<>("client added successfully", HttpStatus.OK);

	}

	@PostMapping("/{fromClientId}/transfer/{toClientId}")
	public ResponseEntity<String> transferMoney(@PathVariable Long fromClientId, @PathVariable Long toClientId,
			@RequestParam Double amount) {

		System.out.println("service methid");
		clientService.transferMoney(fromClientId, toClientId, amount);
		return ResponseEntity.ok("Money transferred successfully.");
	}

	@PostMapping("/{clientId}/phones")
	public ResponseEntity<String> addPhone(@PathVariable Long clientId, @RequestBody String phoneNumber) {
		clientService.addPhone(clientId, phoneNumber);
		return ResponseEntity.ok("Phone added successfully");
	}

	@PutMapping("/{clientId}/phones/{phoneId}")
	public ResponseEntity<String> modifyPhone(@PathVariable Long clientId, @PathVariable Long phoneId,
			@RequestBody String newPhoneNumber) {
		clientService.modifyPhone(clientId, phoneId, newPhoneNumber);
		return ResponseEntity.ok("Phone modified successfully");
	}

	@DeleteMapping("/{clientId}/phones/{phoneId}")
	public ResponseEntity<String> removePhone(@PathVariable Long clientId, @PathVariable Long phoneId) {
		clientService.removePhone(clientId, phoneId);
		return ResponseEntity.ok("Phone removed successfully");
	}

	@PostMapping("/{clientId}/emails")
	public ResponseEntity<String> addEmail(@PathVariable Long clientId, @RequestBody String email) {
		clientService.addEmail(clientId, email);
		return ResponseEntity.ok("Email added successfully");
	}

	@DeleteMapping("/{clientId}/emails/{emailId}")
	public ResponseEntity<String> removeEmail(@PathVariable Long clientId, @PathVariable Long emailId) {
		clientService.removeEmail(clientId, emailId);
		return ResponseEntity.ok("Email removed successfully");
	}

	@GetMapping("/search")
	public ResponseEntity<?> searchClients(@RequestParam(required = false) String name,
			@RequestParam(required = false) String phone, @RequestParam(required = false) String email,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob,
			@RequestParam(defaultValue = "", value = "pageNo") int pageNo,
			@RequestParam(defaultValue = "", value = "pageSize") int pageSize) {

		org.springframework.data.domain.Page<ClientResponseDto> clients = clientService.searchClients(name, phone,
				email, dob, pageNo, pageSize);
		return ResponseEntity.ok(clients);
	}

}
