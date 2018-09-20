package de.sopro.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import de.sopro.model.send.Node;

/**
 * A CompositionNode represents a Service in a Composition
 * 
 * @author HRS3-R.105B
 *
 */
@Entity
public class CompositionNode {

	/* required variables */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private int x;

	private int y;

	@ManyToOne
	private Service service;

	// Hibernate requires a no-arg constructor
	public CompositionNode() {

	}

	/**
	 * creates a CompositionNode with the given values
	 * 
	 * @param x
	 *            x coordinate of the CompositionNode
	 * @param y
	 *            y coordinate of the CompositionNode
	 * @param service
	 *            service that the CompositionNode should represent
	 */
	public CompositionNode(int x, int y, Service service) {
		this.x = x;
		this.y = y;
		this.service = service;
	}

	/* getter and setters */
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
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

	/**
	 * converts the CompositionNode to a Node
	 * 
	 * @return a Node that represents the CompositionNode
	 */
	public Node createNode() {
		return new Node(this.id, this.x, this.y, this.service.createSendService());
	}

	/**
	 * converts the CompositionNode to a String
	 */
	public String toString() {
		if (service != null) {
			return service + " (" + x + "," + y + ")";
		} else {
			return "dummy (" + x + "," + y + ")";
		}
	}
}
