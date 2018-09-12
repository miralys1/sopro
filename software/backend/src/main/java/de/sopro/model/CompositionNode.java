package de.sopro.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import de.sopro.model.send.Node;

@Entity
public class CompositionNode {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private int x;

	private int y;

	// @NotNull
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

	public Node createNode() {
		return new Node(this.id, this.x, this.y, this.service.createSendService());
	}

	public String toString() {
		if (service != null) {
			return service + " (" + x + "," + y + ")";
		} else {
			return "dummy (" + x + "," + y + ")";
		}
	}
}
