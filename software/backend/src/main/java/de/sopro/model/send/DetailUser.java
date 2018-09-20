package de.sopro.model.send;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.sopro.model.User.User;

/**
 * A DetailUser represents a User and contains detail information
 * 
 * @author HRS3-R.105B
 *
 */
public class DetailUser extends SimpleUser {

	/* required variables */
	@NotBlank
	private String email;
	private boolean admin;

	/**
	 * creates a DetailUser with the given values
	 * 
	 * @param email
	 *            email of the DetailUser
	 * @param title
	 *            title of the DetailUser
	 * @param admin
	 *            determines whether the DetailUser is an admin
	 * @param firstName
	 *            first name of the DetailUser
	 * @param lastName
	 *            last name of the DetailUser
	 * @param id
	 *            id of the User the DetailUser represents
	 */
	@JsonCreator
	public DetailUser(@JsonProperty("email") String email, @JsonProperty("title") String title,
			@JsonProperty("admin") boolean admin, @JsonProperty("firstName") String firstName,
			@JsonProperty("lastName") String lastName, @JsonProperty("id") long id) {
		super(id, firstName, lastName, title);
		this.email = email;
		this.admin = admin;
	}

	/* getter and setter */
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean isAdmin) {
		this.admin = isAdmin;
	}

	/**
	 * creates a User that represents the DetailUser
	 * 
	 * @param pw
	 *            password the User should have
	 * @return a User that represents the DetailUser
	 */
	public User createUser(String pw) {
		User user = new User(super.getFirstName(), super.getLastName(), this.email, super.getTitle(), this.admin);
		user.setId(super.getId());
		user.setPassword(pw);
		return user;
	}

	/**
	 * converts the DetailUser to a String
	 */
	public String toString() {
		return super.toString();
	}
}
