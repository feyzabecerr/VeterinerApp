package com.app.veteriner.service;

import com.app.veteriner.model.Owner;
import com.app.veteriner.repository.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {

	private final OwnerRepository ownerRepository;

	public OwnerService(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	public Owner getOneOwner(Long id) {
		return  ownerRepository.findById(id).
				orElseThrow(() -> new IllegalStateException("owner with " + id + " doesn't exist"));
	}

	public void saveOwner(Owner owner) {
		ownerRepository.save(owner);
	}

	public List<Owner> getAllOwners() {
		return ownerRepository.findAll();
	}

	public void updateOneOwner(Long id, Owner newOwner) {
		Optional<Owner> owner = ownerRepository.findById(id);
		if (owner.isPresent()) {
			Owner foundOwner = owner.get();
			foundOwner.setEmail(newOwner.getEmail());
			foundOwner.setFirstName(newOwner.getFirstName());
			foundOwner.setLastName(newOwner.getLastName());
			foundOwner.setPhoneNumber(newOwner.getPhoneNumber());
			ownerRepository.save(foundOwner);

		} else {
			throw new IllegalStateException();
		}

	}

	public List<Owner> searchByFirstNameOrLastName(String keyword){
		if(keyword != null){
			return ownerRepository.searchByFirstNameOrLastName(keyword.toLowerCase());
		}
		return ownerRepository.findAll();
	}
	public void deleteOwnerById(Long id) {
		if (ownerRepository.existsById(id)) {
			System.out.println("deleted with " + id );
			ownerRepository.deleteById(id);
		}
	}
}
