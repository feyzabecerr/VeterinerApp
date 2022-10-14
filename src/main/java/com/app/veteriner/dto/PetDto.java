package com.app.veteriner.dto;

import lombok.Data;



@Data
public class PetDto {

	private Long ownerId;

	private String species;

	private String gender;

	private String name;

	private String description;

	private Long age;
}
