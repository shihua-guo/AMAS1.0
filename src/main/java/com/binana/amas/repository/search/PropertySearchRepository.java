package com.binana.amas.repository.search;

import com.binana.amas.domain.Property;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Property entity.
 */
public interface PropertySearchRepository extends ElasticsearchRepository<Property, Long> {
}
