package com.app.veteriner.controller;


import com.app.veteriner.dto.PetDto;
import com.app.veteriner.model.Pet;
import com.app.veteriner.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

	@Autowired
	private final PetService petService;

	public PetController(PetService petService) {
		this.petService = petService;
	}

	@GetMapping
	public ResponseEntity<List<Pet>> list() {
		return new ResponseEntity<>(petService.getAllPets(), HttpStatus.OK);
	}


	@GetMapping("/{id}")
	public ResponseEntity<Pet> getOnePet(@PathVariable Long id) {
		return new ResponseEntity<>(petService.getOnePet(id), HttpStatus.OK);
	}

	@PostMapping
	ResponseEntity<PetDto> createPet(@RequestBody PetDto petDto) {
		petService.createPet(petDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(petDto);
	}


	@PutMapping("/{id}")
	public ResponseEntity<Pet> updateOnePet(@PathVariable Long id, @RequestBody PetDto petDto) {
		petService.updateOnePet(id, petDto);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePet(@PathVariable Long id) {
		petService.deletePetById(id);
		return ResponseEntity.noContent().build();
	}


}
