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
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String email;

	private boolean isAdmin;

	@NotBlank
	private String firstname;

	@NotBlank
	@JsonIgnore // test
	private String lastname;

	@NotBlank
	private String title;

	// CascadeType.ALL => if you delete an User then all compositions associated
	// with that User also be deleted
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
	private List<Composition> ownsComp;

	@ManyToMany(cascade = CascadeType.MERGE)
	private List<Composition> viewableComps;

	@ManyToMany(cascade = CascadeType.MERGE)
	private List<Composition> editableComps;

	public User(String firstname, String lastname) {
		this.firstname = firstname;
		this.lastname = lastname;
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
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
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

	public String getFullName() {
		return firstname + " " + lastname;
	}

	public String toString() {
		return getFullName();
	}
}
