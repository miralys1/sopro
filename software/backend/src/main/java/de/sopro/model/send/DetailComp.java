package de.sopro.model.send;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.sopro.model.Composition;
import de.sopro.model.CompositionEdge;
import de.sopro.model.CompositionNode;
import de.sopro.model.User;

/**
 * A DetailComp represents a Composition with detail information
 * 
 * @author HRS3-R.105B
 *
 */
public class DetailComp extends SimpleComp {

	/* required variables */
	private List<Node> nodes;
	private List<Edge> edges;

	/**
	 * creates a DetailComp with the given values
	 * 
	 * @param id
	 *            id of the composition the DetailComp should represent
	 * @param owner
	 *            owner of the Composition
	 * @param name
	 *            name of the Composition
	 * @param editable
	 *            determines whether the DetailComp can be edited by user
	 * @param nodes
	 *            nodes of the DetailComp that represents the CompositionNodes of
	 *            the Composition
	 * @param edges
	 *            edges of the DetailComp that represents the CompositionEdges of
	 *            the Composition
	 */
	@JsonCreator
	public DetailComp(@JsonProperty("id") long id, @JsonProperty("owner") SimpleUser owner,
			@JsonProperty("name") String name, @JsonProperty("editable") boolean editable,
			@JsonProperty("nodes") List<Node> nodes, @JsonProperty("edges") List<Edge> edges) {
		super(id, owner, name, editable);
		this.nodes = nodes;
		this.edges = edges;
	}

	/* getter and setter */
	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

	/**
	 * converts the DetailComp to a String
	 */
	public String toString() {
		return super.toString();
	}

	/**
	 * creates a Composition that represents the DetailComp
	 * 
	 * @param owner
	 *            User that should own the Composition
	 * @return a COmposition representing the DetailComp
	 */
	public Composition createComposition(User owner) {
		List<CompositionEdge> edges = new ArrayList<>();
		List<CompositionNode> nodes = new ArrayList<>();

		for (Node n : this.nodes) {
			nodes.add(n.createCompositionNode());
		}

		for (Edge e : this.edges) {
			edges.add(e.createCompositionEdge());
		}

		Composition comp = new Composition(owner, getName(), false, nodes, edges);
		comp.setId(getId());
		return comp;
	}

}
