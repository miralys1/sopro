package de.sopro.controller.rest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.sopro.model.*;
import de.sopro.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.sopro.model.send.CompatibilityAnswer;
import de.sopro.model.send.SendService;

/**
 * handles requests about services
 * 
 * @author HRS3-R.105B
 *
 */
@RestController
public class ServiceController {

	// required repositories
	@Autowired
	private ServiceRepository serviceRepo;
	@Autowired
	private TagRepository tagRepo;
	@Autowired
	private FormatRepository formatRepo;
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CompositionRepository compRepo;

	@Autowired
	private Compatibility compa;


	private String dummyServiceName = "Service nicht existent";


	/**
	 * Method to get the existing services stored in the Database
	 * 
	 * @return all services with the given string contained in tags or name
	 */
	@RequestMapping(value = "/services", method = RequestMethod.GET)
	public ResponseEntity<Iterable<SendService>> getServices() {

		Service dummyService = serviceRepo.findByName(dummyServiceName);

		List<SendService> answer = new ArrayList<>();
		for (Service service : serviceRepo.findAll()) {
			if(service != dummyService) {
				answer.add(service.createSendService());
			}
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
	 * @param principal
	 *            contains information about the logged in user. {@code null} means
	 *            nobody is logged in.
	 * @return {@code HttpStatus.OK} on success
	 */
	@RequestMapping(value = "/services/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteService(@PathVariable("id") long id, Principal principal) {
		// logged in user must be an admin
		if (principal == null || !userRepo.findByEmail(principal.getName()).isAdmin()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		if(!serviceRepo.findById(id).isPresent()){
			return ResponseEntity.notFound().build();
		}


		Iterable<Composition> comps = compRepo.findAll();

		Service dummyService = serviceRepo.findByName(dummyServiceName);

		for(Composition comp : comps){
			for(CompositionNode node : comp.getNodes()){
				if(node.getService().getId() == id){
					node.setService(dummyService);
				}
			}
		}




		serviceRepo.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * creates new service entries in the database
	 * 
	 * @param services
	 *            services that should be saved
	 * @param principal
	 *            contains information about the logged in user. {@code null} means
	 *            nobody is logged in.
	 * @return {@code HttpStatus.CREATED} on success
	 */
	@RequestMapping(value = "/services", method = RequestMethod.POST)
	public ResponseEntity<Void> createServices(@RequestBody List<SendService> services, Principal principal) {

		// logged in user must be an admin
		if (principal == null || !userRepo.findByEmail(principal.getName()).isAdmin()) {
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

			// save service
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
	/**
	 * Method to manipulate a service that is already in the database
	 * 
	 * @param id
	 *            the id of the service that should be manipulated
	 * @param sendService
	 *            the new service that is to be saved
	 * @param principal
	 *            contains information about the logged in user. {@code null} means
	 *            nobody is logged in.
	 * @return {@code HttpStatus.OK} on success, else {@code HttpStatus.NOT_FOUND}
	 */
	@RequestMapping(value = "/services/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> editService(@PathVariable long id, @RequestBody SendService sendService,
			Principal principal) {

		// logged in user must be an admin
		if (principal == null || !userRepo.findByEmail(principal.getName()).isAdmin()) {
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

			// save service
			serviceRepo.save(service);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * checks whether the two services indicated by {@code id1} and {@code id2} are
	 * compatible
	 * 
	 * @param id1
	 *            id that indicates the service that is the source
	 * @param id2
	 *            id that indicates the service that is the target
	 * @return a CompatibilityAnswer that contains whether the two services are
	 *         compatible
	 */
	@RequestMapping(value = "/services/{id1}/{id2}", method = RequestMethod.GET)
	public ResponseEntity<CompatibilityAnswer> checkCompatibility(@PathVariable long id1, @PathVariable long id2) {
		// the services indicated by the ids must exist
		if (!serviceRepo.existsById(id1) || !serviceRepo.existsById(id2)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// check compatibility
		CompatibilityAnswer answer = compa.checkCompatibility(id1, id2);
		return new ResponseEntity<>(answer, HttpStatus.OK);
	}

}