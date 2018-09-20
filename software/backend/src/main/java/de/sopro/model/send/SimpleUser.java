package de.sopro.model.send;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A SimpleUser represents a User with fewer information
 * 
 * @author HRS3-R.105B
 *
 */
public class SimpleUser {

	/* required variables */
	private long id;
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;
	private String title;

	/**
	 * creates a SimpleUser with the given values
	 * 
	 * @param id
	 *            id of the User that should be represented
	 * @param firstName
	 *            first name of the User
	 * @param lastName
	 *            last name of the User
	 * @param title
	 *            title of the User
	 */
	@JsonCreator
	public SimpleUser(@JsonProperty("id") long id, @JsonProperty("firstName") String firstName,
			@JsonProperty("lastName") String lastName, @JsonProperty("title") String title) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.title = title;
	}

	/* getter and setter */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		return title + " " + firstName + " " + lastName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * converts the Simpleuser to a String
	 */
	public String toString() {
		return getFullName();
	}
}
