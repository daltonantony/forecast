package com.opensolutions.forecast.repository.search;

import com.opensolutions.forecast.domain.CodeValues;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CodeValues entity.
 */
public interface CodeValuesSearchRepository extends ElasticsearchRepository<CodeValues, Long> {
}
