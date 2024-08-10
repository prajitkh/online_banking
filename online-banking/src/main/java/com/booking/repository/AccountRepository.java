package com.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
