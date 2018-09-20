package de.sopro.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

/**
 * A Tag is used for services
 * 
 * @author HRS3-R.105B
 *
 */
@Entity
public class Tag {

	/* required variables */
	@Id
	@NotBlank
	private String name;

	// Hibernate requires a no-arg constructor
	public Tag() {
	}

	/**
	 * creates a tag with the given value
	 * 
	 * @param name
	 *            name of the tag
	 */
	public Tag(String name) {
		this.name = name;
	}

	/* getter and setter */
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * converts the Tag to a String
	 */
	public String toString() {
		return name;
	}
}