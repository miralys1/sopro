package de.sopro.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Format {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String name;

	@NotBlank
	private String version;

	private boolean isFlexible;

	// Hibernate requires a no-arg constructor
	public Format() {

	}

	public Format(String name, String version, boolean isFlexible) {
		this.name = name;
		this.version = version;
		this.isFlexible = isFlexible;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isFlexible() {
		return isFlexible;
	}

	public void setFlexible(boolean isFlexible) {
		this.isFlexible = isFlexible;
	}

	public String toString() {
		return name + ", Version: " + version;
	}
}
