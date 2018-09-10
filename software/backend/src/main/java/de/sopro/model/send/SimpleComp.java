package de.sopro.model.send;

public class SimpleComp {

	private SimpleUser owner;
	private long id;
	private String name;
	private boolean isEditable;

	public SimpleComp(long id, SimpleUser owner, String name, boolean isEditable) {
		this.id = id;
		this.owner = owner;
		this.name = name;
		this.isEditable = isEditable;
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
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public String toString() {
		return name;
	}
}
