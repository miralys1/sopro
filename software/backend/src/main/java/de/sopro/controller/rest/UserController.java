package de.sopro.controller.rest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.sopro.model.User.User;
import de.sopro.model.send.DetailUser;
import de.sopro.model.send.SimpleUser;
import de.sopro.repository.UserRepository;
import de.sopro.model.User.UserRegistrationDto;
import de.sopro.model.User.UserService;

/**
 * handles the requests about Users
 * 
 * @author HRS3-R.105B
 *
 */
@RestController
public class UserController {

	// required repositories
	@Autowired
	private UserRepository userRepo;

	@Autowired
	UserService userService;

	/**
	 * Allows to get all users that are saved. Optionally the users are filtered by
	 * {@code searchString}.
	 * 
	 * @param searchString
	 *            string that should be contained by the names of the users that are
	 *            returned
	 * @return all users or the users which names contain the searchString.
	 */
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ResponseEntity<Iterable<SimpleUser>> getUsers(
			@RequestParam(value = "search", defaultValue = "") String searchString) {
		Iterable<User> user = userRepo.findAll();
		List<SimpleUser> simpUsers = new ArrayList<>();

		// filter users
		for (User u : user) {
			if (u.getFullName().contains(searchString) || searchString.equals("")) {
				simpUsers.add(u.createSimpleUser());
			}
		}

		Iterable<SimpleUser> result = simpUsers;
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * Allows to get the details of the user indicated by {@code id}
	 * 
	 * @param id
	 *            id of the user which should be returned
	 * @param principal
	 *            contains information about the logged in user. {@code null} means
	 *            nobody is logged in.
	 * @return the user indicated by {@code id}
	 */
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public ResponseEntity<DetailUser> getUserDetails(@PathVariable long id, Principal principal) {

		// somebody must be logged in
		if (principal == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		// logged in user must exist
		User loggedUser = userRepo.findByEmail(principal.getName());
		if (loggedUser == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		// logged in user must be an admin or the owner of the requested user account
		if (!loggedUser.isAdmin() && loggedUser.getId() != id) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		// requested user must exist
		Optional<User> user = userRepo.findById(id);
		if (!user.isPresent()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		User u = user.get();
		return new ResponseEntity<>(u.createDetailUser(), HttpStatus.OK);
	}

	/**
	 * Allows to edit the user indicated by {@code id}
	 * 
	 * @param principal
	 *            contains information about the logged in user. {@code null} means
	 *            nobody is logged in.
	 * @param id
	 *            id of the user that should be edited
	 * @param detailUser
	 *            user informations that should be saved for the user indicated by
	 *            {@code id}
	 * @return {@code HttpStatus.OK} on success.
	 */
	@RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> editUser(Principal principal, @PathVariable long id,
			@RequestBody DetailUser detailUser) {
		// sombeody musst be logged in
		if (principal == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		// logged in user must exist
		User loggedUser = userRepo.findByEmail(principal.getName());
		if (loggedUser == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		// logged in user must be admin or the owner of the requested user account
		if (!loggedUser.isAdmin() && loggedUser.getId() != id) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		// user that should be edited must exist
		Optional<User> user = userRepo.findById(id);
		if (!user.isPresent()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		// update user information
		User oldUser = user.get();
		oldUser.setFirstName(detailUser.getFirstName());
		oldUser.setLastName(detailUser.getLastName());
		oldUser.setEmail(detailUser.getEmail());
		oldUser.setTitle(detailUser.getTitle());
		userRepo.save(oldUser);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Allows to delete an user
	 * 
	 * @param id
	 *            id of the user that should be deleted
	 * @param principal
	 *            contains information about the logged in user. {@code null} means
	 *            nobody is logged in.
	 * @return {@code HttpStatus.OK} on success
	 */
	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteUser(@PathVariable long id, Principal principal) {

		// somebody must be logged in
		if (principal == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		// logged in user must be an admin or the owner of the user account
		User user = userRepo.findByEmail(principal.getName());
		if (!(user.getId() == id || user.isAdmin())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		// user that should be deleted must exist
		Optional<User> delUser = userRepo.findById(id);
		if (!delUser.isPresent()) {
			return ResponseEntity.badRequest().build();
		}

		userRepo.delete(delUser.get());
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public ResponseEntity<Void> register(@RequestBody @Valid UserRegistrationDto userDto) {

		User existing = userService.findByEmail(userDto.getEmail());

		if(existing != null){
			return ResponseEntity.badRequest().build();
		}

		userService.save(userDto);


		return new ResponseEntity<>(HttpStatus.CREATED);
	}

}
