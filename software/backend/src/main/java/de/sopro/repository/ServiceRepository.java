package de.sopro.repository;

import org.springframework.data.repository.CrudRepository;

import de.sopro.model.Service;

public interface ServiceRepository extends CrudRepository<Service, Long> {

}
