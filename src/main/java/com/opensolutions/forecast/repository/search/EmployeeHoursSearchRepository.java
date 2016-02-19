package com.opensolutions.forecast.repository.search;

import com.opensolutions.forecast.domain.EmployeeHours;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the EmployeeHours entity.
 */
public interface EmployeeHoursSearchRepository extends ElasticsearchRepository<EmployeeHours, Long> {
}
