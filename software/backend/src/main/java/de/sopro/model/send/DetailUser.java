package de.sopro.model.send;

import de.sopro.model.User;

public class DetailUser extends SimpleUser {

	private String email;
	private String title;
	private boolean admin;

	public DetailUser(String email, String title, boolean admin, String firstName, String lastName, long id) {
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
