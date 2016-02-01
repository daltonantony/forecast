package com.opensolutions.forecast.repository.search;

import com.opensolutions.forecast.domain.Holidays;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Holidays entity.
 */
public interface HolidaysSearchRepository extends ElasticsearchRepository<Holidays, Long> {
}
