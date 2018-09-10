package de.sopro.model.send;

import java.util.List;

import de.sopro.model.CompositionNode;

public class DetailComp extends SimpleComp {

	private List<CompositionNode> nodes;
	private List<Edge> edges;

	public DetailComp(long id, SimpleUser owner, String name, boolean isEditable, List<CompositionNode> nodes,
			List<Edge> edges) {
		super(id, owner, name, isEditable);
		this.nodes = nodes;
		this.edges = edges;
	}

	public List<CompositionNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<CompositionNode> nodes) {
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

}
