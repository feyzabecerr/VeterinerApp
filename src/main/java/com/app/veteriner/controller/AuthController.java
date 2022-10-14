package com.app.veteriner.controller;

import com.app.veteriner.dto.LoginDto;
import com.app.veteriner.dto.LoginResponse;
import com.app.veteriner.dto.SignupDto;
import com.app.veteriner.model.ERole;
import com.app.veteriner.model.Role;
import com.app.veteriner.model.User;
import com.app.veteriner.repository.RoleRepository;
import com.app.veteriner.security.UserDetailsImpl;
import com.app.veteriner.security.jwt.JwtUtils;
import com.app.veteriner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	RoleRepository roleRepository;
	@Autowired
	PasswordEncoder encoder;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginDto loginDto) {
		Set<GrantedAuthority> authorities = Set.of(JwtUtils.convertAuthority(ERole.ROLE_USER.name()));

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword(),authorities);

		System.out.println(token);

		Authentication authentication = authenticationManager.authenticate(token);

		System.out.println(authentication);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwtToken = jwtUtils.generateJwtToken(authentication);


		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority).toList();

		return ResponseEntity.ok()
				.body(new LoginResponse(userDetails.getId(),
						userDetails.getUsername(),
						userDetails.getEmail(),
						roles, jwtToken));

	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody SignupDto signupDto) {
		if (userService.getOneUserByUsername(signupDto.getUsername()).isPresent()) {
			return new ResponseEntity<>("Username already exists.", HttpStatus.BAD_REQUEST);
		}

		User user = new User();
		user.setUsername(signupDto.getUsername());
		user.setPassword(signupDto.getPassword());
		user.setEmail(signupDto.getEmail());

		userService.save(user);

		return new ResponseEntity<>("User successfully registered.", HttpStatus.CREATED);
	}


}
