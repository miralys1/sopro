package de.sopro.model.send;

import java.util.ArrayList;
import java.util.List;

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

	private List<Format> formatIn;

	private List<Format> formatOut;

	public SendService(String name, String version, List<String> tags, String organisation, long date, String logo,
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

	public String getPicturePath() {
		return logo;
	}

	public void setPicturePath(String picture) {
		this.logo = picture;
	}

	public List<Format> getIn() {
		return formatIn;
	}

	public void setIn(List<Format> in) {
		this.formatIn = in;
	}

	public List<Format> getOut() {
		return formatOut;
	}

	public void setOut(List<Format> out) {
		this.formatOut = out;
	}

	public Service createService() {
		List<Tag> tags = new ArrayList<>();
		for (String tag : this.tags) {
			tags.add(new Tag(tag));
		}
		return new Service(this.name, this.version, tags, this.organisation, this.date, this.logo, this.formatIn,
				this.formatOut);
	}

	public String toString() {
		return name;
	}
}
