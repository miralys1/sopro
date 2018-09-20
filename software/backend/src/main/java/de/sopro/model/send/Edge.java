package de.sopro.model.send;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.sopro.model.CompositionEdge;

/**
 * An Edge represents the connection between two nodes and contains information
 * about the compatibility.
 * 
 * @author HRS3-R.105B
 *
 */
public class Edge {

	/* required variables */
	private long id;
	private Node source;
	private Node target;
	private CompatibilityAnswer compatibility;

	/**
	 * creates an edge with the given values
	 * 
	 * @param id
	 *            id of the Compositionedge the Edge represents
	 * @param source
	 *            source of the Edge
	 * @param target
	 *            target of the Edge
	 */
	@JsonCreator
	public Edge(@JsonProperty("id") long id, @JsonProperty("source") Node source, @JsonProperty("target") Node target) {
		this.id = id;
		this.source = source;
		this.target = target;
	}

	/**
	 * creates an edge with the given values
	 * 
	 * @param id
	 *            id of the CompositionEdge the Edge represents
	 * @param source
	 *            source of the Edge
	 * @param target
	 *            target of the Edge
	 * @param compatibility
	 *            conatins information about the compatibility of source and target
	 */
	public Edge(long id, Node source, Node target, CompatibilityAnswer compatibility) {
		this.id = id;
		this.source = source;
		this.target = target;
		this.compatibility = compatibility;
	}

	/* getter and setter */
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

	public void setId(long id) {
		this.id = id;
	}

	public long getID() {
		return id;
	}

	/**
	 * converts the Edge to a CompositionEdge
	 * 
	 * @return a CompositionEdge that represents the Edge
	 */
	public CompositionEdge createCompositionEdge() {
		CompositionEdge edge = new CompositionEdge(source.createCompositionNode(), target.createCompositionNode());
		edge.setId(id);
		return edge;
	}

	/**
	 * converts the Edge to a String
	 */
	public String toString() {
		return source + " -> " + target;
	}

}
