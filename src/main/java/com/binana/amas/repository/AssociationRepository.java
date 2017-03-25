package com.binana.amas.repository;

import java.util.List;

import javax.persistence.ColumnResult;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.SqlResultSetMapping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.binana.amas.domain.Association;

/**
 * Spring Data JPA repository for the Association entity.
 */
@SqlResultSetMapping(name="OrderResults", 
entities={ 
		@EntityResult(entityClass=com.binana.amas.domain.Association.class, fields={
				@FieldResult(name="id", column="order_id"),
				@FieldResult(name="quantity", column="order_quantity"), 
				@FieldResult(name="item", column="order_item")})},
columns={
		@ColumnResult(name="item_name")}
		)
@SuppressWarnings("unused")
public interface AssociationRepository extends JpaRepository<Association,Long> {
	
	/*@Query("select amember from Amember amember where amember.id=:id")
	Set<Amember> findByMembassos_(@Param("id") Long id);*/
	@Query("select id,assoName from Association association")
	List<Object[]> findAssoIdAndAssoName();
	
	
	
	
}
