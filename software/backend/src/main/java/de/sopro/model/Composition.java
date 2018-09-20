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

import de.sopro.model.User.User;
import de.sopro.model.send.DetailComp;
import de.sopro.model.send.Edge;
import de.sopro.model.send.Node;
import de.sopro.model.send.SimpleComp;
import de.sopro.model.send.SimpleUser;
import de.sopro.model.send.UserAuthorizations;

/**
 * A Composition represents a combination of several services and is saved as a
 * graph
 * 
 * @author HRS3-R.105B
 *
 */
@Entity
public class Composition {

	/* required attributes */
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
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CompositionNode> nodes;

	@NotNull
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CompositionEdge> edges;

	// Hibernate requires a no-arg constructor
	public Composition() {

	}

	/**
	 * creates a Composition with the given values
	 * 
	 * @param owner
	 *            User that created/owns the Composition
	 * @param name
	 *            name of the Composition
	 * @param isPublic
	 *            determines whether the Composition should be public
	 * @param nodes
	 *            nodes of the Composition
	 * @param edges
	 *            edges of the Composition
	 */
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

	/* getter and setter */
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

	/**
	 * converts the Composition in a SimpleComp
	 * 
	 * @param userID
	 *            id of the user that should get the SimpleComp
	 * @return a SimpleComp that represents the Composition
	 */
	public SimpleComp createSimpleComp(long userID) {
		// editable determines whether the user is allowed to edit the composition
		boolean editable = false;
		for (User user : editors) {
			if (user.getId() == userID) {
				editable = true;
			}
		}
		return new SimpleComp(this.id, this.owner.createSimpleUser(), this.name, editable);
	}

	/**
	 * converts the Composition in a DetailComp
	 * 
	 * @param userID
	 *            id of the user that should get the DetailComp
	 * @return a DetailComp that represents the Composition
	 */
	public DetailComp createDetailComp(long userID) {
		// user is allowed to edit the COmposition if he is the owner or have edit
		// permissions
		boolean editable = false;
		boolean owner = false;
		if (userID == getOwner().getId()) {
			editable = true;
			owner = true;
		} else {

			for (User user : editors) {
				if (user.getId() == userID) {
					editable = true;
				}
			}
		}

		// create nodes and edges for the DetialComp
		List<Node> nodes = new ArrayList<>();
		for (CompositionNode node : this.nodes) {
			nodes.add(node.createNode());
		}
		List<Edge> edges = new ArrayList<>();
		for (CompositionEdge edge : this.edges) {
			edges.add(edge.createEdge());
		}

		DetailComp dComp = new DetailComp(this.id, this.owner.createSimpleUser(), this.name, editable, nodes, edges);
		dComp.setIsOwner(owner);
		return dComp;
	}

	/**
	 * creates an UserAuthorizations object that contains the users with edit
	 * permission and the users with view permission for the Composition
	 * 
	 * @return UserAuthorizations for the Composition
	 */
	public UserAuthorizations createUserAuths() {
		// collect editors
		List<SimpleUser> editors = new ArrayList<>();
		for (User user : this.editors) {
			editors.add(user.createSimpleUser());
		}
		// collect viewers
		List<SimpleUser> viewers = new ArrayList<>();
		for (User user : this.viewers) {
			viewers.add(user.createSimpleUser());
		}

		return new UserAuthorizations(editors, viewers);
	}

	/**
	 * convert the Composition to a String
	 */
	public String toString() {
		return owner.getFullName() + ": " + name;
	}
}
