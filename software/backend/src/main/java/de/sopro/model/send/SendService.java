package de.sopro.model.send;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.sopro.model.Format;
import de.sopro.model.Service;
import de.sopro.model.Tag;

/**
 * A SendService represents a Service
 * 
 * @author HRS3-R.105B
 *
 */
public class SendService {

	/* required variables */
	private Long id;
	@NotBlank
	private String name;
	private String version;
	private List<String> tags;
	private String organisation;
	private long date;
	private String logo;
	private String certified;
	private List<Format> formatIn;
	private List<Format> formatOut;

	/**
	 * creates a SendService with the given values
	 * 
	 * @param id
	 *            id of the service that should be represented
	 * @param name
	 *            name of the service
	 * @param version
	 *            version of the service
	 * @param tags
	 *            tags of the service
	 * @param organisation
	 *            organization of the service
	 * @param date
	 *            date of the service
	 * @param logo
	 *            logo that should be used by the service
	 * @param certified
	 *            determines whether the service is certified
	 * @param formatIn
	 *            input formats of the service
	 * @param formatOut
	 *            output formats of the service
	 */
	@JsonCreator
	public SendService(@JsonProperty("id") Long id, @JsonProperty("name") String name,
			@JsonProperty("version") String version, @JsonProperty("tags") List<String> tags,
			@JsonProperty("organisation") String organisation, @JsonProperty("date") long date,
			@JsonProperty("logo") String logo, @JsonProperty("certified") String certified,
			@JsonProperty("formatIn") List<Format> formatIn, @JsonProperty("formatOut") List<Format> formatOut) {
		this.id = id;
		this.name = name;
		this.version = version;
		this.tags = tags;
		this.organisation = organisation;
		this.date = date;
		this.logo = logo;
		this.certified = certified;
		this.formatIn = formatIn;
		this.formatOut = formatOut;
	}

	/* getter and setter */
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

	public String getCertified() {
		return certified;
	}

	public void setCertified(String certified) {
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

	/**
	 * converts the SendService to a Service
	 * 
	 * @return a Service that represents the SendService
	 */
	public Service createService() {
		// convert tags to list of tags
		List<Tag> tags = new ArrayList<>();
		for (String tag : this.tags) {
			tags.add(new Tag(tag));
		}

		// convert certified to boolean
		boolean c = false;
		if (certified.equals("true")) {
			c = true;
		}
		Service s = new Service(this.name, this.version, tags, this.organisation, this.date, this.logo, c,
				this.formatIn, this.formatOut);
		s.setId(id);
		return s;
	}

	/**
	 * converts the SendService to a String
	 */
	public String toString() {
		return name;
	}
}
