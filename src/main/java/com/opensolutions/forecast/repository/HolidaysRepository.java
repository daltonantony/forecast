package com.opensolutions.forecast.repository;

import com.opensolutions.forecast.domain.Holidays;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Holidays entity.
 */
public interface HolidaysRepository extends JpaRepository<Holidays,Long> {

}
