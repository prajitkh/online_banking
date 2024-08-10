package com.booking.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.booking.dto.ClientRequestDto;
import com.booking.dto.ClientResponseDto;
import com.booking.entity.Account;
import com.booking.entity.ClientEntity;
import com.booking.entity.Email;
import com.booking.entity.Phone;
import com.booking.exception.ResourceNotFoundException;
import com.booking.repository.AccountRepository;
import com.booking.repository.ClientRepository;
import com.booking.repository.EmailRepository;
import com.booking.repository.PhoneRepository;
import com.booking.service.ClientService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PhoneRepository phoneRepository;

	@Autowired
	private EmailRepository emailRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public boolean addClient(ClientRequestDto client) {
		BigDecimal bigDecimal = BigDecimal.valueOf(client.getBalance());

		if (bigDecimal.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Initial balance must be greater than zero.");
		}

		ClientEntity client1 = new ClientEntity();

		client1.setUsername(client.getUsername());
		client1.setPassword(client.getPassword());
		client1.setName(client.getName());
		client1.setDateOfBirth(client.getDateOfBirth());
		clientRepository.save(client1);

		Account account = new Account();
		account.setInitialBalance(client.getBalance());
		account.setBalance(client.getBalance());
		account.setClient(client1);
		accountRepository.save(account);

		for (String number : client.getPhoneNumbers()) {
			Phone phone = new Phone();
			phone.setClient(client1);
			phone.setNumber(number);

			phoneRepository.save(phone);
		}

		for (String email : client.getEmailAddresses()) {
			Email emailEntity = new Email();
			emailEntity.setClient(client1);
			emailEntity.setEmailAddress(email);

			emailRepository.save(emailEntity);
		}

		return true;

	}

	@Transactional
	@Override
	public void transferMoney(Long fromClientId, Long toClientId, Double amount) {

		ClientEntity fromClient = clientRepository.findById(fromClientId)
				.orElseThrow(() -> new RuntimeException("Client not found"));
		ClientEntity toClient = clientRepository.findById(toClientId)
				.orElseThrow(() -> new RuntimeException("Client not found"));

//		Account fromAccount = accountRepository.findById(fromClient.getAccount().getId())
//				.orElseThrow(() -> new RuntimeException("Account not found"));
//		Account toAccount = accountRepository.findById(toClient.getAccount().getId())
//				.orElseThrow(() -> new RuntimeException("Account not found"));

		Account fromAccount = entityManager.find(Account.class, fromClientId, LockModeType.PESSIMISTIC_WRITE);
		Account toAccount = entityManager.find(Account.class, toClientId, LockModeType.PESSIMISTIC_WRITE);

		if (fromAccount.getBalance() < amount) {
			throw new RuntimeException("Insufficient funds");
		}

		fromAccount.setBalance(fromAccount.getBalance() - amount);
		toAccount.setBalance(toAccount.getBalance() + amount);

		// Save the updated accounts
		accountRepository.save(fromAccount);
		accountRepository.save(toAccount);
	}

	@Scheduled(fixedRate = 60000)
	@Transactional
	public void applyInterest() {
		List<Account> accounts = accountRepository.findAll();

		for (Account account : accounts) {
			double initialBalance = account.getInitialBalance();
			double newBalance = account.getBalance() * 1.05;

			if (newBalance > initialBalance * 2.07) {
				newBalance = initialBalance * 2.07;
			}

			account.setBalance(newBalance);
			accountRepository.save(account);
		}

	}

	@Override
	public void removePhone(Long clientId, Long phoneId) {
		ClientEntity client = clientRepository.findById(clientId)
				.orElseThrow(() -> new ResourceNotFoundException("Client not found"));

		Phone phone = phoneRepository.findByIdAndClient(phoneId, client)
				.orElseThrow(() -> new ResourceNotFoundException("Phone not found"));

		if (client.getPhones().size() <= 1) {
			throw new IllegalArgumentException("A client must have at least one phone number.");
		}

		phoneRepository.delete(phone);
	}

	// Add an email to an existing client
	@Override
	public void addEmail(Long clientId, String email) {
		ClientEntity client = clientRepository.findById(clientId)
				.orElseThrow(() -> new ResourceNotFoundException("Client not found"));

		Email emailEntity = new Email();
		emailEntity.setEmailAddress(email);
		emailEntity.setClient(client);
		emailRepository.save(emailEntity);
	}

	// Remove an email from a client
	@Override
	@Transactional
	public void removeEmail(Long clientId, Long emailId) {
		ClientEntity client = clientRepository.findById(clientId)
				.orElseThrow(() -> new ResourceNotFoundException("Client not found"));

		Email email = emailRepository.findByIdAndClient(emailId, client)
				.orElseThrow(() -> new ResourceNotFoundException("Email not found"));

		if (client.getEmails().size() <= 1) {
			throw new IllegalArgumentException("A client must have at least one email address.");
		}

		emailRepository.delete(email);
	}

	@Override
	@Transactional
	public void addPhone(Long clientId, String phoneNumber) {
		ClientEntity client = clientRepository.findById(clientId)
				.orElseThrow(() -> new ResourceNotFoundException("Client not found"));

		if (phoneRepository.existsByNumberAndClient(phoneNumber, client)) {
			throw new IllegalArgumentException("This phone number already exists for the client.");
		}

		Phone phone = new Phone();
		phone.setNumber(phoneNumber);
		phone.setClient(client);
		phoneRepository.save(phone);
	}

	@Override
	public void modifyPhone(Long clientId, Long phoneId, String newPhoneNumber) {
		ClientEntity client = clientRepository.findById(clientId)
				.orElseThrow(() -> new ResourceNotFoundException("Client not found"));

		Phone phone = phoneRepository.findByIdAndClient(phoneId, client)
				.orElseThrow(() -> new ResourceNotFoundException("Phone not found"));

		if (phoneRepository.existsByNumberAndClient(newPhoneNumber, client)) {
			throw new IllegalArgumentException("This phone number already exists for the client.");
		}

		phone.setNumber(newPhoneNumber);
		phoneRepository.save(phone);

	}

	@Override
	public Page<ClientResponseDto> searchClients(String name, String phone, String email, LocalDate dob, int pageNo,
			int pageSize) {

		PageRequest pageable = PageRequest.of(pageNo, pageSize);

		Page<ClientResponseDto> findAllRecords = clientRepository.findAllRecords(name, phone, email, dob, pageable,
				ClientResponseDto.class);

		return findAllRecords;
	}

}
