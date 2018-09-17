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

import de.sopro.model.Composition;
import de.sopro.model.User;
import de.sopro.model.send.SimpleUser;
import de.sopro.model.send.UserAuthorizations;
import de.sopro.repository.CompositionRepository;
import de.sopro.repository.UserRepository;

@RestController
public class PermissionController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CompositionRepository compRepo;

	//TODO: javadocs
	@RequestMapping(value = "/compositions/{id}/users", method = RequestMethod.GET)
	public ResponseEntity<UserAuthorizations> getUserPermissions(@PathVariable long id, Principal principal) {
		Optional<Composition> opComp = compRepo.findById(id);
		if (!opComp.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Composition comp = opComp.get();
		if (!authorized(principal, comp)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		List<SimpleUser> editors = new ArrayList<>();
		for (User u : comp.getEditors()) {
			editors.add(u.createSimpleUser());
		}

		List<SimpleUser> viewers = new ArrayList<>();
		for (User u : comp.getViewers()) {
			viewers.add(u.createSimpleUser());
		}

		UserAuthorizations userAuths = new UserAuthorizations(editors, viewers);
		return new ResponseEntity<>(userAuths, HttpStatus.OK);
	}

	@RequestMapping(value = "/compositions/{id}/users/{email}", method = RequestMethod.POST)
	public ResponseEntity<Void> createUserPermission(@PathVariable long id, @PathVariable String email,
			@RequestBody String permission, Principal principal) {
		Optional<Composition> opComp = compRepo.findById(id);
		if (!opComp.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Composition comp = opComp.get();
		if (!authorized(principal, comp)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		// owner is not allowed to give himself permissions
		if (principal.getName().equals(email)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		User user = userRepo.findByEmail(email);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		switch (permission) {
		case "editor":
			if (!inList(comp.getEditors(), user)) {
				user.getEditable().add(comp);
				comp.getEditors().add(user);
			}
			break;
		case "viewer":
			if (!inList(comp.getViewers(), user)) {
				user.getViewable().add(comp);
				comp.getViewers().add(user);
			}
			break;
		default:
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		userRepo.save(user);
		compRepo.save(comp);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/compositions/{id}/users/{email}", method = RequestMethod.PUT)
	public ResponseEntity<Void> editUserPermission(@PathVariable long id, @PathVariable String email,
			Principal principal) {

		Optional<Composition> opComp = compRepo.findById(id);
		if (!opComp.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Composition comp = opComp.get();
		if (!authorized(principal, comp)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		User user = userRepo.findByEmail(email);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		boolean editor = inList(comp.getEditors(), user);
		boolean viewer = inList(comp.getViewers(), user);

		if ((editor && viewer) || (!editor && !viewer)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (editor) {
			comp.getEditors().remove(user);
			comp.getViewers().add(user);
			user.getEditable().remove(comp);
			user.getViewable().add(comp);
		} else {
			comp.getEditors().add(user);
			comp.getViewers().remove(user);
			user.getEditable().add(comp);
			user.getViewable().remove(comp);
		}

		compRepo.save(comp);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/compositions/{compId}/users/{userId}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteUserPermission(@PathVariable long compId, @PathVariable long userId,
			Principal principal) {

		Optional<Composition> opComp = compRepo.findById(compId);
		if (!opComp.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Composition comp = opComp.get();
		if (!authorized(principal, comp)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		Optional<User> opUser = userRepo.findById(userId);
		if (!opUser.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		User user = opUser.get();

		comp.getEditors().remove(user);
		comp.getViewers().remove(user);
		user.getEditable().remove(comp);
		user.getViewable().remove(comp);

		compRepo.save(comp);
		userRepo.save(user);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/////////////////
	// Helper Code //
	/////////////////

	private boolean inList(List<User> viewers, User user) {
		for (User u : viewers) {
			if (u.getId() == user.getId()) {
				return true;
			}
		}
		return false;
	}

	private boolean authorized(Principal principal, Composition comp) {
		if (principal == null) {
			return false;
		}

		User user = userRepo.findByEmail(principal.getName());
		if (user == null) {
			return false;
		}

		if (!comp.getOwner().getEmail().equals(user.getEmail())) {
			return false;
		}
		return true;
	}

}
