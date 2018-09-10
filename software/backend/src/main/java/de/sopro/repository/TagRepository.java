package de.sopro.repository;

import org.springframework.data.repository.CrudRepository;

import de.sopro.model.Tag;

public interface TagRepository extends CrudRepository<Tag, String>{

}