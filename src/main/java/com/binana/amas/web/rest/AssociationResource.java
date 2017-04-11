package com.binana.amas.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.binana.amas.domain.Association;

import com.binana.amas.repository.AssociationRepository;
import com.binana.amas.repository.search.AssociationSearchRepository;
import com.binana.amas.web.rest.util.HeaderUtil;
import com.binana.amas.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Association.
 */
@RestController
@RequestMapping("/api")
public class AssociationResource {

    private final Logger log = LoggerFactory.getLogger(AssociationResource.class);

    private static final String ENTITY_NAME = "association";
        
    private final AssociationRepository associationRepository;

    private final AssociationSearchRepository associationSearchRepository;
    
    public AssociationResource(AssociationRepository associationRepository, AssociationSearchRepository associationSearchRepository) {
        this.associationRepository = associationRepository;
        this.associationSearchRepository = associationSearchRepository;
    }
    
    /**
     * GET  /associations : 获取所有社团的id和名字.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of associations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/getAssoIdAndName")
    @Timed
    public List<Association> getAssoIdAndName()
        throws URISyntaxException {
        log.debug("REST request to get a page of Associations");
        List<Association> findAll = associationRepository.findAssoIdAndAssoName();
        return findAll;
        
    }
    
    /**
     * POST  /associations : Create a new association.
     *
     * @param association the association to create
     * @return the ResponseEntity with status 201 (Created) and with body the new association, or with status 400 (Bad Request) if the association has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/associations")
    @Timed
    public ResponseEntity<Association> createAssociation(@Valid @RequestBody Association association) throws URISyntaxException {
        log.debug("REST request to save Association : {}", association);
        if (association.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new association cannot already have an ID")).body(null);
        }
        Association result = associationRepository.save(association);
        associationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/associations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /associations : Updates an existing association.
     *
     * @param association the association to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated association,
     * or with status 400 (Bad Request) if the association is not valid,
     * or with status 500 (Internal Server Error) if the association couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/associations")
    @Timed
    public ResponseEntity<Association> updateAssociation(@Valid @RequestBody Association association) throws URISyntaxException {
        log.debug("REST request to update Association : {}", association);
        if (association.getId() == null) {
            return createAssociation(association);
        }
        Association result = associationRepository.save(association);
        associationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, association.getId().toString()))
            .body(result);
    }

    /**
     * GET  /associations : get all the associations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of associations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/associations")
    @Timed
    public ResponseEntity<List<Association>> getAllAssociations(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Associations");
        Page<Association> page = associationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/associations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /associations/:id : get the "id" association.
     *
     * @param id the id of the association to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the association, or with status 404 (Not Found)
     */
    @GetMapping("/associations/{id}")
    @Timed
    public ResponseEntity<Association> getAssociation(@PathVariable Long id) {
        log.debug("REST request to get Association : {}", id);
        Association association = associationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(association));
    }

    /**
     * DELETE  /associations/:id : delete the "id" association.
     *
     * @param id the id of the association to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/associations/{id}")
    @Timed
    public ResponseEntity<Void> deleteAssociation(@PathVariable Long id) {
        log.debug("REST request to delete Association : {}", id);
        associationRepository.delete(id);
        associationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/associations?query=:query : search for the association corresponding
     * to the query.
     *
     * @param query the query of the association search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/associations")
    @Timed
    public ResponseEntity<List<Association>> searchAssociations(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Associations for query {}", query);
        Page<Association> page = associationSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/associations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
