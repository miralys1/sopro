package de.sopro.model.send;

import java.util.List;

public class CompatibilityAnswer {

	private boolean isCompatible;

	private List<String> suitingFormats;

	List<Alternative> compatibleServices;

	public CompatibilityAnswer(boolean isCompatible, List<String> suitingFormats,
			List<Alternative> compatibleServices) {
		this.isCompatible = isCompatible;
		this.suitingFormats = suitingFormats;
		this.compatibleServices = compatibleServices;
	}

	public boolean isCompatible() {
		return isCompatible;
	}

	public void setCompatible(boolean isCompatible) {
		this.isCompatible = isCompatible;
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
		String s = isCompatible ? "Services are compatible" : "Services are not compatible";
		return s;
	}
}
