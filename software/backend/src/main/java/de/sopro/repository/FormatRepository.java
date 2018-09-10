package de.sopro.repository;

import org.springframework.data.repository.CrudRepository;

import de.sopro.model.Format;

public interface FormatRepository extends CrudRepository<Format, Long> {

	boolean existsByTypeAndVersion(String type, String version);
}
