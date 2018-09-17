package de.sopro.model.send;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleComp {

	private SimpleUser owner;
	private long id;
	private String name;
	private boolean editable;

	@JsonCreator
	public SimpleComp(@JsonProperty("id") long id, @JsonProperty("owner") SimpleUser owner,
			@JsonProperty("name") String name, @JsonProperty("editable")boolean editable) {
		this.id = id;
		this.owner = owner;
		this.name = name;
		this.editable = editable;
	}

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

	public String toString() {
		return name;
	}
}
