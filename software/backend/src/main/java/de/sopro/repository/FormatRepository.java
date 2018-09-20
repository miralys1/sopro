package de.sopro.repository;

import org.springframework.data.repository.CrudRepository;

import de.sopro.model.Format;

/**
 * Repository for Formats
 * 
 * @author HRS3-R.105B
 *
 */
public interface FormatRepository extends CrudRepository<Format, Long> {

	/* additional method for searching the repository */
	Format findOneByTypeAndVersion(String type, String version);
}
