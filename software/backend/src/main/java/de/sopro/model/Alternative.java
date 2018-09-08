package de.sopro.model;

import java.util.List;

public class Alternative {

	private List<String> names;

	private List<String> versions;

	private List<Long> ids;

	public Alternative(List<String> names, List<String> versions, List<Long> ids) {
		this.names = names;
		this.versions = versions;
		this.ids = ids;
	}

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

	public String toString() {
		String s = "";
		for (int i = 0; i < names.size() - 1; i++) {
			s += names.get(i) + ", ";
		}
		s += names.get(names.size() - 1);
		return s;
	}
}
