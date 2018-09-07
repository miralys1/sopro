package de.sopro.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CompositionEdge {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

//	@NotNull
//	private CompositionNode source;
//
//	@NotNull
//	private CompositionNode target;

	public CompositionEdge() {
		
	}
	
//	public CompositionEdge(CompositionNode source, CompositionNode target) {
//		this.source = source;
//		this.target = target;
//	}

//	public CompositionNode getSource() {
//		return source;
//	}
//
//	public void setSource(CompositionNode source) {
//		this.source = source;
//	}
//
//	public CompositionNode getTarget() {
//		return target;
//	}
//
//	public void setTarget(CompositionNode target) {
//		this.target = target;
//	}

	// TODO
	// public Edge createEdge() {
	//
	// }

//	public String toString() {
//		return source + " -> " + target;
//	}
}
