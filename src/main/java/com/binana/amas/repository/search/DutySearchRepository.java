package com.binana.amas.repository.search;

import com.binana.amas.domain.Duty;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Duty entity.
 */
public interface DutySearchRepository extends ElasticsearchRepository<Duty, Long> {
}
