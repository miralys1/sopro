package de.sopro.model.send;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.sopro.model.Format;
import de.sopro.model.Service;
import de.sopro.model.Tag;

public class SendService {

	private Long id;

	private String name;

	private String version;

	private List<String> tags;

	private String organisation;

	private long date;

	private String logo;

	private boolean certified;

	private List<Format> formatIn;

	private List<Format> formatOut;

	@JsonCreator
	public SendService(@JsonProperty("id") Long id, @JsonProperty("name") String name,
			@JsonProperty("version") String version, @JsonProperty("tags") List<String> tags,
			@JsonProperty("organisation") String organisation, @JsonProperty("date") long date,
			@JsonProperty("logo") String logo, @JsonProperty("certified") boolean certified,
			@JsonProperty("formatIn") List<Format> formatIn, @JsonProperty("formatOut") List<Format> formatOut) {
		this.id = id;
		this.name = name;
		this.version = version;
		this.tags = tags;
		this.organisation = organisation;
		this.date = date;
		this.logo = logo;
		this.setCertified(certified);
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

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
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

	public void setLogo(String picture) {
		this.logo = picture;
	}

	public boolean isCertified() {
		return certified;
	}

	public void setCertified(boolean certified) {
		this.certified = certified;
	}

	public List<Format> getFormatIn() {
		return formatIn;
	}

	public void setFormatIn(List<Format> in) {
		this.formatIn = in;
	}

	public List<Format> getFormatOut() {
		return formatOut;
	}

	public void setFormatOut(List<Format> out) {
		this.formatOut = out;
	}

	public Service createService() {
		List<Tag> tags = new ArrayList<>();
		for (String tag : this.tags) {
			tags.add(new Tag(tag));
		}

		Service s = new Service(this.name, this.version, tags, this.organisation, this.date, this.logo, this.certified,
				this.formatIn, this.formatOut);
		s.setId(id);
		return s;
	}

	public String toString() {
		return name;
	}
}
