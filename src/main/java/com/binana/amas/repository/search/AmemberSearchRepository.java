package com.binana.amas.repository.search;

import com.binana.amas.domain.Amember;

import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Amember entity.
 */
public interface AmemberSearchRepository extends ElasticsearchRepository<Amember, Long> {
	//Page<Amember> searchByAssociations_Id(Long id,QueryBuilder query, Pageable pageable);
}
