package de.sopro.controller.rest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.sopro.model.Compatibility;
import de.sopro.model.Composition;
import de.sopro.model.CompositionEdge;
import de.sopro.model.CompositionNode;
import de.sopro.model.User;
import de.sopro.model.send.CompLists;
import de.sopro.model.send.DetailComp;
import de.sopro.model.send.Edge;
import de.sopro.model.send.SimpleComp;
import de.sopro.repository.CompositionEdgeRepository;
import de.sopro.repository.CompositionNodeRepository;
import de.sopro.repository.CompositionRepository;
import de.sopro.repository.UserRepository;

@RestController
public class CompController {

	@Autowired
	private CompositionRepository compRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private CompositionNodeRepository nodeRepo;
	@Autowired
	private CompositionEdgeRepository edgeRepo;

	@Autowired
	private Compatibility compa;

	// TODO actual way to identificate the user

	/**
	 * Allows to get all the compositions that are either viewable or editable for
	 * the user.
	 * 
	 * @param userId
	 *            the userId of the user requesting the list
	 * @return a Object that contains two lists of compositions. One for editing and
	 *         one for viewing
	 */
	@CrossOrigin
	@RequestMapping(value = "/compositions", method = RequestMethod.GET)
	public ResponseEntity<CompLists> getCompositions(Principal principal) {

		if (principal == null) {
			// if user is not logged in, only public compositions are viewable, none
			// editable
			return new ResponseEntity<CompLists>(new CompLists(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
					convertListToSimple(compRepo.findByIsPublic(true), 0)), HttpStatus.OK);
		}

		User user = userRepo.findByEmail(principal.getName());
		// if user is logged in, editable, viewable and public composition are shown

		return new ResponseEntity<CompLists>(new CompLists(convertListToSimple(user.getOwnsComp(), user.getId()),
				convertListToSimple(user.getEditable(), user.getId()),
				convertListToSimple(user.getViewable(), user.getId()),
				convertListToSimple(compRepo.findByIsPublic(true), user.getId())), HttpStatus.OK);

	}

	@RequestMapping(value = "/compositions/{id}", method = RequestMethod.GET)
	public ResponseEntity<DetailComp> getCompositionDetail(@PathVariable long id, Principal principal) {

		Optional<Composition> opComp = compRepo.findById(id);
		if (opComp.isPresent()) {
			long userID = principal == null ? 0 : userRepo.findByEmail(principal.getName()).getId();
			Composition comp = opComp.get();

			if (isUserViewer(userID, comp)) {

				DetailComp dComp = comp.createDetailComp(userID);

				for (Edge edge : dComp.getEdges()) {
					edge.setCompatibility(compa.checkCompatibility(edge.getSource().getSendService().getId(),
							edge.getTarget().getSendService().getId()));
				}

				return new ResponseEntity<>(dComp, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/compositions", method = RequestMethod.POST)
	public ResponseEntity<Long> createComposition(@RequestBody String name, Principal principal) {

		if (principal == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		User owner = userRepo.findByEmail(principal.getName());

		Composition comp = new Composition(owner, name, false, new ArrayList<>(), new ArrayList<>());

		owner.getOwnsComp().add(comp);
		compRepo.save(comp);
		userRepo.save(owner);
		return new ResponseEntity<>(comp.getId(), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/compositions/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> editComposition(@RequestBody DetailComp dComp, Principal principal) {
		if (dComp == null || !compRepo.findById(dComp.getId()).isPresent()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Optional<Composition> opComp = compRepo.findById(dComp.getId());
		
		User user = userRepo.findByEmail(principal.getName());
		// User is not logged in or not authorized
		if (principal == null || !isViewerEditor(user, opComp.get())) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		Composition saveComp = dComp.createComposition(user);
		Composition emptyComp = new Composition(saveComp.getOwner(), saveComp.getName(), saveComp.isPublic(), new ArrayList<>(), new ArrayList<>());
		emptyComp.setId(saveComp.getId());
		compRepo.save(emptyComp);
		
		setIdsForComp(saveComp);
		compRepo.save(saveComp);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/compositions/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteComposition(@PathVariable(value = "id") long compId, Principal principal) {

		Optional<Composition> opComp = compRepo.findById(compId);

		if (!opComp.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Composition comp = opComp.get();

		if (principal == null || !isViewerEditor(userRepo.findByEmail(principal.getName()), comp)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		compRepo.delete(comp);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/////////////////
	// Helper Code //
	/////////////////

	// TODO: in Util Klasse auslagern

	private void setIdsForComp(Composition saveComp) {
		for (CompositionEdge e : saveComp.getEdges()) {
			for (CompositionNode n : saveComp.getNodes()) {
				if (n.getX() == e.getSource().getX() && n.getY() == e.getSource().getY()
						&& n.getId() == e.getSource().getId()) {
					e.setSource(n);
				} else if (n.getX() == e.getTarget().getX() && n.getY() == e.getTarget().getY()
						&& n.getId() == e.getTarget().getId()) {
					e.setTarget(n);
				}

			}
		}

		nodeRepo.saveAll(saveComp.getNodes());
		edgeRepo.saveAll(saveComp.getEdges());
	}

	private boolean isViewerEditor(User user, Composition composition) {
		return user.getId() == composition.getOwner().getId() || composition.getEditors().contains(user);
	}

	private boolean isUserViewer(long userID, Composition comp) {
		if (comp.isPublic()) {
			return true;
		}
		Optional<User> opUser = userRepo.findById(userID);
		if (!opUser.isPresent()) {
			return false;
		}
		User user = opUser.get();
		return comp.getOwner().equals(user) || comp.getViewers().contains(user) || comp.getEditors().contains(user);
	}

	private List<SimpleComp> convertListToSimple(Iterable<Composition> comps, long userID) {
		List<SimpleComp> simpleComps = new ArrayList<>();
		for (Composition comp : comps) {
			simpleComps.add(comp.createSimpleComp(userID));
		}
		return simpleComps;
	}
}