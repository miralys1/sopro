package de.sopro.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import de.sopro.model.send.DetailComp;
import de.sopro.model.send.Edge;
import de.sopro.model.send.Node;
import de.sopro.model.send.SimpleComp;
import de.sopro.model.send.SimpleUser;
import de.sopro.model.send.UserAuthorizations;

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

	@NotNull
	@OneToMany(cascade = CascadeType.ALL)
	private List<CompositionNode> nodes;

	@NotNull
	@OneToMany(cascade = CascadeType.ALL)
	private List<CompositionEdge> edges;

	// Hibernate requires a no-arg constructor
	public Composition() {

	}

	public Composition(User owner, String name, boolean isPublic, List<CompositionNode> nodes,
			List<CompositionEdge> edges) {
		this.owner = owner;
		this.name = name;
		this.isPublic = isPublic;
		this.viewers = new ArrayList<User>();
		this.editors = new ArrayList<User>();
		this.nodes = nodes;
		this.edges = edges;
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

	public List<CompositionNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<CompositionNode> nodes) {
		this.nodes = nodes;
	}

	public List<CompositionEdge> getEdges() {
		return edges;
	}

	public void setEdges(List<CompositionEdge> edges) {
		this.edges = edges;
	}

	// TODO: userID �bergeben?
	public SimpleComp createSimpleComp(long userID) {
		boolean editable = false;
		for (User user : editors) {
			if (user.getId() == userID) {
				editable = true;
			}
		}
		return new SimpleComp(this.id, this.owner.createSimpleUser(), this.name, editable);
	}

	// TODO: userID �bergeben?
	public DetailComp createDetailComp(long userID) {
		boolean editable = false;
		if(userID == getOwner().getId()){
			editable = true;
		}else {
			for (User user : editors) {
				if (user.getId() == userID) {
					editable = true;
				}
			}
		}
		List<Node> nodes = new ArrayList<>();
		for (CompositionNode node : this.nodes) {
			nodes.add(node.createNode());
		}

		List<Edge> edges = new ArrayList<>();
		for (CompositionEdge edge : this.edges) {
			edges.add(edge.createEdge());
		}
		
		return new DetailComp(this.id, this.owner.createSimpleUser(), this.name, editable, nodes, edges);
	}

	public UserAuthorizations createUserAuths() {
		List<SimpleUser> editors = new ArrayList<>();
		for (User user : this.editors) {
			editors.add(user.createSimpleUser());
		}
		List<SimpleUser> viewers = new ArrayList<>();
		for (User user : this.viewers) {
			viewers.add(user.createSimpleUser());
		}
		return new UserAuthorizations(editors, viewers);
	}

	public String toString() {
		return owner.getFullName() + ": " + name;
	}
}
