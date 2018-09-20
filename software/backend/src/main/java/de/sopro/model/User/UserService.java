package de.sopro.model.User;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Interface that defines the Methods that are necessary to manage the registered users
 */
public interface UserService extends UserDetailsService{

    User findByEmail(String email);

    User save(UserRegistrationDto registration);    

}