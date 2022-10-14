package com.app.veteriner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@Table(name = "owners")
@NoArgsConstructor

public class Owner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String firstName;

	private String lastName;

	private String phoneNumber;

	private String email;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
	private List<Pet> petList;

}
