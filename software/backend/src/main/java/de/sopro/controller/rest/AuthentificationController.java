package de.sopro.controller.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.sopro.model.User;
import de.sopro.model.send.DetailUser;
import de.sopro.repository.UserRepository;

@RestController
public class AuthentificationController{

    @Autowired
    UserRepository userRepo;

    @RequestMapping(value="/authentification", method=RequestMethod.GET)
    public ResponseEntity<DetailUser> getUserInformation(Principal principal){
        if(principal == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User user = userRepo.findByEmail(principal.getName());
        if(user == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        DetailUser dUser = user.createDetailUser();
        return new ResponseEntity<>(dUser, HttpStatus.OK);
    }


}