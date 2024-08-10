package com.booking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.entity.ClientEntity;
import com.booking.entity.Email;

public interface EmailRepository extends JpaRepository<Email, Long> {

	Optional<Email> findByIdAndClient(Long emailId, ClientEntity client);

}
