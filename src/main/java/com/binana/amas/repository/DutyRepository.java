package com.binana.amas.repository;

import com.binana.amas.domain.Duty;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Duty entity.
 */
@SuppressWarnings("unused")
public interface DutyRepository extends JpaRepository<Duty,Long> {

}
