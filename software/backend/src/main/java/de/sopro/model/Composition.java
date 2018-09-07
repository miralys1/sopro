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
public class Composition {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@ManyToOne
	private User owner;

	@NotBlank
	private String name;

	private boolean isPublic;

	@NotNull
	@ManyToMany(cascade = CascadeType.MERGE, mappedBy = "viewableComps")
	private List<User> viewers;

	@NotNull
	@ManyToMany(cascade = CascadeType.MERGE, mappedBy = "editableComps")
	private List<User> editors;

	// @NotNull
	// private List<CompositionNode> nodes;
	//
	// @NotNull
	// private List<CompositionEdge> edges;

	public Composition() {

	}

	public Composition(User owner, String name,
			boolean isPublic/*
							 * , List<CompositionNode> nodes, List<CompositionEdge> edges
							 */) {
		this.owner = owner;
		this.name = name;
		this.isPublic = isPublic;
		this.viewers = new ArrayList<User>();
		this.editors = new ArrayList<User>();
		// this.nodes = nodes;
		// this.edges = edges;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public List<User> getViewers() {
		return viewers;
	}

	public void setViewers(List<User> viewers) {
		this.viewers = viewers;
	}

	public List<User> getEditors() {
		return editors;
	}

	public void setEditors(List<User> editors) {
		this.editors = editors;
	}

	// public List<CompositionNode> getNodes() {
	// return nodes;
	// }
	//
	// public void setNodes(List<CompositionNode> nodes) {
	// this.nodes = nodes;
	// }
	//
	// public List<CompositionEdge> getEdges() {
	// return edges;
	// }
	//
	// public void setEdges(List<CompositionEdge> edges) {
	// this.edges = edges;
	// }

	// TODO
	// public DetailComp createDetailComp() {
	//
	// }
	//
	// public SimpleComp createSimpleComp() {
	//
	// }
	//
	// public UserAuthorizations createUserAuths() {
	//
	// }

	public String toString() {
		return /* owner.getFullName() + */": " + name;
	}
}
