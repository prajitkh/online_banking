package com.booking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.entity.ClientEntity;
import com.booking.entity.Phone;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

	Optional<Phone> findByIdAndClient(Long phoneId, ClientEntity client);

	boolean existsByNumberAndClient(String phoneNumber, ClientEntity client);

}
