package de.sopro.model.send;

import java.util.List;

/**
 * An Alternative contains information about services that can convert formats
 * to make two services compatible
 * 
 * @author HRS3-R.105B
 *
 */
public class Alternative {

	/* required variables */
	private List<String> names;

	private List<String> versions;

	private List<Long> ids;

	/**
	 * creates an alternative with the given values
	 * 
	 * @param names
	 *            names of the services that serve as alternatives
	 * @param versions
	 *            versions of the services
	 * @param ids
	 *            ids of the services
	 */
	public Alternative(List<String> names, List<String> versions, List<Long> ids) {
		this.names = names;
		this.versions = versions;
		this.ids = ids;
	}

	/* getter and setter */
	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public List<String> getVersions() {
		return versions;
	}

	public void setVersions(List<String> versions) {
		this.versions = versions;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	/**
	 * converts the Alternative to a String
	 */
	public String toString() {
		String s = "";
		for (int i = 0; i < names.size() - 1; i++) {
			s += names.get(i) + ", ";
		}
		s += names.get(names.size() - 1);
		return s;
	}
}
