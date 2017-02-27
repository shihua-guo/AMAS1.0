package com.binana.amas.repository;

import com.binana.amas.domain.Property;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Property entity.
 */
@SuppressWarnings("unused")
public interface PropertyRepository extends JpaRepository<Property,Long> {

}
