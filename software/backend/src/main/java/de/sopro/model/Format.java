package de.sopro.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * A Format serves as Input and/or Output Format of services
 * 
 * @author HRS3-R.105B
 *
 */
@Entity
public class Format {

	/* required variables */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String type;

	@NotNull
	private String version;

	@NotBlank
	private String compatibilityDegree;

	// Hibernate requires a no-arg constructor
	public Format() {

	}

	/**
	 * creates a Format with the given values
	 * 
	 * @param name
	 *            name of the Format
	 * @param version
	 *            version of the Format
	 * @param compatibilityDegree
	 *            determines whether the Format is strict or flexible compatible;
	 */
	public Format(String name, String version, String compatibilityDegree) {
		this.type = name;
		this.version = version;
		this.compatibilityDegree = compatibilityDegree;
	}

	/* getter and setters */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCompatibilityDegree() {
		return compatibilityDegree;
	}

	public void setCompatibilityDegree(String compatibilityDegree) {
		this.compatibilityDegree = compatibilityDegree;
	}

	/**
	 * converts the Format to a String
	 */
	public String toString() {
		return type + ", Version: " + version;
	}
}
