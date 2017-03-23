package com.binana.amas.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.lucene.search.TermQuery;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.binana.amas.domain.Amember;
import com.binana.amas.repository.AmemberRepository;
import com.binana.amas.repository.search.AmemberSearchRepository;
import com.binana.amas.web.rest.util.HeaderUtil;
import com.binana.amas.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Amember.
 */
@RestController
@RequestMapping("/api")
public class AmemberResource {

    private final Logger log = LoggerFactory.getLogger(AmemberResource.class);

    private static final String ENTITY_NAME = "amember";
        
    private final AmemberRepository amemberRepository;

    private final AmemberSearchRepository amemberSearchRepository;

    public AmemberResource(AmemberRepository amemberRepository, AmemberSearchRepository amemberSearchRepository) {
        this.amemberRepository = amemberRepository;
        this.amemberSearchRepository = amemberSearchRepository;
    }

    
    /**
     * SEARCH  /_search/amembers?query=:query : search for the amember corresponding
     * to the query.
     *
     * @param query the query of the amember search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/amembersOfAsso")
    @Timed
    public ResponseEntity<List<Amember>> searchAmembersOfAsso(@RequestParam(value="assoId") Long id,@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Amembers Of Association for query {} and Association is {}", query,id);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
        		.must(QueryBuilders.termQuery("associations.id",id))
        		.must(queryStringQuery(query));
        Page<Amember> page = amemberSearchRepository.search(boolQueryBuilder, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/amembers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * Get 获取该社团的成员数量
     * @param id
     * @return
     */
    @GetMapping("/getAssoAmemberNum/{id}")
    @Timed
    public int getAssoAmemberNum(@PathVariable Long id) {
    	log.debug("REST request to get Number of Amembers of Association id : {}", id);
    	int count = amemberRepository.countByAssociations_Id(id);
    	return count;
    }
    
    /**
     * GET  /amembers/:id : get the "id" amember.
     *
     * @param id the id of the amember to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the amember, or with status 404 (Not Found)
     */
    @GetMapping("/amembersOfAsso")
    @Timed
    public ResponseEntity<List<Amember>> getByAssoId(@RequestParam(value="assoId") Long id,@ApiParam Pageable pageable) 
    		 throws URISyntaxException{
    	log.debug("REST request to get a page of Amembers by AssociationId {}",id);
    	Page<Amember> page = amemberRepository.findByAssociations_Id(id,pageable);
         HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/amembersOfAsso");
         return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * POST  /amembers : Create a new amember.
     *
     * @param amember the amember to create
     * @return the ResponseEntity with status 201 (Created) and with body the new amember, or with status 400 (Bad Request) if the amember has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/amembers")
    @Timed
    public ResponseEntity<Amember> createAmember(@Valid @RequestBody Amember amember) throws URISyntaxException {
        log.debug("REST request to save Amember : {}", amember);
        if (amember.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new amember cannot already have an ID")).body(null);
        }
        Amember result = amemberRepository.save(amember);
        amemberSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/amembers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /amembers : Updates an existing amember.
     *
     * @param amember the amember to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated amember,
     * or with status 400 (Bad Request) if the amember is not valid,
     * or with status 500 (Internal Server Error) if the amember couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/amembers")
    @Timed
    public ResponseEntity<Amember> updateAmember(@Valid @RequestBody Amember amember) throws URISyntaxException {
        log.debug("REST request to update Amember : {}", amember);
        if (amember.getId() == null) {
            return createAmember(amember);
        }
        Amember result = amemberRepository.save(amember);
        amemberSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, amember.getId().toString()))
            .body(result);
    }

    /**
     * GET  /amembers : get all the amembers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of amembers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/amembers")
    @Timed
    public ResponseEntity<List<Amember>> getAllAmembers(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Amembers");
        Page<Amember> page = amemberRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/amembers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /amembers/:id : get the "id" amember.
     *
     * @param id the id of the amember to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the amember, or with status 404 (Not Found)
     */
    @GetMapping("/amembers/{id}")
    @Timed
    public ResponseEntity<Amember> getAmember(@PathVariable Long id) {
        log.debug("REST request to get Amember : {}", id);
        Amember amember = amemberRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(amember));
    }
    
    
    /**
     * DELETE  /amembers/:id : delete the "id" amember.
     *
     * @param id the id of the amember to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/amembers/{id}")
    @Timed
    public ResponseEntity<Void> deleteAmember(@PathVariable Long id) {
        log.debug("REST request to delete Amember : {}", id);
        amemberRepository.delete(id);
        amemberSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/amembers?query=:query : search for the amember corresponding
     * to the query.
     *
     * @param query the query of the amember search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/amembers")
    @Timed
    public ResponseEntity<List<Amember>> searchAmembers(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Amembers for query {}", query);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("associations.id","2"));
        BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery()
        		.must(QueryBuilders.termQuery("associations.id","2"))
        		.must(queryStringQuery(query));
        Page<Amember> page = amemberSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/amembers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
