package com.app.veteriner.service;

import com.app.veteriner.model.User;

import java.util.Optional;

public interface UserService {

	void save(User user);

	Optional<User> findByUsername(String username);

	Optional<User> getOneUserByUsername(String username);


	}
