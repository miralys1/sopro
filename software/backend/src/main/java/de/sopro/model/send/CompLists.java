package de.sopro.model.send;

/**
 * An instance of this class contains four lists with compositions to
 * distinguish them.
 * 
 * @author HRS3-R.105B
 *
 */
public class CompLists {
	/* required variables */
	private Iterable<SimpleComp> editable;
	private Iterable<SimpleComp> viewable;
	private Iterable<SimpleComp> publicComps;
	private Iterable<SimpleComp> owns;

	/**
	 * creates a compLists with the given values
	 * 
	 * @param owns
	 *            Compositions that are owned
	 * @param editable
	 *            Compositions that can be edited
	 * @param viewable
	 *            Compositions that can only be viewed
	 * @param publicComps
	 *            Compositions that are public
	 */
	public CompLists(Iterable<SimpleComp> owns, Iterable<SimpleComp> editable, Iterable<SimpleComp> viewable,
			Iterable<SimpleComp> publicComps) {
		this.editable = editable;
		this.viewable = viewable;
		this.publicComps = publicComps;
		this.owns = owns;
	}

	/* getter and setter */
	public Iterable<SimpleComp> getEditable() {
		return editable;
	}

	public Iterable<SimpleComp> getviewable() {
		return viewable;
	}

	public Iterable<SimpleComp> getpublicComps() {
		return publicComps;
	}

	public Iterable<SimpleComp> getOwns() {
		return owns;
	}
}