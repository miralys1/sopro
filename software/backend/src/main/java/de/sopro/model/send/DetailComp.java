package de.sopro.model.send;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.sopro.model.Composition;
import de.sopro.model.CompositionEdge;
import de.sopro.model.CompositionNode;
import de.sopro.model.User.User;

public class DetailComp extends SimpleComp {

	private List<Node> nodes;
	private List<Edge> edges;

	@JsonCreator
	public DetailComp(@JsonProperty("id") long id, @JsonProperty("owner") SimpleUser owner,
			@JsonProperty("name") String name, @JsonProperty("editable") boolean editable,
			@JsonProperty("nodes") List<Node> nodes, @JsonProperty("edges")List<Edge> edges) {
		super(id, owner, name, editable);
		this.nodes = nodes;
		this.edges = edges;
	}

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

	public String toString() {
		return super.toString();
	}

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
