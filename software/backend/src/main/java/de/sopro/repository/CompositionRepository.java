package de.sopro.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.sopro.model.Composition;

public interface CompositionRepository extends CrudRepository<Composition, Long>{

	
	List<Composition> findByOwnerId(Long id);

	List<Composition> findByViewersId(Long id);
	
	List<Composition> findByEditorsId(Long id);

	List<Composition> findByIsPublic(boolean isPublic);
}
