package de.sopro.model.send;

import de.sopro.model.CompositionNode;

public class Node {

	private long id;
	private int x;
	private int y;
	private SendService sendService;

	public Node(long id, int x, int y, SendService service) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.sendService = service;
	}

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

	public CompositionNode createCompositionNode() {
		return new CompositionNode(this.x, this.y, sendService.createService());
	}

	public String toString() {
		if (sendService != null) {
			return sendService + " (" + x + "," + y + ")";
		} else {
			return "dummy (" + x + "," + y + ")";
		}
	}
}
