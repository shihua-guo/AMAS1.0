package com.binana.amas.repository;

import com.binana.amas.domain.Amember;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Amember entity.
 */
@SuppressWarnings("unused")
public interface AmemberRepository extends JpaRepository<Amember,Long> {

    @Query("select distinct amember from Amember amember left join fetch amember.associations left join fetch amember.departments left join fetch amember.roles")
    List<Amember> findAllWithEagerRelationships();

    @Query("select amember from Amember amember left join fetch amember.associations left join fetch amember.departments left join fetch amember.roles where amember.id =:id")
    Amember findOneWithEagerRelationships(@Param("id") Long id);

}
