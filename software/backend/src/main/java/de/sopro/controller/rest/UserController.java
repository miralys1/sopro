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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.sopro.model.Service;
import de.sopro.model.User;
import de.sopro.model.send.DetailUser;
import de.sopro.model.send.SendService;
import de.sopro.model.send.SimpleUser;
import de.sopro.repository.UserRepository;

@RestController
public class UserController {

	@Autowired
	private UserRepository userRepo;

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ResponseEntity<Iterable<SimpleUser>> getUsers(
			@RequestParam(value = "search", defaultValue = "") String searchString) {
		Iterable<User> user = userRepo.findAll();
		List<SimpleUser> simpUsers = new ArrayList<>();

		for (User u : user) {
			if (u.getFullName().contains(searchString) || searchString.equals("")) {
				simpUsers.add(u.createSimpleUser());
			}
		}
		Iterable<SimpleUser> result = simpUsers;

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public ResponseEntity<DetailUser> getUserDetails(@PathVariable long id, Principal principal) {

		if (principal == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		User loggedUser = userRepo.findByEmail(principal.getName());
		if (loggedUser == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (!loggedUser.isAdmin() && loggedUser.getId() != id) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		Optional<User> user = userRepo.findById(id);
		if (!user.isPresent()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		User u = user.get();
		return new ResponseEntity<>(u.createDetailUser(), HttpStatus.OK);
	}

	// TODO:implement
	@RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> editUser(Principal principal, @PathVariable long id,
			@RequestBody SimpleUser detailUser) {
//		if (principal == null) {
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//
//		User loggedUser = userRepo.findByEmail(principal.getName());
//		if (loggedUser == null) {
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//
//		if (!loggedUser.isAdmin() && loggedUser.getId() != id) {
//			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//		}
//
//		Optional<User> user = userRepo.findById(id);
//		if (!user.isPresent()) {
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//
//		User oldUser = user.get();
//		User newUser = detailUser.createUser(oldUser.getPassword());
//		userRepo.save(newUser);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
