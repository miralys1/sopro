package de.sopro.model.send;

public class DetailUser extends SimpleUser {

	private String email;
	private String title;
	private boolean isAdmin;

	public DetailUser(String email, String title, boolean isAdmin, String firstName, String lastName, long id) {
		super(id, firstName, lastName);
		this.email = email;
		this.title = title;
		this.isAdmin = isAdmin;
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
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String toString() {
		return super.toString();
	}
}
