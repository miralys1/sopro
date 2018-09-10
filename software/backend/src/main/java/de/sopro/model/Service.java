package de.sopro.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Service {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String name;

	private String version;

	@NotNull
	@ManyToMany
	private List<Tag> tags;

	private String organisation;

	private long date;

	private String logo;

	@NotNull
	@ManyToMany
	private List<Format> formatIn;

	@NotNull
	@ManyToMany
	private List<Format> formatOut;

	// Hibernate requires a no-arg constructor
	public Service() {
	}

	public Service(String name, String version, List<Tag> tags, String organisation, long date, String logo,
			List<Format> formatIn, List<Format> formatOut) {
		this.name = name;
		this.version = version;
		this.tags = tags;
		this.organisation = organisation;
		this.date = date;
		this.logo = logo;
		this.formatIn = formatIn;
		this.formatOut = formatOut;
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

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

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

	 public String getLogo() {
	 return logo;
	 }
	
	 public void setLogo(String logo) {
	 this.logo = logo;
	 }

	public List<Format> getFormatIn() {
		return formatIn;
	}

	public void setIn(List<Format> formatIn) {
		this.formatIn = formatIn;
	}

	public List<Format> getFormatOut() {
		return formatOut;
	}

	public void setOut(List<Format> formatOut) {
		this.formatOut = formatOut;
	}

	public String toString() {
		return name;
	}
}
