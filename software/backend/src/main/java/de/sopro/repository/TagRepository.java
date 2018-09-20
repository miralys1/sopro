package de.sopro.repository;

import org.springframework.data.repository.CrudRepository;

import de.sopro.model.Tag;

/**
 * Repository for Tags
 * 
 * @author HRS3-R.105B
 *
 */
public interface TagRepository extends CrudRepository<Tag, String> {

}