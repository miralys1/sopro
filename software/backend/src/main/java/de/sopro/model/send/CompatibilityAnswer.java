package de.sopro.model.send;

import java.util.List;

public class CompatibilityAnswer {

	private boolean compatible;

	private List<String> suitingFormats;

	List<Alternative> compatibleServices;

	public CompatibilityAnswer(boolean compatible, List<String> suitingFormats,
			List<Alternative> compatibleServices) {
		this.compatible = compatible;
		this.suitingFormats = suitingFormats;
		this.compatibleServices = compatibleServices;
	}

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

	public String toString() {
		String s = compatible ? "Services are compatible" : "Services are not compatible";
		return s;
	}
}
