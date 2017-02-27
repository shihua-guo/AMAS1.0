package com.binana.amas.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.binana.amas.domain.Duty;

import com.binana.amas.repository.DutyRepository;
import com.binana.amas.repository.search.DutySearchRepository;
import com.binana.amas.web.rest.util.HeaderUtil;
import com.binana.amas.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Duty.
 */
@RestController
@RequestMapping("/api")
public class DutyResource {

    private final Logger log = LoggerFactory.getLogger(DutyResource.class);

    private static final String ENTITY_NAME = "duty";
        
    private final DutyRepository dutyRepository;

    private final DutySearchRepository dutySearchRepository;

    public DutyResource(DutyRepository dutyRepository, DutySearchRepository dutySearchRepository) {
        this.dutyRepository = dutyRepository;
        this.dutySearchRepository = dutySearchRepository;
    }

    /**
     * POST  /duties : Create a new duty.
     *
     * @param duty the duty to create
     * @return the ResponseEntity with status 201 (Created) and with body the new duty, or with status 400 (Bad Request) if the duty has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/duties")
    @Timed
    public ResponseEntity<Duty> createDuty(@RequestBody Duty duty) throws URISyntaxException {
        log.debug("REST request to save Duty : {}", duty);
        if (duty.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new duty cannot already have an ID")).body(null);
        }
        Duty result = dutyRepository.save(duty);
        dutySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/duties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /duties : Updates an existing duty.
     *
     * @param duty the duty to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated duty,
     * or with status 400 (Bad Request) if the duty is not valid,
     * or with status 500 (Internal Server Error) if the duty couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/duties")
    @Timed
    public ResponseEntity<Duty> updateDuty(@RequestBody Duty duty) throws URISyntaxException {
        log.debug("REST request to update Duty : {}", duty);
        if (duty.getId() == null) {
            return createDuty(duty);
        }
        Duty result = dutyRepository.save(duty);
        dutySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, duty.getId().toString()))
            .body(result);
    }

    /**
     * GET  /duties : get all the duties.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of duties in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/duties")
    @Timed
    public ResponseEntity<List<Duty>> getAllDuties(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Duties");
        Page<Duty> page = dutyRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/duties");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /duties/:id : get the "id" duty.
     *
     * @param id the id of the duty to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the duty, or with status 404 (Not Found)
     */
    @GetMapping("/duties/{id}")
    @Timed
    public ResponseEntity<Duty> getDuty(@PathVariable Long id) {
        log.debug("REST request to get Duty : {}", id);
        Duty duty = dutyRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(duty));
    }

    /**
     * DELETE  /duties/:id : delete the "id" duty.
     *
     * @param id the id of the duty to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/duties/{id}")
    @Timed
    public ResponseEntity<Void> deleteDuty(@PathVariable Long id) {
        log.debug("REST request to delete Duty : {}", id);
        dutyRepository.delete(id);
        dutySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/duties?query=:query : search for the duty corresponding
     * to the query.
     *
     * @param query the query of the duty search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/duties")
    @Timed
    public ResponseEntity<List<Duty>> searchDuties(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Duties for query {}", query);
        Page<Duty> page = dutySearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/duties");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
