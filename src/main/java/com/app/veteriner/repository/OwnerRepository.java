package com.app.veteriner.repository;

import com.app.veteriner.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

	@Query("SELECT o FROM Owner o WHERE CONCAT(lower(o.firstName), lower(o.lastName)) LIKE %?1%")
	public List<Owner> searchByFirstNameOrLastName(String keyword);

}
