package com.app.veteriner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
	@NotBlank
	private String username;

	@NotBlank
	private String password;

}
