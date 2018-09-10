package de.sopro.model.send;

import java.util.List;

public class UserAuthorizations {

	private List<SimpleUser> editors;
	private List<SimpleUser> viewers;

	public UserAuthorizations(List<SimpleUser> editors, List<SimpleUser> viewers) {
		this.editors = editors;
		this.viewers = viewers;
	}

	public List<SimpleUser> getEditors() {
		return editors;
	}

	public void setEditors(List<SimpleUser> editors) {
		this.editors = editors;
	}

	public List<SimpleUser> getViewers() {
		return viewers;
	}

	public void setViewers(List<SimpleUser> viewers) {
		this.viewers = viewers;
	}

	public String toString() {
		String s = "editors: ";
		for (int i = 0; i < editors.size() - 1; i++) {
			s += editors.get(i) + ", ";
		}
		s += editors.get(editors.size() - 1);
		s += "\n viewers: ";
		for (int i = 0; i < viewers.size() - 1; i++) {
			s += viewers.get(i) + ", ";
		}
		s += viewers.get(viewers.size() - 1);
		return s;
	}
}
