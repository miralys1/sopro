package de.sopro.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import de.sopro.model.send.Edge;

@Entity
public class CompositionEdge {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@OneToOne(cascade = CascadeType.ALL)
	private CompositionNode source;

	@NotNull
	@OneToOne(cascade = CascadeType.ALL)
	private CompositionNode target;

	// Hibernate requires a no-arg constructor
	public CompositionEdge() {

	}

	public CompositionEdge(CompositionNode source, CompositionNode target) {
		this.source = source;
		this.target = target;
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

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public Edge createEdge() {
		Edge e = new Edge(this.id, this.source.createNode(), this.target.createNode());
		return e;
	}

	public String toString() {
		return source + " -> " + target;
	}
}
