package de.sopro.model;

import de.sopro.model.send.Alternative;
import de.sopro.model.send.CompatibilityAnswer;
import de.sopro.repository.ServiceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Component
public class Compatibility {

	@Autowired
	ServiceRepository serviceRepo;

	private static final String FLEXIBLE_MARKER = "flexible";
	private static final String VERSION_DELIMS = "xX.";
	private static final Tag CONVERTER_TAG = new Tag("converter");

	/**
	 * Checks if two services given by their IDs are compatible.
	 * 
	 * @param serviceID1
	 *            the service producing information
	 * @param serviceID2
	 *            the service receiving information
	 * @return Information on the compatibility of the services (suiting formats,
	 *         alternatives)
	 */
	public CompatibilityAnswer checkCompatibility(long serviceID1, long serviceID2) {

		// Get services from database
		Service s1 = serviceRepo.findById(serviceID1).get();
		Service s2 = serviceRepo.findById(serviceID2).get();

		// get list of names of all formats in the intersection
		List<String> suitingFormats = intersect(s1.getFormatOut(), s2.getFormatIn());

		boolean isCompatible = suitingFormats.isEmpty();

		List<Alternative> alternatives = new ArrayList<Alternative>();

		if (isCompatible) {

			// Services are compatible, thus no alternatives have to be searched
			return new CompatibilityAnswer(isCompatible, suitingFormats, alternatives);

		} else {

			for (Service service : serviceRepo.findAll()) {

				// Get the intersection
				List<String> intersect1 = intersect(s1.getFormatOut(), service.getFormatIn());
				List<String> intersect2 = intersect(service.getFormatOut(), s2.getFormatIn());
				boolean converter = service.getTags().contains(CONVERTER_TAG);

				if (converter && !intersect1.isEmpty() && !intersect2.isEmpty()) {

					// Create list of IDs, versions and names and add it to the
					// alternatives
					List<Long> ids = new ArrayList<Long>();
					ids.add(service.getId());
					List<String> versions = new ArrayList<String>();
					versions.add(service.getVersion());
					List<String> names = new ArrayList<String>();
					names.add(service.getName());

					Alternative alternative = new Alternative(names, versions, ids);
					alternatives.add(alternative);

				}

			}

		}

		return new CompatibilityAnswer(isCompatible, suitingFormats, alternatives);

	}

	/**
	 * computes the intersection of two given lists
	 * 
	 * @param list1
	 *            the first list
	 * @param list2
	 *            the second list
	 * @return the intersection of both lists
	 */
	private static List<String> intersect(List<Format> list1, List<Format> list2) {

		List<String> intersection = new ArrayList<String>();

		// Go through first list and check for every elem if it is also in the
		// second list
		for (Format elem : list1) {
			for (Format elem2 : list2) {
				if (!elem.equals(elem2) && isCompatible(elem, elem2)) {
					intersection.add(elem.getType());
				}
			}
		}

		return intersection;

	}

	/**
	 * Checks
	 * 
	 * @param f1
	 * @param f2
	 * @return
	 */
	private static boolean isCompatible(Format f1, Format f2) {

		// If the formats do not have the same name, the can't be compatible
		if (!f1.getType().equals(f2.getType())) {
			return false;
		}

		if (f2.getCompatibilityDegree().equals(FLEXIBLE_MARKER)) {

			// Seperate all numbers in the version string
			StringTokenizer tokenizer1 = new StringTokenizer(f1.getVersion(), VERSION_DELIMS);
			StringTokenizer tokenizer2 = new StringTokenizer(f2.getVersion(), VERSION_DELIMS);

			while (tokenizer1.hasMoreTokens() && tokenizer2.hasMoreTokens()) {

				// get next number of version
				String t1 = tokenizer1.nextToken();
				String t2 = tokenizer2.nextToken();

				// if they are empty, do nothing
				if (t1.isEmpty() || t2.isEmpty()) {
					continue;
				}

				// parse the numbers to normal ints
				int v1 = Integer.parseInt(t1);
				int v2 = Integer.parseInt(t2);

				// if v1 is smaller than v2, f1 has lower version than f2, thus
				// the services are compatible
				if (v1 < v2) {
					return true;
				}
				// if v1 is bigger than v2, f1 has a higher version than f2,
				// thus the services are not compatible
				if (v1 > v2) {
					return false;
				}

			}

			// The versions of both formats are identical => they are
			// compatible, yeah
			return true;

		} else {

			// only check if version strings are equal (we can assume that names
			// are equal due to the first name check)
			assert f1.getType().equals(f2.getType());

			return f1.getVersion().equals(f2.getVersion());
		}

	}

}
