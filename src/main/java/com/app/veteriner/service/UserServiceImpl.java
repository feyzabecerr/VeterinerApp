package com.app.veteriner.service;

import com.app.veteriner.model.ERole;
import com.app.veteriner.model.Role;
import com.app.veteriner.model.User;
import com.app.veteriner.repository.RoleRepository;
import com.app.veteriner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public void save(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		Role adminRole = new Role(null,ERole.ROLE_ADMIN);

		if(user.getUsername().equals("admin")){
			user.setRoles(new HashSet<>(List.of(adminRole)));
		}

		Role userRole = new Role(null,ERole.ROLE_USER);
		user.setRoles(new HashSet<>(List.of(userRole)));

		userRepository.save(user);
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public Optional<User> getOneUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

}
