package de.sopro.model.send;

import de.sopro.model.CompositionEdge;
import de.sopro.model.CompositionNode;

public class Edge {

	private long id;
	private CompositionNode source;
	private CompositionNode target;
	private CompatibilityAnswer compatibility;

	public Edge(long id, CompositionNode source, CompositionNode target, CompatibilityAnswer compatibility) {
		this.id = id;
		this.source = source;
		this.target = target;
		this.compatibility = compatibility;
	}

	public CompositionEdge createCompositionEdge(){
		return new CompositionEdge(source, target);
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

	public void setId(long id){
		this.id = id;
	}
	public long getID(){
		return id;
	}

}
