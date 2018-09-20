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
import de.sopro.model.User.User;
import de.sopro.model.send.SimpleUser;
import de.sopro.model.send.UserAuthorizations;
import de.sopro.repository.CompositionRepository;
import de.sopro.repository.UserRepository;

/**
 * handles requests about view and edit permissions
 * 
 * @author HRS3-R.105B
 *
 */
@RestController
public class PermissionController {

	// required repositories
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private CompositionRepository compRepo;

	/**
	 * Allows to get the Permissions for the composition indicated by {@code id}
	 * 
	 * @param id
	 *            id of the composition which permissions should be returned
	 * @param principal
	 *            contains information about the logged in user. {@code null} means
	 *            nobody is logged in.
	 * @return the permissions of the composition indicated by {@code id}
	 */
	@RequestMapping(value = "/compositions/{id}/users", method = RequestMethod.GET)
	public ResponseEntity<UserAuthorizations> getUserPermissions(@PathVariable long id, Principal principal) {
		Optional<Composition> opComp = compRepo.findById(id);
		// the composition must exist
		if (!opComp.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Composition comp = opComp.get();

		// logged in user must have the permission to get the permissions
		if (!authorized(principal, comp)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		// create UserAuthorizations
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

	/**
	 * Gives the user indicated by {@code email} permissions for the composition
	 * indicated by {@code id}
	 * 
	 * @param id
	 *            id of the composition which the user should get permissions for
	 * @param email
	 *            email of the user that should get permissions
	 * @param permission
	 *            the permission that the user should get. Must be "editor" or
	 *            "viewer".
	 * @param principal
	 *            contains information about the logged in user. {@code null} means
	 *            nobody is logged in.
	 * @return {@code HttpStatus.OK} on success.
	 */
	@RequestMapping(value = "/compositions/{id}/users/{email}", method = RequestMethod.POST)
	public ResponseEntity<Void> createUserPermission(@PathVariable long id, @PathVariable String email,
			@RequestBody String permission, Principal principal) {
		Optional<Composition> opComp = compRepo.findById(id);
		// composition must exist
		if (!opComp.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Composition comp = opComp.get();

		// logged in user must have the permission to give other users permissions
		if (!authorized(principal, comp)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		// owner is not allowed to give himself permissions
		if (principal.getName().equals(email)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		// user that should get permissions must exist
		User user = userRepo.findByEmail(email);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// saves permission for the user
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

	/**
	 * changes the permission of the user indicated by {@code email} for the
	 * composition indicated by {@code id}. If the user is an editor he will be a
	 * viewer. If the user is an viewer he will be an editor.
	 * 
	 * @param id
	 *            id of the composition for which the permissions of the user should
	 *            be changed
	 * @param email
	 *            email of the user which permission should be changed.
	 * @param principal
	 *            contains information about the logged in user. {@code null} means
	 *            nobody is logged in.
	 * @return {@code HttpStatus.OK} on success
	 */
	@RequestMapping(value = "/compositions/{id}/users/{email}", method = RequestMethod.PUT)
	public ResponseEntity<Void> editUserPermission(@PathVariable long id, @PathVariable String email,
			Principal principal) {

		// composition must exist
		Optional<Composition> opComp = compRepo.findById(id);
		if (!opComp.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Composition comp = opComp.get();

		// logged in user must have permission to edit permissions
		if (!authorized(principal, comp)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		// user which permission should be changed must exist
		User user = userRepo.findByEmail(email);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// user must be either viewer or editor
		boolean editor = inList(comp.getEditors(), user);
		boolean viewer = inList(comp.getViewers(), user);

		if ((editor && viewer) || (!editor && !viewer)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		// swaps user permission
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

	/**
	 * Allows to delete the permissions of the user indicated by {@code userID} for
	 * the composition indicated by {@code compId}
	 * 
	 * @param compId
	 *            id of the composition for which the permissions should be changed
	 * @param userId
	 *            id of the user which permission should be deleted
	 * @param principal
	 *            contains information about the logged in user. {@code null} means
	 *            nobody is logged in.
	 * @return {@code HttpStatus.OK} on success.
	 */
	@RequestMapping(value = "/compositions/{compId}/users/{userId}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteUserPermission(@PathVariable long compId, @PathVariable long userId,
			Principal principal) {

		// composition must exist
		Optional<Composition> opComp = compRepo.findById(compId);
		if (!opComp.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Composition comp = opComp.get();

		// user must have the permission to delete permission
		if (!authorized(principal, comp)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		// user which permission should be deleted must exist
		Optional<User> opUser = userRepo.findById(userId);
		if (!opUser.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		User user = opUser.get();

		// delete permissions
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

	/**
	 * checks whether {@code user} exists in {@code users}
	 * 
	 * @param users
	 *            list of user
	 * @param user
	 *            user that should checked whether it is in {@code users}
	 * @return whether {@code user} exists in {@code users}
	 */
	private boolean inList(List<User> users, User user) {
		for (User u : users) {
			if (u.getId() == user.getId()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * checks whether the logged in user is the owner of {@code comp}.
	 * 
	 * @param principal
	 *            contains information about the logged in user. {@code null} means
	 *            nobody is logged in.
	 * @param comp
	 *            Composition which owner should exist
	 * @return whether the logged in user and the owner of {@code comp} exists
	 */
	private boolean authorized(Principal principal, Composition comp) {
		// somebody must be logged in
		if (principal == null) {
			return false;
		}

		User user = userRepo.findByEmail(principal.getName());
		// logged in user must exist
		if (user == null) {
			return false;
		}

		// logged in user must be the owner of the composition
		if (!comp.getOwner().getEmail().equals(user.getEmail())) {
			return false;
		}
		return true;
	}

}
