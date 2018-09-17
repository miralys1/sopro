package de.sopro.model.send;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.sopro.model.User;

public class DetailUser extends SimpleUser {

	private String email;
	private String title;
	private boolean admin;

	@JsonCreator
	public DetailUser(@JsonProperty("email") String email, @JsonProperty("title") String title,
			@JsonProperty("admin") boolean admin, @JsonProperty("firstName") String firstName,
			@JsonProperty("lastName") String lastName, @JsonProperty("id") long id) {
		super(id, firstName, lastName);
		this.email = email;
		this.title = title;
		this.admin = admin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean isAdmin) {
		this.admin = isAdmin;
	}

	public User createUser(String pw) {
		User user = new User(super.getFirstName(), super.getLastName(), this.email, this.title, this.admin);
		user.setId(super.getId());
		user.setPassword(pw);
		return user;
	}

	public String toString() {
		return super.toString();
	}
}
