package de.sopro.model.send;

import java.util.ArrayList;
import java.util.List;

import de.sopro.model.Composition;
import de.sopro.model.CompositionEdge;
import de.sopro.model.CompositionNode;
import de.sopro.model.User;

public class DetailComp extends SimpleComp {

	private List<Node> nodes;
	private List<Edge> edges;

	public DetailComp(long id, SimpleUser owner, String name, boolean isEditable, List<Node> nodes, List<Edge> edges) {
		super(id, owner, name, isEditable);
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

		return new Composition(owner, getName(), false, nodes, edges);
	}

}
