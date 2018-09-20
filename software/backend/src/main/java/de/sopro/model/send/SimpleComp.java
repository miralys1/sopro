package de.sopro.model.send;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A SimpleComp represents a Composition with less information
 * 
 * @author HRS3-R.105B
 *
 */
public class SimpleComp {

	/* required variables */
	@NotNull
	private SimpleUser owner;
	private long id;
	@NotBlank
	private String name;
	private boolean editable;

	/**
	 * creates a SimpleComp with the given values.
	 * 
	 * @param id
	 *            id of the Composition that should be represented
	 * @param owner
	 *            owner of the Composition
	 * @param name
	 *            name of the Composition
	 * @param editable
	 *            determines whether the Composition is editable by a user
	 */
	@JsonCreator
	public SimpleComp(@JsonProperty("id") long id, @JsonProperty("owner") SimpleUser owner,
			@JsonProperty("name") String name, @JsonProperty("editable") boolean editable) {
		this.id = id;
		this.owner = owner;
		this.name = name;
		this.editable = editable;
	}

	/* getter and setter */
	public SimpleUser getOwner() {
		return owner;
	}

	public void setOwner(SimpleUser owner) {
		this.owner = owner;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * converts the SimpleComp to a String
	 */
	public String toString() {
		return name;
	}
}
