package de.sopro.repository;

import org.springframework.data.repository.CrudRepository;

import de.sopro.model.User;

/**
 * Repository for Users
 * 
 * @author HRS3-R.105B
 *
 */
public interface UserRepository extends CrudRepository<User, Long> {

	/* additional method for searching the repository */
	User findByEmail(String email);

}
