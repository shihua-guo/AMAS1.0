package com.binana.amas.repository.search;

import com.binana.amas.domain.Association;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Association entity.
 */
public interface AssociationSearchRepository extends ElasticsearchRepository<Association, Long> {
}
