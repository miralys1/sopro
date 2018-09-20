package de.sopro.model.User;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import de.sopro.repository.UserRepository;

/**
 * Service that allows to manage the registered users.
 * Allows to add new User, find and authenticate them.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Retrieves a user from the database which is identified by a given username
     * @param email the email-address functions as username
     * @return the corresponding user to the username
     * @throws UsernameNotFoundException if the email doesn't belong to a registered User
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepo.findByEmail(email);
        if(user==null){
            throw new UsernameNotFoundException("Invalid Username");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());

    }


    /**
     * Retrieves a user via a given email-address
     * @param email the given address
     * @return the corresponding user, if existing, else {@code null}
     */
	@Override
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }


    /**
     * Allows to register a new user
     * @param registration the information send by the user wanting to register
     * @return the created User-instance, that is also saved in the database
     */
	@Override
	public User save(UserRegistrationDto registration) {
        User user = new User();

        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setEmail(registration.getEmail());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));


        if(registration.getTitle() == null){
            user.setTitle("");
        }else{
            user.setTitle(registration.getTitle());
        }

        user.setRoles(new String[0]);


        return userRepo.save(user);
	}

}