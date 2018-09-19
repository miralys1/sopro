package de.sopro.controller.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.sopro.model.User;
import de.sopro.model.send.DetailUser;
import de.sopro.repository.UserRepository;

/**
 * handles the authentification in our application
 * 
 * @author HRS3-R.105B
 *
 */
@RestController
public class AuthentificationController {

	// required repositories
	@Autowired
	UserRepository userRepo;

	/**
	 * returns information about the current logged in user
	 * 
	 * @param principal
	 *            contains information about the logged in user. {@code null} means
	 *            nobody is logged in.
	 * @return a DetailUser that represents the logged in user
	 */
	@CrossOrigin
	@RequestMapping(value = "/authentification", method = RequestMethod.GET)
	public ResponseEntity<DetailUser> getUserInformation(Principal principal) {
		// Somebody must be logged in
		if (principal == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		User user = userRepo.findByEmail(principal.getName());
		// logged in user must be in the userRepository
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		DetailUser dUser = user.createDetailUser();
		return new ResponseEntity<>(dUser, HttpStatus.OK);
	}

}