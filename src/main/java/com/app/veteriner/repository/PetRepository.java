package com.app.veteriner.repository;

import com.app.veteriner.dto.PetDto;
import com.app.veteriner.model.Owner;
import com.app.veteriner.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {

	List<Pet> findByOwnerId(Long ownerId);

	@Query("SELECT p FROM Pet p WHERE lower(p.name) LIKE %?1%")
	public List<Pet> searchByPetName(String name);


}
