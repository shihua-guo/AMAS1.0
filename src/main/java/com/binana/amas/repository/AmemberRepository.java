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
import com.binana.amas.domain.GenderPie;

/**
 * Spring Data JPA repository for the Amember entity.
 */
@SuppressWarnings("unused")
public interface AmemberRepository extends JpaRepository<Amember,Long> {
	
    @Query("select distinct amember from Amember amember left join fetch amember.associations left join fetch amember.departments left join fetch amember.roles")
    List<Amember> findAllWithEagerRelationships();

    @Query("select amember from Amember amember left join fetch amember.associations left join fetch amember.departments left join fetch amember.roles where amember.id =:id")
    Amember findOneWithEagerRelationships(@Param("id") Long id);
    
    //获取所有激活了的会员
    Page<Amember> findByStatusIsNull(Pageable pageable);
    //根据社团id获取会员
    Page<Amember> findByAssociations_Id(@Param("id") Long id,Pageable pageable);
    
    //通过社团id获取会员数量
    int countByAssociations_Id(@Param("id") Long id);
    
    /*
    //获取全体会员各个学院数量
    long countByCollege(College college);
    */
    
    //获取全体会员各个学院数量
    @Query("select new com.binana.amas.domain.CollegePie(amember.college, count(1))"
    	 + " from  Amember amember "
    	 + " where amember.college is not null"
    	 + " GROUP by amember.college order by count(1) DESC")
    List<CollegePie> countCollegeMapResult();
    
    /*//获取某协会会员的各个学院数量
    long countByCollegeAndAssociations_Id(College college,Long id);
    */
    @Query("select new com.binana.amas.domain.CollegePie(amember.college, count(1)) "
    	 + "from Amember amember join amember.associations b "
    	 + "where amember.college is not null and b.id =:id "
    	 + " GROUP by amember.college "
    	 + " ORDER by count(1) DESC") 
    List<CollegePie> countCollegeByAssoId(@Param("id") Long id);
    
  //获取全体会员前十专业数量
    @Query("select new com.binana.amas.domain.CommBean(amember.major, count(1))"
    	 + " from  Amember amember "
    	 + " where amember.major is not null  "
    	 + " GROUP by amember.major order by count(1) DESC")
    List<CommBean> countMajorMapResult(Pageable pageable);
    
    //获取某个社团会员前十专业数量
    @Query("select new com.binana.amas.domain.CommBean(amember.major, count(1)) "
       	 + "from Amember amember join amember.associations b "
       	 + "where amember.major is not null and b.id =:id  "
       	 + " GROUP by amember.major "
       	 + " ORDER by count(1) DESC") 
       List<CommBean> countMajorByAssoId(@Param("id") Long id,Pageable pageable);
       
  //获取全体会员男女数量
    @Query("select new com.binana.amas.domain.GenderPie(amember.gender, count(1))"
    	 + " from  Amember amember "
    	 + " where amember.gender is not null "
    	 + " GROUP by amember.gender ")
    List<GenderPie> countGenderMapResult();
    
    //获取某个社团男女数量
    @Query("select new com.binana.amas.domain.GenderPie(amember.gender, count(1)) "
       	 + "from Amember amember join amember.associations b "
       	 + "where amember.gender is not null and b.id =:id "
       	 + "GROUP by amember.gender ") 
    List<GenderPie> countGenderByAssoId(@Param("id") Long id);
    
  //获取全体会员各个年级数量
    @Query("select new com.binana.amas.domain.CommBean(SUBSTRING(amember.membNO, 0, 4), count(1))"
    	 + " from  Amember amember "
    	 + " where amember.membNO is not null "
    	 + " GROUP by amember.membNO ")
    List<CommBean> countGradeMapResult();
  //获取某个社团各个年级数量
    @Query("select new com.binana.amas.domain.CommBean(SUBSTRING(amember.membNO, 0, 4), count(1)) "
       	 + "from Amember amember join amember.associations b "
       	 + "where amember.membNO is not null and b.id =:id "
       	 + "GROUP by amember.membNO ") 
    List<CommBean> countGradeByAssoId(@Param("id") Long id);
}
