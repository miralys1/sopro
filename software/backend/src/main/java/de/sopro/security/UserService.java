package de.sopro.security;

import org.springframework.security.core.userdetails.UserDetailsService;

import de.sopro.model.User;

public interface UserService extends UserDetailsService{
    User findByEmail(String email);

    User save(UserRegistrationDto registration);    

}