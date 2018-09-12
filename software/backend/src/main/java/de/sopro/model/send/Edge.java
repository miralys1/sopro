package de.sopro.model.send;

import de.sopro.model.CompositionEdge;

public class Edge {

	private long id;
	private Node source;
	private Node target;
	private CompatibilityAnswer compatibility;

	public Edge(long id, Node source, Node target, CompatibilityAnswer compatibility) {
		this.id = id;
		this.source = source;
		this.target = target;
		this.compatibility = compatibility;
	}

	public CompositionEdge createCompositionEdge() {
		return new CompositionEdge(source.createCompositionNode(), target.createCompositionNode());
	}

	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public Node getTarget() {
		return target;
	}

	public void setTarget(Node target) {
		this.target = target;
	}

	public CompatibilityAnswer getCompatibility() {
		return compatibility;
	}

	public void setCompatibility(CompatibilityAnswer compatibility) {
		this.compatibility = compatibility;
	}

	public String toString() {
		return source + " -> " + target;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getID() {
		return id;
	}

}
