package de.sopro.controller.rest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.sopro.model.Compatibility;
import de.sopro.model.Format;
import de.sopro.model.Service;
import de.sopro.model.Tag;
import de.sopro.model.send.CompatibilityAnswer;
import de.sopro.model.send.SendService;
import de.sopro.repository.FormatRepository;
import de.sopro.repository.ServiceRepository;
import de.sopro.repository.TagRepository;
import de.sopro.repository.UserRepository;

@RestController
public class ServiceController {

	// Repositories to save transmitted data
	@Autowired
	private ServiceRepository serviceRepo;
	@Autowired
	private TagRepository tagRepo;
	@Autowired
	private FormatRepository formatRepo;

	@Autowired
	private Compatibility compa;

	@Autowired
	private UserRepository userRepo;

	/**
	 * Method to get the existing services stored in the Database
	 * 
	 * @param searchString
	 *            a string that must be contained in either the name or the tags of
	 *            a service
	 * @return all services with the given string contained in tags or name
	 */
	@RequestMapping(value = "/services", method = RequestMethod.GET)
	public ResponseEntity<Iterable<SendService>> getServices() {
		List<SendService> answer = new ArrayList<>();
		for(Service service : serviceRepo.findAll()){
			answer.add(service.createSendService());
		}

		return new ResponseEntity<>(answer, HttpStatus.OK);
	}

	/**
	 * Method to retrieve the details about one service
	 * 
	 * @param id
	 *            the ID of the service in question
	 * @return the Details of the Service in form of a HTTP-response
	 */
	@RequestMapping(value = "/services/{id}", method = RequestMethod.GET)
	public ResponseEntity<SendService> getServiceDetails(@PathVariable long id) {
		Optional<Service> service = serviceRepo.findById(id);
		if (service.isPresent()) {
			return new ResponseEntity<>(service.get().createSendService(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Deletes the given service from the database
	 * 
	 * @param id
	 *            the id ID of the service that is to be deleted
	 * @return HTTP-Response ok, if the deletion was a success
	 */
	@RequestMapping(value = "/services/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteService(@PathVariable("id") long id, Principal principal) {
		if(principal == null || !userRepo.findByEmail(principal.getName()).isAdmin()){
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		serviceRepo.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Creates a new service entry in the database
	 * 
	 * @param service
	 *            the service without an id
	 * @return HTTP-response ok, if creation was a success
	 */
	@RequestMapping(value = "/services", method = RequestMethod.POST)
	public ResponseEntity<Void> createServices(@RequestBody List<SendService> services, Principal principal) {

		if(principal == null || !userRepo.findByEmail(principal.getName()).isAdmin()){
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		for (SendService sendService : services) {

			Service service = sendService.createService();

			// first save all tags and services, that are referenced
			for (Tag t : service.getTags()) {
				tagRepo.save(t);
			}
			for (Format f : service.getFormatIn()) {
				Format fSaved = formatRepo.findOneByTypeAndVersion(f.getType(), f.getVersion());
				if (fSaved == null) {
					formatRepo.save(f);
				} else {
					f.setId(fSaved.getId());
				}
			}
			for (Format f : service.getFormatOut()) {
				Format fSaved = formatRepo.findOneByTypeAndVersion(f.getType(), f.getVersion());
				if (fSaved == null) {
					formatRepo.save(f);
				} else {
					f.setId(fSaved.getId());
				}
			}
			serviceRepo.save(service);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * Method to manpulate a service that is already in the database
	 * 
	 * @param id
	 *            the id of the service
	 * @param service
	 *            the new service that is to be saved
	 * @return {@code HttpStatus.OK} if the service was already saved else
	 *         {@code HttpStatus.NOT_FOUND}
	 */
	@RequestMapping(value = "/services/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> editService(@PathVariable long id, @RequestBody SendService sendService, Principal principal) {

		if(principal == null || !userRepo.findByEmail(principal.getName()).isAdmin()){
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		if (serviceRepo.existsById(id) && sendService.getId() == id) {
			Service service = sendService.createService();
			// first save all tags and services, that are referenced
			for (Tag t : service.getTags()) {
				tagRepo.save(t);
			}
			for (Format f : service.getFormatIn()) {
				Format fSaved = formatRepo.findOneByTypeAndVersion(f.getType(), f.getVersion());
				if (fSaved == null) {
					formatRepo.save(f);
				} else {
					f.setId(fSaved.getId());
				}
			}
			for (Format f : service.getFormatOut()) {
				Format fSaved = formatRepo.findOneByTypeAndVersion(f.getType(), f.getVersion());
				if (fSaved == null) {
					formatRepo.save(f);
				} else {
					f.setId(fSaved.getId());
				}
			}

			serviceRepo.save(service);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/services/{id1}/{id2}", method = RequestMethod.GET)
	public ResponseEntity<CompatibilityAnswer> checkCompatibility(@PathVariable long id1, @PathVariable long id2) {
		if (!serviceRepo.existsById(id1) || !serviceRepo.existsById(id2)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		CompatibilityAnswer answer = compa.checkCompatibility(id1, id2);
		return new ResponseEntity<>(answer, HttpStatus.OK);
	}

	////////////////////////////////
	// Helper Methods and classes //
	////////////////////////////////

	/**
	 * Small method that allows to search for a substring in the tags of a service
	 * 
	 * @param str
	 *            the string that should be in at least one tag
	 * @param s
	 *            the service
	 * @return {@code true} if the string was found in one of the tags, else
	 *         {@code false}
	 */
	private boolean stringInTags(String str, Service s) {
		for (Tag t : s.getTags()) {
			if (t.getName().contains(str)) {
				return true;
			}
		}
		return false;
	}

}