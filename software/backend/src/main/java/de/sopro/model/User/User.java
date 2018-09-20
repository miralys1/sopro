package de.sopro.model.User;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import de.sopro.model.Composition;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import de.sopro.model.send.DetailUser;
import de.sopro.model.send.SimpleUser;

/**
 * A User represents a User of our application
 * 
 * @author HRS3-R.105B
 *
 */
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String email;

	private boolean admin;

	@NotBlank
	private String firstname;

	@NotBlank
	private String lastname;

	@NotNull
	private String title;

	@NotBlank
	private String password;

	private String[] roles;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
	private List<Composition> ownsComp;

	@ManyToMany(cascade = CascadeType.MERGE)
	private List<Composition> viewableComps;

	@ManyToMany(cascade = CascadeType.MERGE)
	private List<Composition> editableComps;

	// Hibernate requires a no-arg constructor
	public User() {

	}

	/**
	 * creates a User with the given values
	 * 
	 * @param firstname
	 *            first name of the User
	 * @param lastname
	 *            last name of the User
	 * @param email
	 *            email of the user
	 * @param title
	 *            title of the user
	 * @param isAdmin
	 *            determines whether the user is an admin
	 */
	public User(String firstname, String lastname, String email, String title, boolean isAdmin) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.title = title;
		this.admin = isAdmin;
		this.ownsComp = new ArrayList<Composition>();
		this.viewableComps = new ArrayList<Composition>();
		this.editableComps = new ArrayList<Composition>();
	}

	/* getter and setter */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getFirstName() {
		return firstname;
	}

	public void setFirstName(String firstName) {
		this.firstname = firstName;
	}

	public String getLastName() {
		return lastname;
	}

	public void setLastName(String lastName) {
		this.lastname = lastName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Composition> getOwnsComp() {
		return ownsComp;
	}

	public void setOwnsComp(List<Composition> owns) {
		this.ownsComp = owns;
	}

	public List<Composition> getViewable() {
		return viewableComps;
	}

	public void setViewable(List<Composition> viewable) {
		this.viewableComps = viewable;
	}

	public List<Composition> getEditable() {
		return editableComps;
	}

	public void setEditable(List<Composition> editable) {
		this.editableComps = editable;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password){
		this.password = password;
	}

	public String[] getRoles(){
		return roles;
	}

	public void setRoles(String[] roles){
		this.roles = roles;
	}

	public String getFullName() {
		return title + " " + firstname + " " + lastname;
	}

	/**
	 * converts the User to a SimpleUser
	 * 
	 * @return a SimpleUser that represents the User
	 */
	public SimpleUser createSimpleUser() {
		return new SimpleUser(this.id, this.firstname, this.lastname, this.title);
	}

	/**
	 * converts the User to a DetailUser
	 * 
	 * @return a DetailUser that represents the User
	 */
	public DetailUser createDetailUser() {
		return new DetailUser(this.email, this.title, this.admin, this.firstname, this.lastname, this.id);
	}

	/**
	 * converts the User to a String
	 */
	public String toString() {
		return getFullName();
	}
}
