package de.sopro.model.send;

import de.sopro.model.CompositionNode;

public class Edge {

	private CompositionNode source;
	private CompositionNode target;
	private CompatibilityAnswer compatibility;

	public Edge(CompositionNode source, CompositionNode target, CompatibilityAnswer compatibility) {
		this.source = source;
		this.target = target;
		this.compatibility = compatibility;
	}

	public CompositionNode getSource() {
		return source;
	}

	public void setSource(CompositionNode source) {
		this.source = source;
	}

	public CompositionNode getTarget() {
		return target;
	}

	public void setTarget(CompositionNode target) {
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
}
