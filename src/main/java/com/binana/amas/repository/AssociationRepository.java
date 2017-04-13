package com.binana.amas.repository;

import com.binana.amas.domain.Association;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Association entity.
 */
@SuppressWarnings("unused")
public interface AssociationRepository extends JpaRepository<Association,Long> {

    @Query("select association from Association association where association.user.login = ?#{principal.username}")
    List<Association> findByUserIsCurrentUser();
    
  //获取某个社团各个年级数量
    @Query("select new com.binana.amas.domain.Association(id,assoId,assoName) "
       	 + "from Association association ") 
    List<Association> findAssoIdAndAssoName(); 
	
}
