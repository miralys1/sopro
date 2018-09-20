package de.sopro.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import de.sopro.model.send.Edge;

/**
 * A CompositionEdge represents a connection between two CompositionNodes
 * 
 * @author HRS3-R.105B
 *
 */
@Entity
public class CompositionEdge {

	/* required attributes */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@ManyToOne(cascade = CascadeType.MERGE)
	private CompositionNode source;

	@NotNull
	@ManyToOne(cascade = CascadeType.MERGE)
	private CompositionNode target;

	// Hibernate requires a no-arg constructor
	public CompositionEdge() {

	}

	/**
	 * creates a CompositionEdge with {@code source} and {@code target}
	 * 
	 * @param source
	 *            origin CompositionNode of the CompositionEdge
	 * @param target
	 *            target CompositionNode of the CompositionEdge
	 */
	public CompositionEdge(CompositionNode source, CompositionNode target) {
		this.source = source;
		this.target = target;
	}

	/* getter and setters */
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

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	/**
	 * converts a CompositionEdge to an Edge
	 * 
	 * @return an Edge that represents the CompositionEdge
	 */
	public Edge createEdge() {
		Edge e = new Edge(this.id, this.source.createNode(), this.target.createNode());
		return e;
	}

	/**
	 * converts the CompositionEdge to a String
	 */
	public String toString() {
		return source + " -> " + target;
	}
}
