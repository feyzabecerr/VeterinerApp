package com.app.veteriner.repository;

import com.app.veteriner.model.ERole;
import com.app.veteriner.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

	Role findRoleByName(ERole name);


}
