package com.opensolutions.forecast.repository;

import com.opensolutions.forecast.domain.EmployeeHours;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EmployeeHours entity.
 */
public interface EmployeeHoursRepository extends JpaRepository<EmployeeHours,Long> {

}
