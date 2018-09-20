package de.sopro.controller.rest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
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
import de.sopro.model.User.User;
import de.sopro.model.send.CompLists;
import de.sopro.model.send.DetailComp;
import de.sopro.model.send.Edge;
import de.sopro.model.send.Node;
import de.sopro.model.send.SimpleComp;
import de.sopro.repository.CompositionRepository;
import de.sopro.repository.ServiceRepository;
import de.sopro.repository.UserRepository;

/**
 * handles requests about compositions
 * 
 * @author HRS3-R.105B
 *
 */
@RestController
public class CompController {

	// required repositories
	@Autowired
	private CompositionRepository compRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ServiceRepository serviceRepo;

	@Autowired
	private Compatibility compa;

	/**
	 * Allows to get all the compositions that are either viewable or editable for
	 * the user and the ones he owns.
	 * 
	 * @param principal
	 *            contains information about the logged in user. {@code null} means
	 *            nobody is logged in.
	 * @return a Object that contains four lists of compositions. One for editing,
	 *         one for viewing, one for public Compositions and one for the
	 *         Compositions the user owns.
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
		// if user is logged in, editable, viewable, owning and public composition are
		// shown

		// the compositions the logged in user owns should not be in the public list
		List<Composition> publicComps = compRepo.findByIsPublic(true);
		List<Composition> newPublics = new ArrayList<>();
		for (Composition comp : publicComps) {
			if (comp.getOwner().getId() != user.getId()) {
				newPublics.add(comp);
			}
		}

		return new ResponseEntity<CompLists>(new CompLists(convertListToSimple(user.getOwnsComp(), user.getId()),
				convertListToSimple(user.getEditable(), user.getId()),
				convertListToSimple(user.getViewable(), user.getId()), convertListToSimple(newPublics, user.getId())),
				HttpStatus.OK);

	}

	/**
	 * Allows to get a composition in detail.
	 * 
	 * @param id
	 *            the id of the composition that should be returned in detail
	 * @param principal
	 *            contains information about the logged in user. {@code null} means
	 *            nobody is logged in.
	 * @return a DetailComp representing the requested Composition.
	 */
	@RequestMapping(value = "/compositions/{id}", method = RequestMethod.GET)
	public ResponseEntity<DetailComp> getCompositionDetail(@PathVariable long id, Principal principal) {

		Optional<Composition> opComp = compRepo.findById(id);
		// requested composition must be in the compositionRepository
		if (!opComp.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		long userID = principal == null ? 0 : userRepo.findByEmail(principal.getName()).getId();
		Composition comp = opComp.get();

		// logged in user must have view rights in order to request the composition
		if (!isUserViewer(userID, comp)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		// create DetailComp that represents the requested composition
		DetailComp dComp = comp.createDetailComp(userID);
		// set for every edge the compatibility
		for (Edge edge : dComp.getEdges()) {
			edge.setCompatibility(compa.checkCompatibility(edge.getSource().getSendService().getId(),
					edge.getTarget().getSendService().getId()));
		}

		return new ResponseEntity<>(dComp, HttpStatus.OK);
	}

	/**
	 * Creates a new Composition with the given {@code name}
	 * 
	 * @param name
	 *            String indicating the name the new composition should have
	 * @param principal
	 *            contains information about the logged in user. {@code null} means
	 *            nobody is logged in.
	 * @return the id of the new composition
	 */
	@RequestMapping(value = "/compositions", method = RequestMethod.POST)
	public ResponseEntity<Long> createComposition(@RequestBody String name, Principal principal) {

		// to create a new composition the user must be logged in
		if (principal == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		User owner = userRepo.findByEmail(principal.getName());

		// create new composition
		Composition comp = new Composition(owner, name, false, new ArrayList<>(), new ArrayList<>());

		// update owner
		owner.getOwnsComp().add(comp);
		compRepo.save(comp);
		userRepo.save(owner);

		return new ResponseEntity<>(comp.getId(), HttpStatus.CREATED);
	}

	/**
	 * updates a composition by overwrite it with {@code dComp}
	 * 
	 * @param dComp
	 *            DetailComposition that should be used to overwrite the old
	 *            composition. Must contain the id of the Composition that should be
	 *            overwritten.
	 * @param principal
	 *            contains information about the logged in user. {@code null} means
	 *            nobody is logged in.
	 * @return {@code HttpStatus.OK} on success
	 */
	@RequestMapping(value = "/compositions/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> editComposition(@RequestBody DetailComp dComp, Principal principal) {
		// new composition must contain the id of an existing composition
		if (dComp == null || !compRepo.findById(dComp.getId()).isPresent()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Optional<Composition> opComp = compRepo.findById(dComp.getId());

		User user = userRepo.findByEmail(principal.getName());
		// User is not logged in or not authorized to edit the composition
		if (principal == null || !isViewerEditor(user, opComp.get())) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		// create the new composition
		Composition newComp = createNewComp(dComp);
		if (newComp == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		// update the old composition
		newComp.setId(dComp.getId());
		compRepo.save(newComp);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Allows to change the public status of the Compostition
	 * 
	 * @param id
	 *            id of the COmposition which public status should be set
	 * @param principal
	 *            contains information about the logged in user. {@code null} means
	 *            nobody is logged in.
	 * @param pub
	 *            determines whether the Composition should be public or not
	 * @return {@code HttpStatus.OK} on success
	 */
	@RequestMapping(value = "/compositions/{id}/public", method = RequestMethod.PUT)
	public ResponseEntity<Void> setPublic(@PathVariable long id, Principal principal, @RequestBody boolean pub) {
		Optional<Composition> opComp = compRepo.findById(id);
		// composition that should be deleted must exist
		if (!opComp.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Composition comp = opComp.get();

		// logged in user must have the rights to deleted the composition
		if (principal == null || userRepo.findByEmail(principal.getName()).getId() != comp.getOwner().getId()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		comp.setPublic(pub);
		compRepo.save(comp);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Allows to delete a composition.
	 * 
	 * @param compId
	 *            the id of the composition that should be deleted
	 * @param principal
	 *            contains information about the logged in user. {@code null} means
	 *            nobody is logged in.
	 * @return {@code HttpsStatus.OK} on success
	 */
	@RequestMapping(value = "/compositions/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteComposition(@PathVariable(value = "id") long compId, Principal principal) {

		Optional<Composition> opComp = compRepo.findById(compId);
		// composition that should be deleted must exist
		if (!opComp.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Composition comp = opComp.get();

		// logged in user must have the rights to deleted the composition
		if (principal == null || !isViewerEditor(userRepo.findByEmail(principal.getName()), comp)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		// delete composition
		compRepo.delete(comp);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/////////////////
	// Helper Code //
	/////////////////

	/**
	 * creates a new composition that represents {@code dComp}
	 * 
	 * @param dComp
	 *            DetailComp that should be changed to a Composition
	 * @return a Composition representing {@code dComp}
	 */
	private Composition createNewComp(DetailComp dComp) {
		// collect given node ids
		HashMap<Long, CompositionNode> nodeIds = new HashMap<>();
		List<CompositionNode> nodes = new ArrayList<>();
		List<CompositionEdge> edges = new ArrayList<>();

		// change Nodes to CompositionNodes without ids
		for (Node n : dComp.getNodes()) {
			long oldId = n.getId();
			CompositionNode node = new CompositionNode(n.getX(), n.getY(),
					serviceRepo.findById(n.getSendService().getId()).get());
			nodes.add(node);
			nodeIds.put(oldId, node);
		}
		// change Edge to CompositionEdges without ids
		for (Edge e : dComp.getEdges()) {
			long sourceId = e.getSource().getId();
			long targetId = e.getTarget().getId();
			if (!nodeIds.containsKey(sourceId) || !nodeIds.containsKey(targetId)) {
				return null;
			}
			// hashmap contains the nodes the edge should reference
			CompositionNode source = nodeIds.get(sourceId);
			CompositionNode target = nodeIds.get(targetId);

			CompositionEdge edge = new CompositionEdge(source, target);
			edges.add(edge);
		}

		// owner of the composition must exist
		if (!userRepo.findById(dComp.getOwner().getId()).isPresent()) {
			return null;
		}

		// old composition must exist
		Optional<Composition> opComp = compRepo.findById(dComp.getId());
		if (!opComp.isPresent()) {
			return null;
		}

		// create new composition
		Composition comp = new Composition(userRepo.findById(dComp.getOwner().getId()).get(), dComp.getName(),
				dComp.isEditable(), nodes, edges);
		Composition saveComp = opComp.get();
		comp.setEditors(saveComp.getEditors());
		comp.setViewers(saveComp.getViewers());

		return comp;
	}

	/**
	 * checks whether {@code user} is allowed to edit {@code composition}
	 * 
	 * @param user
	 *            User which editor rights should be checked
	 * @param composition
	 *            Composition that should be checked whether {@code user} is allowed
	 *            to edit it.
	 * @return whether {@code user} is allowed to edit {@code composition}
	 */
	private boolean isViewerEditor(User user, Composition composition) {
		return user.getId() == composition.getOwner().getId() || composition.getEditors().contains(user);
	}

	/**
	 * Checks whether the user indicated by {@code userID} is allowed to see
	 * {@code comp}
	 * 
	 * @param userID
	 *            id indicating the user which view rights should be checked
	 * @param comp
	 *            Composition that should be checked whether the user indicated by
	 *            {@code userID} is allowed to see it.
	 * @return whether the user indicated by {@code userID} is allowed to see
	 *         {@code comp}
	 */
	private boolean isUserViewer(long userID, Composition comp) {
		if (comp.isPublic()) {
			return true;
		}
		// user indicated by userID must exist
		Optional<User> opUser = userRepo.findById(userID);
		if (!opUser.isPresent()) {
			return false;
		}

		User user = opUser.get();
		return comp.getOwner().equals(user) || comp.getViewers().contains(user) || comp.getEditors().contains(user);
	}

	/**
	 * converts a list of Compositions to a list of SimpleCompositions
	 * 
	 * @param comps
	 *            list of Compositions that should be converted
	 * @param userID
	 *            id of the user that is logged in
	 * @return a list of SimpleComp representing {@code comps}.
	 */
	private List<SimpleComp> convertListToSimple(Iterable<Composition> comps, long userID) {
		List<SimpleComp> simpleComps = new ArrayList<>();
		for (Composition comp : comps) {
			simpleComps.add(comp.createSimpleComp(userID));
		}
		return simpleComps;
	}
}