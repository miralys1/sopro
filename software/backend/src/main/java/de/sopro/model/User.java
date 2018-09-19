package de.sopro.model;

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

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import de.sopro.model.send.DetailUser;
import de.sopro.model.send.SimpleUser;

@Entity
public class User {

	public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

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

	@NotBlank
	private String title;

	@NotBlank
	private String password;

	private String [] roles;

	// CascadeType.ALL => if you delete an User then all compositions associated
	// with that User also be deleted
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
	private List<Composition> ownsComp;

	@ManyToMany(cascade = CascadeType.MERGE)
	private List<Composition> viewableComps;

	@ManyToMany(cascade = CascadeType.MERGE)
	private List<Composition> editableComps;

	// Hibernate requires a no-arg constructor
	public User() {

	}

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

	public String getPassword(){
		return password;
	}
	
	public void setPassword(String password){
		this.password = PASSWORD_ENCODER.encode(password);
	}

	public String[] getRole(){
		return roles;
	}

	public void setRole(String[] roles){
		this.roles = roles;
	}

	public String getFullName() {
		return firstname + " " + lastname;
	}

	public SimpleUser createSimpleUser() {
		return new SimpleUser(this.id, this.firstname, this.lastname);
	}

	public DetailUser createDetailUser() {
		return new DetailUser(this.email, this.title, this.admin, this.firstname, this.lastname, this.id);
	}

	public String toString() {
		return getFullName();
	}
}
