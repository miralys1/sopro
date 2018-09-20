package de.sopro.model.User;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import de.sopro.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepo.findByEmail(email);
        if(user==null){
            throw new UsernameNotFoundException("Invalid Username");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());

    }

    
    
	@Override
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }
    
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