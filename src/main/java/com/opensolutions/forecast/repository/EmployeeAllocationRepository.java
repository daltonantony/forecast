package com.opensolutions.forecast.repository;

import com.opensolutions.forecast.domain.EmployeeAllocation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EmployeeAllocation entity.
 */
public interface EmployeeAllocationRepository extends JpaRepository<EmployeeAllocation,Long> {

}
