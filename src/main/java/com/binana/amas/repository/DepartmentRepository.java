package com.binana.amas.repository;

import com.binana.amas.domain.Department;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Department entity.
 */
@SuppressWarnings("unused")
public interface DepartmentRepository extends JpaRepository<Department,Long> {
	//获取社团部门的名字
	@Query("select deptName from Department d where d.assodept.id =:id")
	List<String> getAssoDeptNameByAssoId(@Param("id") Long id);
	
	//获取社团的部门的所有信息
	Page<Department> findDepartmentByAssodept_Id(@Param("id") Long id,Pageable pageable);
}
