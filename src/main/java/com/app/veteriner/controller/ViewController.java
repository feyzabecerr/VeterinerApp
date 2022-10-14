package com.app.veteriner.controller;

import com.app.veteriner.dto.PetDto;
import com.app.veteriner.model.Owner;
import com.app.veteriner.model.Pet;
import com.app.veteriner.service.OwnerService;
import com.app.veteriner.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("")
public class ViewController {

	@Autowired
	OwnerService ownerService;

	@Autowired
	PetService petService;

	@GetMapping("/owner")
	public ModelAndView listOwners(@Param("keyword") String keyword) {
		ModelAndView modelAndView = new ModelAndView("owners");
		List<Owner> ownerList = ownerService.searchByFirstNameOrLastName(keyword);
		modelAndView.addObject("owners", ownerList);
		Owner newOwner = new Owner();
		modelAndView.addObject("newOwner", newOwner);
		modelAndView.addObject("keyword", keyword);

		return modelAndView;
	}

	@GetMapping("/pet")
	public ModelAndView listPets(@Param("petName") String petName) {
		ModelAndView modelAndView = new ModelAndView("pets");
		List<Pet> petList = petService.searchByName(petName);
		modelAndView.addObject("pets", petList);

		List<Owner> ownerList = ownerService.getAllOwners();
		modelAndView.addObject("owners", ownerList);
		Pet pet = new Pet();
		;
		modelAndView.addObject("pet", pet);

		Owner owner = new Owner();
		modelAndView.addObject("owner", owner);

		modelAndView.addObject("petName", petName);

		return modelAndView;
	}

	@GetMapping("/owner/{id}")
	public ModelAndView listOneOwner(@PathVariable Long id) {
		ModelAndView modelAndView = new ModelAndView("ownerItem");
		Owner owner = ownerService.getOneOwner(id);
		modelAndView.addObject("owner", owner);

		if (!owner.getPetList().isEmpty()) {
			List<Pet> petList = owner.getPetList();
			modelAndView.addObject("pets", petList);
		}
		return modelAndView;
	}

	@GetMapping("/pet/{id}")
	public ModelAndView listOnePet(@PathVariable Long id) {
		ModelAndView modelAndView = new ModelAndView("petItem");
		Pet pet = petService.getOnePet(id);
		modelAndView.addObject("pet", pet);

		return modelAndView;
	}

	@GetMapping("/newForm")
	public ModelAndView newForm() {
		ModelAndView modelAndView = new ModelAndView("newForm");

		List<Owner> ownerList = ownerService.getAllOwners();
		modelAndView.addObject("owners", ownerList);

		Pet pet = new Pet();
		Owner owner = new Owner();
		modelAndView.addObject("owner", owner);
		modelAndView.addObject("pet", pet);

		return modelAndView;
	}

	@PostMapping("/saveOwner")
	public RedirectView saveOwner(@ModelAttribute Owner owner) {
		ownerService.saveOwner(owner);
		RedirectView redirect = new RedirectView();
		redirect.setUrl("http://localhost:8080/owner");

		return redirect;
	}

	@PostMapping("/updateOwner/{id}")
	public RedirectView updateOwner(@PathVariable Long id, @ModelAttribute Owner owner) {
		ownerService.updateOneOwner(id, owner);
		RedirectView redirect = new RedirectView();
		redirect.setUrl("http://localhost:8080/owner");

		return redirect;
	}

	@PostMapping("/updatePet/{id}")
	public RedirectView updatePet(@PathVariable Long id, @ModelAttribute PetDto petDto) {
		petService.updateOnePet(id, petDto);
		RedirectView redirect = new RedirectView();
		redirect.setUrl("http://localhost:8080/pet");

		return redirect;
	}

	@PostMapping("/savePet")
	public RedirectView savePet(@ModelAttribute PetDto petDto) {
		petService.createPet(petDto);
		RedirectView redirect = new RedirectView();
		redirect.setUrl("http://localhost:8080/pet");
		return redirect;
	}

	@PostMapping("/delete/{id}")
	public RedirectView deleteOwner(@PathVariable Long id) {
		ownerService.deleteOwnerById(id);
		RedirectView redirect = new RedirectView();
		redirect.setUrl("http://localhost:8080/owner");
		return redirect;
	}

	@PostMapping("/pet/delete/{id}")
	public RedirectView deletePet(@PathVariable Long id) {
		petService.deletePetById(id);
		RedirectView redirect = new RedirectView();
		redirect.setUrl("http://localhost:8080/pet");
		return redirect;
	}
}
