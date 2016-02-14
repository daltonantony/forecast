package com.opensolutions.forecast.repository;

import com.opensolutions.forecast.domain.EmployeeBillingHours;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EmployeeBillingHours entity.
 */
public interface EmployeeBillingHoursRepository extends JpaRepository<EmployeeBillingHours,Long> {

}
