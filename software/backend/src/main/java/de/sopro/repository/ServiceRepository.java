package de.sopro.repository;

import org.springframework.data.repository.CrudRepository;

import de.sopro.model.Service;

/**
 * Repository for Services
 * 
 * @author HRS3-R.105B
 *
 */
public interface ServiceRepository extends CrudRepository<Service, Long> {

}
