package com.binana.amas.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.binana.amas.domain.Amember;

/**
 * Spring Data JPA repository for the Amember entity.
 */
@SuppressWarnings("unused")
public interface AmemberRepository extends JpaRepository<Amember,Long> {

    @Query("select distinct amember from Amember amember left join fetch amember.associations left join fetch amember.departments left join fetch amember.roles")
    List<Amember> findAllWithEagerRelationships();

    @Query("select amember from Amember amember left join fetch amember.associations left join fetch amember.departments left join fetch amember.roles where amember.id =:id")
    Amember findOneWithEagerRelationships(@Param("id") Long id);
    
    Page<Amember> findByAssociations_Id(@Param("id") Long id,Pageable pageable);
    
    int countByAssociations_Id(@Param("id") Long id);
    
}
