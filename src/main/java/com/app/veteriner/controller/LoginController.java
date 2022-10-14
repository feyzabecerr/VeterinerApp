package com.app.veteriner.controller;


import com.app.veteriner.dto.LoginDto;
import com.app.veteriner.dto.SignupDto;
import com.app.veteriner.model.User;
import com.app.veteriner.model.UserValidator;
import com.app.veteriner.repository.RoleRepository;
import com.app.veteriner.service.OwnerService;
import com.app.veteriner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class LoginController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserValidator userValidator;

	@Autowired
	private OwnerService ownerService;

	@Autowired
	RoleRepository roleRepository;


	@GetMapping("/registration")
	public String registration(Model model) {

		model.addAttribute("userForm", new SignupDto());

		return "registration";
	}

	@PostMapping("/registration")
	public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) throws Exception {
		userValidator.validate(userForm, bindingResult);

		if (bindingResult.hasErrors()) {
			return "registration";
		}

		userService.save(userForm);


		return "redirect:/home";
	}

	@GetMapping("/login")
	public String login(Model model, String error, String logout) {

		model.addAttribute("loginDto", new LoginDto());

		if (error != null)
			model.addAttribute("error", "Hatalı kullanıcı adı veya şifre!");

		if (logout != null)
			model.addAttribute("message", "Başarı ile çıkış yapıldı.");

		return "login";
	}

	@GetMapping({"/", "/home"})
	public String home(Model model) {
		model.addAttribute("owners", ownerService.getAllOwners());
		return "home";
	}

}
