package de.sopro.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Format {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String type;

	@NotBlank
	private String version;

	@NotBlank
	private String compatibilityDegree;

	// Hibernate requires a no-arg constructor
	public Format() {

	}

	public Format(String name, String version, String compatibilityDegree) {
		this.type = name;
		this.version = version;
		this.compatibilityDegree = compatibilityDegree;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return type;
	}

	public void setName(String name) {
		this.type = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String isFlexible() {
		return compatibilityDegree;
	}

	public void setFlexible(String compatibilityDegree) {
		this.compatibilityDegree = compatibilityDegree;
	}

	public String toString() {
		return type + ", Version: " + version;
	}
}
