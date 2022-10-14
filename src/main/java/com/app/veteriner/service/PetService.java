package com.app.veteriner.service;

import com.app.veteriner.dto.PetDto;
import com.app.veteriner.model.Owner;
import com.app.veteriner.model.Pet;
import com.app.veteriner.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

	private OwnerService ownerService;
	private PetRepository petRepository;

	public PetService(OwnerService ownerService, PetRepository petRepository) {
		this.ownerService = ownerService;
		this.petRepository = petRepository;
	}

	public List<Pet> getAllPets() {
		return petRepository.findAll();
	}

	public Pet getOnePet(Long id) {
		return  petRepository.findById(id).
				orElseThrow(() -> new IllegalStateException("pet with " + id + " doesn't exist"));

	}
	public Pet createPet(PetDto petDto) {

		Pet pet = dtoToDomain(petDto);

		return petRepository.save(pet);
	}

	public Pet updateOnePet(Long id, PetDto petDto) {
		Optional<Pet> pet = petRepository.findById(id);
		if(pet.isPresent()){
			Pet toUpdate = pet.get();
			toUpdate.setDescription(petDto.getDescription());
			toUpdate.setGender(petDto.getGender());
			toUpdate.setSpecies(petDto.getSpecies());
			toUpdate.setName(petDto.getName());
			toUpdate.setAge(petDto.getAge());
			toUpdate.setOwner(ownerService.getOneOwner(petDto.getOwnerId()));
			petRepository.save(toUpdate);

			return toUpdate;
		}
		else{
			throw new IllegalStateException();
		}
	}

	public Pet dtoToDomain(PetDto petDto){
		Owner owner = ownerService.getOneOwner(petDto.getOwnerId());
		if(owner == null)
			return null;

		Pet pet = new Pet();
		pet.setAge(petDto.getAge());
		pet.setDescription(petDto.getDescription());
		pet.setSpecies(petDto.getSpecies());
		pet.setName(petDto.getName());
		pet.setGender(petDto.getGender());
		pet.setOwner(owner);

		return pet;
	}

	public List<Pet> searchByName(String petName){
		if(petName != null){
			return petRepository.searchByPetName(petName.toLowerCase());
		}
		return petRepository.findAll();
	}

	public void deletePetById(Long id) {
		if (petRepository.existsById(id)) {
			System.out.println("deleted with " + id );
			petRepository.deleteById(id);
		}
	}
}
