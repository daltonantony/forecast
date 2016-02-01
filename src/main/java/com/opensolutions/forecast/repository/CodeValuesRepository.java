package com.opensolutions.forecast.repository;

import com.opensolutions.forecast.domain.CodeValues;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CodeValues entity.
 */
public interface CodeValuesRepository extends JpaRepository<CodeValues,Long> {

}
