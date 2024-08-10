package com.booking.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.booking.dto.ClientResponseDto;
import com.booking.entity.ClientEntity;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

//	@Query(value = "select c.id as  clientId , c.name , c.username,e.email_address, p.number  from client c \r\n"
//			+ "join email e on e.client_id =c.id\r\n" + "join phone p on p.client_id=c.id", nativeQuery = true)
//
//	Page<ClientResponseDto> findAllRecords(String name, String phone, String email, LocalDate dob, PageRequest pageable,
//			Class<ClientResponseDto> class1);

	@Query(value = "SELECT c.id AS clientId, c.name, c.username, e.email_address AS email, p.number AS phone "
			+ "FROM client c " + "JOIN email e ON e.client_id = c.id " + "JOIN phone p ON p.client_id = c.id "
			+ "WHERE (:name IS NULL OR c.name LIKE :name%) " + "AND (:phone IS NULL OR p.number = :phone) "
			+ "AND (:email IS NULL OR e.email_address = :email) "
			+ "AND (:dob IS NULL OR c.dob >= :dob)", countQuery = "SELECT COUNT(*) FROM client c "
					+ "JOIN email e ON e.client_id = c.id " + "JOIN phone p ON p.client_id = c.id "
					+ "WHERE (:name IS NULL OR c.name LIKE :name%) " + "AND (:phone IS NULL OR p.number = :phone) "
					+ "AND (:email IS NULL OR e.email_address = :email) "
					+ "AND (:dob IS NULL OR c.dob >= :dob)", nativeQuery = true)
	Page<ClientResponseDto> findAllRecords(@Param("name") String name, @Param("phone") String phone,
			@Param("email") String email, @Param("dob") LocalDate dob, Pageable pageable,
			Class<ClientResponseDto> projection);

	ClientEntity findByNameOrUsername(String name, String username);
}
