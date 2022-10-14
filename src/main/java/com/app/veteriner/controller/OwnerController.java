package com.app.veteriner.controller;

import com.app.veteriner.model.Owner;
import com.app.veteriner.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owners")
public class OwnerController {

	@Autowired
	private final OwnerService ownerService;

	public OwnerController(OwnerService ownerService) {
		this.ownerService = ownerService;
	}

	@GetMapping("/list")
	public List<Owner> listOwners() {
		return ownerService.getAllOwners();
	}

	@GetMapping("/list/{id}")
	Owner getOneOwner(@PathVariable Long id) {
		return ownerService.getOneOwner(id);
	}

	@PostMapping
	public ResponseEntity<Owner> createOwner(@RequestBody Owner owner) {
		ownerService.saveOwner(owner);
		return ResponseEntity.status(HttpStatus.CREATED).body(owner);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Owner> updateOneOwner(@PathVariable Long id, @RequestBody Owner newOwner) {
		ownerService.updateOneOwner(id, newOwner);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePet(@PathVariable Long id) {
		ownerService.deleteOwnerById(id);
		return ResponseEntity.noContent().build();
	}

}
