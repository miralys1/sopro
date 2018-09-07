package de.sopro.repository;

import org.springframework.data.repository.CrudRepository;

import de.sopro.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

}
