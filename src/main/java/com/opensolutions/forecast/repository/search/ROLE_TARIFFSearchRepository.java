package com.opensolutions.forecast.repository.search;

import com.opensolutions.forecast.domain.ROLE_TARIFF;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ROLE_TARIFF entity.
 */
public interface ROLE_TARIFFSearchRepository extends ElasticsearchRepository<ROLE_TARIFF, Long> {
}
