package com.binana.amas.repository.search;

import com.binana.amas.domain.Activity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Activity entity.
 */
public interface ActivitySearchRepository extends ElasticsearchRepository<Activity, Long> {
}
