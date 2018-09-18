package de.sopro.model.send;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.sopro.model.Composition;
import de.sopro.model.CompositionEdge;

public class Edge {

	private long id;
	private Node source;
	private Node target;
	private CompatibilityAnswer compatibility;

	@JsonCreator
	public Edge(@JsonProperty("id") long id, @JsonProperty("source") Node source, @JsonProperty("target") Node target) {
		this.id = id;
		this.source = source;
		this.target = target;
	}

	@JsonCreator
	public Edge(@JsonProperty("id") long id, @JsonProperty("source") Node source, @JsonProperty("target") Node target,
			@JsonProperty("compatibility") CompatibilityAnswer compatibility) {
		this.id = id;
		this.source = source;
		this.target = target;
		this.compatibility = compatibility;
	}

	public CompositionEdge createCompositionEdge() {
		CompositionEdge edge = new CompositionEdge(source.createCompositionNode(), target.createCompositionNode());
		edge.setId(id);
		return edge;
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
