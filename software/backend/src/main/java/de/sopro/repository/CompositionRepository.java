package de.sopro.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.sopro.model.Composition;

/**
 * Repository for Compositions
 * 
 * @author BHRS3-R.105B
 *
 */
public interface CompositionRepository extends CrudRepository<Composition, Long> {

	/* additional methods for searching the repository */
	List<Composition> findByOwnerId(Long id);

	List<Composition> findByViewersId(Long id);

	List<Composition> findByEditorsId(Long id);

	List<Composition> findByIsPublic(boolean isPublic);
}
