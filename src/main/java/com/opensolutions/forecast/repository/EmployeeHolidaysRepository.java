package com.opensolutions.forecast.repository;

import com.opensolutions.forecast.domain.EmployeeHolidays;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EmployeeHolidays entity.
 */
public interface EmployeeHolidaysRepository extends JpaRepository<EmployeeHolidays,Long> {

}
