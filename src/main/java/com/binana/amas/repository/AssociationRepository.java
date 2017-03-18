package com.binana.amas.repository;

import com.binana.amas.domain.Amember;
import com.binana.amas.domain.Association;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Association entity.
 */
@SuppressWarnings("unused")
public interface AssociationRepository extends JpaRepository<Association,Long> {
	
	/*@Query("select amember from Amember amember where amember.id=:id")
	Set<Amember> findByMembassos_(@Param("id") Long id);*/
}
