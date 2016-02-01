package com.opensolutions.forecast.repository.search;

import com.opensolutions.forecast.domain.EmployeeAllocation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the EmployeeAllocation entity.
 */
public interface EmployeeAllocationSearchRepository extends ElasticsearchRepository<EmployeeAllocation, Long> {
}
