package de.sopro.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import de.sopro.model.User;
import de.sopro.repository.UserRepository;

@Component
public class DetailsService implements UserDetailsService {

  @Autowired
  UserRepository  userRepo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    
    User user = userRepo.findByEmail(username);
    if(user == null){
      throw new UsernameNotFoundException(username + " is no registered user");
    }
    
    return new org.springframework.security.core.userdetails.User(user.getEmail(), 
                                                                  user.getPassword(), 
                                                                  AuthorityUtils.createAuthorityList(user.getRole()));

	}

}