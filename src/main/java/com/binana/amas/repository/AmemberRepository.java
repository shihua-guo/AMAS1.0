package com.binana.amas.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.binana.amas.domain.Amember;
import com.binana.amas.domain.CollegePie;
import com.binana.amas.domain.CommBean;
import com.binana.amas.domain.enumeration.College;

/**
 * Spring Data JPA repository for the Amember entity.
 */
@SuppressWarnings("unused")
public interface AmemberRepository extends JpaRepository<Amember,Long> {

    @Query("select distinct amember from Amember amember left join fetch amember.associations left join fetch amember.departments left join fetch amember.roles")
    List<Amember> findAllWithEagerRelationships();

    @Query("select amember from Amember amember left join fetch amember.associations left join fetch amember.departments left join fetch amember.roles where amember.id =:id")
    Amember findOneWithEagerRelationships(@Param("id") Long id);
    
    //根据社团id获取会员
    Page<Amember> findByAssociations_Id(@Param("id") Long id,Pageable pageable);
    
    //通过社团id获取会员数量
    int countByAssociations_Id(@Param("id") Long id);
    /*
    //获取全体会员各个学院数量
    long countByCollege(College college);
    */
    
    //获取全体会员各个学院数量
    @Query("select new com.binana.amas.domain.CollegePie(amember.college, count(*))"
    	 + " from  Amember amember "
    	 + " where amember.college is not null"
    	 + " GROUP by amember.college order by count(*) DESC")
    List<CollegePie> countCollegeMapResult();
    
    //获取某协会会员的各个学院数量
    long countByCollegeAndAssociations_Id(College college,Long id);
    
    
    @Query("select new com.binana.amas.domain.CollegePie(amember.college, count(*)) "
    	 + "from Amember amember join amember.associations b "
    	 + "where amember.college is not null and b.id =:id "
    	 + " GROUP by amember.college "
    	 + " ORDER by count(*) DESC") 
    List<CollegePie> countCollegeByAssoId(@Param("id") Long id);
}
