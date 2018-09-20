package de.sopro.model.send;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.sopro.model.CompositionNode;

/**
 * A Node represents a SendService in a DetailComposition
 * 
 * @author HRS3-R.105B
 *
 */
public class Node {

	/* required variables */
	private long id;
	private int x;
	private int y;
	private SendService sendService;

	/**
	 * creates a Node with the given values
	 * 
	 * @param id
	 *            id of the CompositionNode the Node represents
	 * @param x
	 *            x coordinate of the node
	 * @param y
	 *            y coordinate of the node
	 * @param service
	 *            service the Node represents
	 */
	@JsonCreator
	public Node(@JsonProperty("id") long id, @JsonProperty("x") int x, @JsonProperty("y") int y,
			@JsonProperty("servcie") SendService service) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.sendService = service;
	}

	/* getter and setter */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public SendService getSendService() {
		return sendService;
	}

	public void setSendService(SendService sendService) {
		this.sendService = sendService;
	}

	/**
	 * converts the Node to a CompositionNode
	 * 
	 * @return a CompositionNode that represents the Node
	 */
	public CompositionNode createCompositionNode() {
		CompositionNode node = new CompositionNode(this.x, this.y, sendService.createService());
		node.setId(getId());
		return node;
	}

	/**
	 * converts the Node to a String
	 */
	public String toString() {
		if (sendService != null) {
			return sendService + " (" + x + "," + y + ")";
		} else {
			return "dummy (" + x + "," + y + ")";
		}
	}
}
