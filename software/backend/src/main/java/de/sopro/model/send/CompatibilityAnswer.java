package de.sopro.model.send;

import java.util.List;

/**
 * A CompatibilityAnswer contains information about the compatibiity of two
 * services
 * 
 * @author HRS3-R.105B
 *
 */
public class CompatibilityAnswer {

	/* required variables */
	private boolean compatible;
	private List<String> suitingFormats;
	List<Alternative> compatibleServices;

	/**
	 * creates a CompatibilityAnswer with the given values
	 * 
	 * @param compatible
	 *            determines whether the two services are compatible
	 * @param suitingFormats
	 *            the formats that ensure the compatibility if the services are
	 *            compatible. If the services are not compatible this list should be
	 *            empty.
	 * @param compatibleServices
	 *            contains alternatives that can convert formats to ensure
	 *            compatibility
	 */
	public CompatibilityAnswer(boolean compatible, List<String> suitingFormats, List<Alternative> compatibleServices) {
		this.compatible = compatible;
		this.suitingFormats = suitingFormats;
		this.compatibleServices = compatibleServices;
	}

	/* getter and setter */
	public boolean isCompatible() {
		return compatible;
	}

	public void setCompatible(boolean compatible) {
		this.compatible = compatible;
	}

	public List<String> getSuitingFormats() {
		return suitingFormats;
	}

	public void setSuitingFormats(List<String> suitingFormats) {
		this.suitingFormats = suitingFormats;
	}

	public List<Alternative> getCompatibleServices() {
		return compatibleServices;
	}

	public void setCompatibleServices(List<Alternative> compatibleServices) {
		this.compatibleServices = compatibleServices;
	}

	/**
	 * converts the CompatibilityAnswer to a String
	 */
	public String toString() {
		String s = compatible ? "Services are compatible" : "Services are not compatible";
		return s;
	}
}
