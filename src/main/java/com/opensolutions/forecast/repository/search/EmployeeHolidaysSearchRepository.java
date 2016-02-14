package com.opensolutions.forecast.repository.search;

import com.opensolutions.forecast.domain.EmployeeHolidays;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the EmployeeHolidays entity.
 */
public interface EmployeeHolidaysSearchRepository extends ElasticsearchRepository<EmployeeHolidays, Long> {
}
