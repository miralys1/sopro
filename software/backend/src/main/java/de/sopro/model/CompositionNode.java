package de.sopro.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CompositionNode {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private int x;

	private int y;

	//@NotNull
	@OneToOne
	private Service service;

	// Hibernate requires a no-arg constructor
	public CompositionNode() {

	}

	public CompositionNode(int x, int y, Service service) {
		this.x = x;
		this.y = y;
		this.service = service;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	// TODO
	// public Node createNode() {
	//
	// }

	public String toString() {
		return service + " (" + x + "," + y + ")";
	}
}
