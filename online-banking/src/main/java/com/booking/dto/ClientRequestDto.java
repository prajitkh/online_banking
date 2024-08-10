package com.booking.dto;

import java.time.LocalDate;
import java.util.Set;

public class ClientRequestDto {

	// @NotNull(message = "Username is required")
	// @Size(min = 4, max = 20, message = "Username must be between 4 and 20
	// characters")
	private String username;

	// @NotNull(message = "Password is required")
	// @Size(min = 8, max = 100, message = "Password must be at least 8 characters")
	private String password;

	// @NotNull(message = "Name is required")
	// @Size(min = 2, max = 50, message = "Name must be between 2 and 50
	// characters")
	private String name;

	// @NotNull(message = "Date of birth is required")
	// @Past(message = "Date of birth must be in the past")
	private LocalDate dateOfBirth;

	// @NotNull(message = "Initial balance is required")
	// @DecimalMin(value = "0.01", message = "Initial balance must be greater than
	// zero")
	private Double balance;

	// @NotEmpty(message = "At least one phone number is required")
	private Set<String> phoneNumbers;

	// @NotEmpty(message = "At least one email address is required")
	private Set<String> emailAddresses;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Set<String> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(Set<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public Set<String> getEmailAddresses() {
		return emailAddresses;
	}

	public void setEmailAddresses(Set<String> emailAddresses) {
		this.emailAddresses = emailAddresses;
	}

}
