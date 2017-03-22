package com.binana.amas.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.binana.amas.domain.Amember;
import com.binana.amas.domain.Department;

import com.binana.amas.repository.DepartmentRepository;
import com.binana.amas.repository.search.DepartmentSearchRepository;
import com.binana.amas.web.rest.util.HeaderUtil;
import com.binana.amas.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Department.
 */
@RestController
@RequestMapping("/api")
public class DepartmentResource {

    private final Logger log = LoggerFactory.getLogger(DepartmentResource.class);

    private static final String ENTITY_NAME = "department";
        
    private final DepartmentRepository departmentRepository;

    private final DepartmentSearchRepository departmentSearchRepository;

    public DepartmentResource(DepartmentRepository departmentRepository, DepartmentSearchRepository departmentSearchRepository) {
        this.departmentRepository = departmentRepository;
        this.departmentSearchRepository = departmentSearchRepository;
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
    @GetMapping("/_search/departmentsOfAsso")
    @Timed
    public ResponseEntity<List<Department>> searchDepartmentsOfAsso(@RequestParam(value="assoId") Long id,@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Departments Of Association for query {} and Association is {}", query,id);
        //查找社团的部门，匹配所有字段
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
        		.must(QueryBuilders.termQuery("assodept.id",id))
        		.must(queryStringQuery(query));
        Page<Department> page = departmentSearchRepository.search(boolQueryBuilder, pageable);
        for(Department deptTmp:page.getContent()){
        	System.out.println(deptTmp.toString());
        }
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/departmentsOfAsso");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /departmentsOfAsso/:id : get the "id" departments.
     *
     * @param id the id of the departments to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the amember, or with status 404 (Not Found)
     */
    @GetMapping("/departmentsOfAsso")
    @Timed
    public ResponseEntity<List<Department>> getDeptsByAssoId(@RequestParam(value="assoId") Long id,@ApiParam Pageable pageable) 
    		 throws URISyntaxException{
    	log.debug("REST request to get a page of Departments by AssociationId {}",id);
    	Page<Department> page = departmentRepository.findDepartmentByAssodept_Id(id,pageable);
         HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/departmentsOfAsso");
         return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * Get 获取该社团的部门名称
     * @param id
     * @return
     */
    @GetMapping("/getAssoDeptNameByAssoId/{id}")
    @Timed
    public List<String> getAssoDeptNameByAssoId(@PathVariable Long id) {
    	log.debug("REST request to get Number of Amembers of Association id : {}", id);
    	List<String> deptName = departmentRepository.getAssoDeptNameByAssoId(id);
    	return deptName;
    }
    /**
     * POST  /departments : Create a new department.
     *
     * @param department the department to create
     * @return the ResponseEntity with status 201 (Created) and with body the new department, or with status 400 (Bad Request) if the department has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/departments")
    @Timed
    public ResponseEntity<Department> createDepartment(@Valid @RequestBody Department department) throws URISyntaxException {
        log.debug("REST request to save Department : {}", department);
        if (department.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new department cannot already have an ID")).body(null);
        }
        Department result = departmentRepository.save(department);
        departmentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/departments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /departments : Updates an existing department.
     *
     * @param department the department to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated department,
     * or with status 400 (Bad Request) if the department is not valid,
     * or with status 500 (Internal Server Error) if the department couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/departments")
    @Timed
    public ResponseEntity<Department> updateDepartment(@Valid @RequestBody Department department) throws URISyntaxException {
        log.debug("REST request to update Department : {}", department);
        if (department.getId() == null) {
            return createDepartment(department);
        }
        Department result = departmentRepository.save(department);
        departmentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, department.getId().toString()))
            .body(result);
    }

    /**
     * GET  /departments : get all the departments.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of departments in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/departments")
    @Timed
    public ResponseEntity<List<Department>> getAllDepartments(@ApiParam Pageable pageable, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("duty-is-null".equals(filter)) {
            log.debug("REST request to get all Departments where duty is null");
            return new ResponseEntity<>(StreamSupport
                .stream(departmentRepository.findAll().spliterator(), false)
                .filter(department -> department.getDuty() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Departments");
        Page<Department> page = departmentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/departments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /departments/:id : get the "id" department.
     *
     * @param id the id of the department to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the department, or with status 404 (Not Found)
     */
    @GetMapping("/departments/{id}")
    @Timed
    public ResponseEntity<Department> getDepartment(@PathVariable Long id) {
        log.debug("REST request to get Department : {}", id);
        Department department = departmentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(department));
    }

    /**
     * DELETE  /departments/:id : delete the "id" department.
     *
     * @param id the id of the department to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/departments/{id}")
    @Timed
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        log.debug("REST request to delete Department : {}", id);
        departmentRepository.delete(id);
        departmentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/departments?query=:query : search for the department corresponding
     * to the query.
     *
     * @param query the query of the department search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/departments")
    @Timed
    public ResponseEntity<List<Department>> searchDepartments(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Departments for query {}", query);
        Page<Department> page = departmentSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/departments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
