package com.binana.amas.repository;

import com.binana.amas.domain.Role;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Role entity.
 */
@SuppressWarnings("unused")
public interface RoleRepository extends JpaRepository<Role,Long> {

}
