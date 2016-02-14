package com.opensolutions.forecast.repository.search;

import com.opensolutions.forecast.domain.EmployeeBillingHours;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the EmployeeBillingHours entity.
 */
public interface EmployeeBillingHoursSearchRepository extends ElasticsearchRepository<EmployeeBillingHours, Long> {
}
