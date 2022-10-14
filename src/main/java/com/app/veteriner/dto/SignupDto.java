package com.app.veteriner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto {

	@NotBlank
	@Size(min = 3, max = 32)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	private Set<String> role;

	@NotBlank
	//@Size(min = 6, max = 60)
	private String password;

	@NotBlank
//	@Size(min = 6, max = 60)
	private String passwordConfirm;
}
