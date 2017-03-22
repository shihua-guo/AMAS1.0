package com.binana.amas.repository;

import com.binana.amas.domain.Activity;
import com.binana.amas.domain.Department;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Activity entity.
 */
@SuppressWarnings("unused")
public interface ActivityRepository extends JpaRepository<Activity,Long> {
	//获取社团最近的一次活动
	Activity findTopByAssoacti_idOrderByActiDateDesc(@Param("id") Long id);
	//获取社团所有的活动
	Page<Activity> findActivityByAssoacti_Id(@Param("id") Long id,Pageable pageable);
	
}
