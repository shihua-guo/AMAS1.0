package com.binana.amas.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.binana.amas.domain.Activity;
import com.binana.amas.domain.CommBean;
import com.binana.amas.domain.Department;
import com.binana.amas.repository.ActivityRepository;
import com.binana.amas.repository.search.ActivitySearchRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Activity.
 */
@RestController
@RequestMapping("/api")
public class ActivityResource {

    private final Logger log = LoggerFactory.getLogger(ActivityResource.class);

    private static final String ENTITY_NAME = "activity";
        
    private final ActivityRepository activityRepository;

    private final ActivitySearchRepository activitySearchRepository;

    public ActivityResource(ActivityRepository activityRepository, ActivitySearchRepository activitySearchRepository) {
        this.activityRepository = activityRepository;
        this.activitySearchRepository = activitySearchRepository;
    }
    
    /**
     * 获取全体会员各个年级分布分析饼图
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/activityMonthTrendBar/{id}")
    @Timed
    public List<CommBean> getActivityMonthTrendBar(@PathVariable Long id) 
    		throws URISyntaxException{
    	log.debug("REST request 获取某个社团的活动分布柱状图");
    	List<CommBean> cpList = new ArrayList<CommBean>();
    	cpList = activityRepository.getActiMonthTrend(id);
    	return cpList;
    }
    /**
     * 获取全体会员各个年级分布分析饼图
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/activityMonthTrendBar")
    @Timed
    public List<CommBean> getActivityMonthTrendBar() 
    		 throws URISyntaxException{
    	log.debug("REST request 获取全体社团的活动分布柱状图 ");
    	List<CommBean> cpList = new ArrayList<CommBean>();
    	cpList = activityRepository.getActiMonthTrend();
    	return cpList;
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
    @GetMapping("/_search/activitiesOfAsso")
    @Timed
    public ResponseEntity<List<Activity>> searchActivitiesOfAsso(@RequestParam(value="assoId") Long id,@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Activities Of Association for query {} and Association is {}", query,id);
        //查找社团的部门，匹配所有字段
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
        		.must(QueryBuilders.termQuery("assoacti.id",id))
        		.must(queryStringQuery(query));
        Page<Activity> page = activitySearchRepository.search(boolQueryBuilder, pageable);
        for(Activity deptTmp:page.getContent()){
        	System.out.println(deptTmp.toString());
        }
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/activitiesOfAsso");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /getActivitiesByAssoId/:id : get the "id" Association.
     *
     * @param id the id of the departments to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the Activities, or with status 404 (Not Found)
     */
    @GetMapping("/getRecentActivitiesByAssoId")
    @Timed
    public ResponseEntity<List<Activity>> getActivitiesByAssoId(@RequestParam(value="assoId") Long id,@ApiParam Pageable pageable) 
    		 throws URISyntaxException{
    	log.debug("REST request to get a page of Activities by AssociationId {}",id);
    	Page<Activity> page = activityRepository.findActivityByAssoacti_Id(id,pageable);
         HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/getActivitiesByAssoId");
         return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /activities/:id : get the "id" activity.
     *
     * @param id the id of the activity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the activity, or with status 404 (Not Found)
     */
    @GetMapping("/getRecentActivitiesByAssoId/{id}")
    @Timed
    public Activity getRecentActivitiesByAssoId(@PathVariable Long id) {
        log.debug("REST request to get Activity By AssoId: {}", id);
        Activity activity = activityRepository.findTopByAssoacti_idOrderByActiDateDesc(id);
        return activity;
    }
    
    /**
     * POST  /activities : Create a new activity.
     *
     * @param activity the activity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new activity, or with status 400 (Bad Request) if the activity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/activities")
    @Timed
    public ResponseEntity<Activity> createActivity(@Valid @RequestBody Activity activity) throws URISyntaxException {
        log.debug("REST request to save Activity : {}", activity);
        if (activity.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new activity cannot already have an ID")).body(null);
        }
        Activity result = activityRepository.save(activity);
        activitySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /activities : Updates an existing activity.
     *
     * @param activity the activity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated activity,
     * or with status 400 (Bad Request) if the activity is not valid,
     * or with status 500 (Internal Server Error) if the activity couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/activities")
    @Timed
    public ResponseEntity<Activity> updateActivity(@Valid @RequestBody Activity activity) throws URISyntaxException {
        log.debug("REST request to update Activity : {}", activity);
        if (activity.getId() == null) {
            return createActivity(activity);
        }
        Activity result = activityRepository.save(activity);
        activitySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, activity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /activities : get all the activities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of activities in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/activities")
    @Timed
    public ResponseEntity<List<Activity>> getAllActivities(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Activities");
        Page<Activity> page = activityRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /activities/:id : get the "id" activity.
     *
     * @param id the id of the activity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the activity, or with status 404 (Not Found)
     */
    @GetMapping("/activities/{id}")
    @Timed
    public ResponseEntity<Activity> getActivity(@PathVariable Long id) {
        log.debug("REST request to get Activity : {}", id);
        Activity activity = activityRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(activity));
    }

    /**
     * DELETE  /activities/:id : delete the "id" activity.
     *
     * @param id the id of the activity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/activities/{id}")
    @Timed
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        log.debug("REST request to delete Activity : {}", id);
        activityRepository.delete(id);
        activitySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/activities?query=:query : search for the activity corresponding
     * to the query.
     *
     * @param query the query of the activity search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/activities")
    @Timed
    public ResponseEntity<List<Activity>> searchActivities(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Activities for query {}", query);
        Page<Activity> page = activitySearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/activities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
