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
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Service {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String name;

	private String version; // TODO: fragen ob das nicht in Format ist

	// @NotNull
	// private List<String> tags; //TODO: fragen ob tags in die datenbank einzeln
	// aufgenommen werden sollen

	private String organisation;

	private long date;

	// private String picture; // TODO: fragen ob pictures extra gespeichert werden

	@NotNull
	@ManyToMany
	private List<Format> in;

	@NotNull
	@ManyToMany
	private List<Format> out;

	// Hibernate requires a no-arg constructor
	public Service() {
	}

	public Service(String name, String version, List<String> tags, String organisation, long date, String picture,
			List<Format> in, List<Format> out) {
		this.name = name;
		this.version = version;
		// this.tags = tags;
		this.organisation = organisation;
		this.date = date;
		// this.picture=picture;
		this.in = in;
		this.out = out;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	// public List<String> getTags() {
	// return tags;
	// }
	//
	// public void setTags(List<String> tags) {
	// this.tags = tags;
	// }

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	// public String getPicture() {
	// return picture;
	// }
	//
	// public void setPicture(String picture) {
	// this.picture = picture;
	// }

	public List<Format> getIn() {
		return in;
	}

	public void setIn(List<Format> in) {
		this.in = in;
	}

	public List<Format> getOut() {
		return out;
	}

	public void setOut(List<Format> out) {
		this.out = out;
	}

	public String toString() {
		return name;
	}
}
