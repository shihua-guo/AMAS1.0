package com.binana.amas.repository.search;

import com.binana.amas.domain.Amember;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Amember entity.
 */
public interface AmemberSearchRepository extends ElasticsearchRepository<Amember, Long> {
}
