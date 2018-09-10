package de.sopro.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class CompositionEdge {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@OneToOne
	private CompositionNode source;

	@NotNull
	@OneToOne
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

	// TODO
	// public Edge createEdge() {
	//
	// }

	public String toString() {
		return source + " -> " + target;
	}
}
