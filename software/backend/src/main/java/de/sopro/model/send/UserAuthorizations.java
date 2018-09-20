package de.sopro.model.send;

import java.util.List;

/**
 * An UserAuthorizations instance contains information about the permissions for
 * a composition.
 * 
 * @author HRS3-R.105B
 *
 */
public class UserAuthorizations {

	/* required variables */
	private List<SimpleUser> editors;
	private List<SimpleUser> viewers;

	/**
	 * creates an UserAuthorizations with the given values
	 * 
	 * @param editors
	 *            User that have the permission to edit
	 * @param viewers
	 *            User that have the permission to view
	 */
	public UserAuthorizations(List<SimpleUser> editors, List<SimpleUser> viewers) {
		this.editors = editors;
		this.viewers = viewers;
	}

	/* getetr and setter */
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

	/**
	 * converts the UserAuthorizations to a String
	 */
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
